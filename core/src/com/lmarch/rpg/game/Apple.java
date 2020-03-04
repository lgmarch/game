package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Apple {
    private TextureRegion textureApple;
    private Vector2 position; //Позиция яблока
    private Circle areaApple;

    public Apple(TextureAtlas atlas) {
        this.textureApple = atlas.findRegion("apple");
        this.position = new Vector2(getRandomCoordinate(0, 1280), getRandomCoordinate(0, 720));
        this.areaApple = new Circle(position.x+32, position.y+32, 20);
    }

    public void render(SpriteBatch batch){
        batch.draw(textureApple, position.x, position.y);
    }

    public Circle getAreaApple() {
        return areaApple;
    }

    public void setPosition() {
        this.position.set(getRandomCoordinate(0, 1280), getRandomCoordinate(0, 720));
        this.areaApple.setPosition(position.x+32, position.y+32);
    }

    public float getRandomCoordinate(int min, int max) {
        Random random = new Random();
        return random.ints(min+32, max-32).findFirst().getAsInt();
    }
}
