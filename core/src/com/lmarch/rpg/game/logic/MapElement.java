package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Элемент на карте
 */
public interface MapElement {
    void render(SpriteBatch batch, BitmapFont font);
    int getCellX();
    int getCellY();
}
