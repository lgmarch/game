package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Map {
    public static final int MAP_CELLS_WIDTH = 16;
    public static final int MAP_CELLS_HEIGHT = 9;

    private byte[][] data;
    private TextureRegion grassTexture;
    private TextureRegion wallTexture;

    //Проверка проходжения летящего объекта через ячейку (вода, стена)
    public boolean isAirPassable(int cellX, int cellY){
        return data[cellX][cellY]  == 0;
    }

    public boolean isGroundPassable(int cellX, int cellY){
        if (cellX < 0 || cellY < 0 || cellX >= MAP_CELLS_WIDTH || cellY >= MAP_CELLS_HEIGHT) {
            return false;
        }
        return data[cellX][cellY]  == 0;
    }

    public boolean isGroundPassable(Vector2 position){
        return isGroundPassable((int) (position.x / 80), (int) (position.y / 80));
    }

    public Map() {
        this.data = new byte[MAP_CELLS_WIDTH][MAP_CELLS_HEIGHT];
        for (int i = 0; i < 5; i++) {
            data[MathUtils.random(15)][MathUtils.random(8)] = 1;
        }
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.wallTexture = Assets.getInstance().getAtlas().findRegion("wall");
    }

    //Отрисовка земли в клетке
    public void renderGround(SpriteBatch batch, int x, int y){
        batch.draw(grassTexture, x * 80, y * 80);
    }

    //Отрисовка объекта в клетке на земле
    public void renderUpper(SpriteBatch batch, int x, int y) {
        if (data[x][y]  == 1) {
            batch.draw(wallTexture, x * 80, y * 80);
        }
    }

    //Отрисовка оставшегося оружия
    public void renderFreeWeapons(SpriteBatch batch, TextureRegion tr, int x, int y) {
        //batch.draw(Assets.getInstance().getAtlas().findRegion("metal"), x, y, 35, 35);
        batch.draw(tr, x, y, 55, 55);
    }
}
