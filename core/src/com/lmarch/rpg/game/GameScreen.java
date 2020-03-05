package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen extends AbstractScreen {
    private BitmapFont font24;
    private TextureRegion textureGrass;
    private ProjectilesController projectilesController;
    private Hero hero;

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override //Инициализация
    public void show() {
        this.projectilesController = new ProjectilesController();
        this.hero = new Hero(this);
        this.textureGrass = Assets.getInstance().getAtlas().findRegion("grass");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
    }

    public void update(float dt){
        hero.update(dt);
        projectilesController.update(dt);
    }

    @Override //Отрисовка
    public void render(float delta) {
        update(delta);
        //Цвет очистки экрана: выбор цвета
        Gdx.gl.glClearColor(1, 1, 1, 1);
        //Очистка экрана
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(textureGrass, i * 80, j * 80);
            }
        }
        hero.render(batch);
        projectilesController.render(batch);
        hero.renderGUI(batch, font24);//GUI желательно рисовать отдельно
        batch.end();
    }
}
