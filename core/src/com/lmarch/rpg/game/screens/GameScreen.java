package com.lmarch.rpg.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lmarch.rpg.game.logic.GameController;
import com.lmarch.rpg.game.logic.WorldRenderer;

public class GameScreen extends AbstractScreen {
    private GameController gc;
    private WorldRenderer worldRenderer;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override //Инициализация
    public void show() {
        this.gc = new GameController();
        this.worldRenderer = new WorldRenderer(gc, batch);
    }

    @Override //Отрисовка
    public void render(float delta) {
        gc.update(delta);
        worldRenderer.render();
    }
}
