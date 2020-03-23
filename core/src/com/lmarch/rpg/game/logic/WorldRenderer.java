package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.screens.ScreenManager;
import com.lmarch.rpg.game.screens.utils.Assets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font20;
    private BitmapFont font12;
    private List<MapElement>[] drawables; //Список объектов по полосам
    private Vector2 pov;

    private Comparator<MapElement> yComparator;

    private FrameBuffer frameBuffer; //Кадр экрана можно занести в буфер
    private TextureRegion frameBufferRegion;
    private ShaderProgram shaderProgram; //

    public WorldRenderer(GameController gameController, SpriteBatch batch) {
        this.gc = gameController;
        this.font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");
        this.font12 = Assets.getInstance().getAssetManager().get("fonts/font12.ttf");
        this.batch = batch;
        this.pov = new Vector2(0, 0);
        //Список объектов, находящихся на определенной линии карты. Инициализация
        this.drawables = new ArrayList[Map.MAP_CELLS_HEIGHT];
        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = new ArrayList<>();
        }
        this.yComparator = new Comparator<MapElement>() {
            @Override
            public int compare(MapElement o1, MapElement o2) {
                return (int) (o2.getY() - o1.getY());
            }
        };

        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, ScreenManager.WORLD_WIDTH, ScreenManager.WORLD_HEIGHT, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.frameBufferRegion.flip(false, true);
        this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(), Gdx.files.internal("shaders/fragment.glsl").readString());
        //Компиляция Шейдера
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());
        }
    }

    public void render() {
        pov.set(gc.getHero().getPosition());
        if (pov.x < ScreenManager.HALF_WORLD_WIDTH) {
            pov.x = ScreenManager.HALF_WORLD_WIDTH;
        }
        if (pov.y < ScreenManager.HALF_WORLD_HEIGHT) {
            pov.y = ScreenManager.HALF_WORLD_HEIGHT;
        }
        if (pov.x > gc.getMap().getWidthLimit() - ScreenManager.HALF_WORLD_WIDTH) {
            pov.x = gc.getMap().getWidthLimit() - ScreenManager.HALF_WORLD_WIDTH;
        }
        if (pov.y > gc.getMap().getHeightLimit() - ScreenManager.HALF_WORLD_HEIGHT) {
            pov.y = gc.getMap().getHeightLimit() - ScreenManager.HALF_WORLD_HEIGHT;
        }

        ScreenManager.getInstance().pointCameraTo(pov);

        //На каждом кадре перераскладываем объекты в массив
        for (List<MapElement> drawable : drawables) {
            drawable.clear();
        }
        drawables[gc.getHero().getCellY()].add(gc.getHero());

        for (int i = 0; i < gc.getWeaponsController().getActiveList().size(); i++) {
            Weapon w = gc.getWeaponsController().getActiveList().get(i);
            drawables[w.getCellY()].add(w);
        }

        for (Treasure treasure : gc.getTreasureController().getActiveList()) {
            if (treasure.isFree()) drawables[treasure.getCellY()].add(treasure);
        }

        for (Monster monster : gc.getMonstersController().getActiveList()) {
            drawables[monster.getCellY()].add(monster);
        }
        for (int i = 0; i < gc.getProjectilesController().getActiveList().size(); i++) {
            Projectile p = gc.getProjectilesController().getActiveList().get(i);
            drawables[p.getCellY()].add(p);
        }

        for (List<MapElement> drawable : drawables) {
            drawable.sort(yComparator);
        }

        frameBuffer.begin(); //Вывод во фрейм буфер
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //Цвет очистки экрана: выбор цвета
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //Очистка экрана
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Рисуем всю землю
        for (int y = Map.MAP_CELLS_HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < Map.MAP_CELLS_WIDTH; x++) {
                gc.getMap().renderGround(batch, x, y);
            }
        }
        //Рисуем сверху
        for (int y = Map.MAP_CELLS_HEIGHT - 1; y >= 0; y--) {
            //Рисуем персонажей
            for (int i = 0; i < drawables[y].size(); i++) {
                drawables[y].get(i).render(batch, font12);
            }
            //Рисуем объекты карты
            for (int x = 0; x < Map.MAP_CELLS_WIDTH; x++) {
                gc.getMap().renderTree(batch, x, y);
            }
        }
        //gc.getSpecialEffectsController().render(batch);
        batch.end();
        frameBuffer.end();

        ScreenManager.getInstance().resetCamera();

        batch.begin();
        batch.setShader(shaderProgram); //Устанавливаем шейдерную программу
        shaderProgram.setUniformf(shaderProgram.getUniformLocation("time"), gc.getWorldTimer());
        shaderProgram.setUniformf(shaderProgram.getUniformLocation("px"), 640.f / 1280.0f);
        shaderProgram.setUniformf(shaderProgram.getUniformLocation("py"), 360.f / 720.0f);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(frameBufferRegion, 0, 0); //Рисуем разом весь фрейм на экран
        batch.end();
        batch.setShader(null); //Сброс Шейдера

        batch.begin();
        gc.getHero().renderGUI(batch, font20);
        batch.end();

        ScreenManager.getInstance().pointCameraTo(pov);
    }
}
