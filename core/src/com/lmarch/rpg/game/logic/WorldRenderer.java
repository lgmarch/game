package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.screens.utils.Assets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font24;
    private BitmapFont font8;
    private List<MapElement>[] drawables; //Список объектов по полосам
    private Comparator<MapElement> yComparator;

    public WorldRenderer(GameController gameController, SpriteBatch batch) {
        this.gc = gameController;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font8 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.batch = batch;
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
    }

    public void render() {
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

        for (int i = 0; i < drawables.length; i++) {
            Collections.sort(drawables[i], yComparator);
        }

        //Цвет очистки экрана: выбор цвета
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //Очистка экрана
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
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
                drawables[y].get(i).render(batch, font8);
            }
            //Рисуем объекты карты
            for (int x = 0; x < Map.MAP_CELLS_WIDTH; x++) {
                gc.getMap().renderTree(batch, x, y);
                //gc.getMap().renderStone(batch, x, y);
                gc.getMap().renderOak(batch, x, y);
            }
        }

        gc.getHero().renderGUI(batch, font24);
        batch.end();
    }
}
