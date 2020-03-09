package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {
    public static final int MAP_CELLS_WIDTH = 16;
    public static final int MAP_CELLS_HEIGHT = 9;

    private byte[][] data;
    private TextureRegion grassTexture;
    private TextureRegion wallTexture;

    public Map() {
        this.data = new byte[MAP_CELLS_WIDTH][MAP_CELLS_HEIGHT];
        this.data[2][2] = 1;
        this.data[2][4] = 1;
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.wallTexture = Assets.getInstance().getAtlas().findRegion("wall");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < MAP_CELLS_WIDTH; i++) {
            for (int j = MAP_CELLS_HEIGHT-1; j >= 0 ; j--) {
                batch.draw(grassTexture, i * 80, j * 80);
                if (data[i][j]  == 1) {
                    batch.draw(wallTexture, i * 80, j * 80 + 40);
                }
            }
        }
    }
}
