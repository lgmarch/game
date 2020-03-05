package com.lmarch.rpg.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractScreen implements Screen {
    protected SpriteBatch batch;

    public AbstractScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override //Масштабирование
    public void resize(int width, int height) {
    }

    @Override //Режим ожидания приложения
    public void pause() {
    }

    @Override //Выход из состояния hide
    public void resume() {
    }

    @Override //Приложение свернули
    public void hide() {
    }

    @Override //
    public void dispose() {
    }
}
