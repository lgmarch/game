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
    private TextureRegion stoneTexture;
    private TextureRegion treeTexture;
    private TextureRegion oakTexture;

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
        calculateBarrierMatrix();
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.stoneTexture = Assets.getInstance().getAtlas().findRegion("stoun");
        this.treeTexture = Assets.getInstance().getAtlas().findRegion("tree");
        this.oakTexture = Assets.getInstance().getAtlas().findRegion("wool");
    }

    //Отрисовка земли в клетке
    public void renderGround(SpriteBatch batch, int x, int y){
        batch.draw(grassTexture, x * 80, y * 80);
    }

    //Отрисовка объекта в клетке на земле
    public void renderTree(SpriteBatch batch, int x, int y) {
        if (data[x][y]  == 1) {
            batch.draw(treeTexture, x * 80, y * 80);
        }
    }

//    //Отрисовка объекта в клетке на земле
//    public void renderStone(SpriteBatch batch, int x, int y) {
//        if (data[x][y]  == 2) {
//            batch.draw(stoneTexture, x * 80, y * 80);
//        }
//    }

    //Отрисовка объекта в клетке на земле
    public void renderOak(SpriteBatch batch, int x, int y) {
        if (data[x][y]  == 3) {
            batch.draw(oakTexture, x * 80, y * 80);
        }
    }

    public void calculateBarrierMatrix() {
        //Tree
        for (int i = 0; i < 3; i++) {
            data[MathUtils.random(15)][MathUtils.random(8)] = 1;
        }
//        //Stone
//        for (int i = 0; i < 2; i++) {
//            data[MathUtils.random(15)][MathUtils.random(8)] = 2;
//        }
        //Oak
        for (int i = 0; i < 3; i++) {
            data[MathUtils.random(15)][MathUtils.random(8)] = 3;
        }
    }
}
