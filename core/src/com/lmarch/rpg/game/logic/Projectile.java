package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Projectile implements Poolable, MapElement{
    private TextureRegion textureRegion;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public int getCellX() {
        return (int) position.x / 80;
    }

    @Override
    public int getCellY() {
        return (int) position.y / 80;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    //Создаем болванки
    public Projectile() {
        this.textureRegion = null;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void setup(TextureRegion textureRegion, float x, float y, float targetX, float targetY){
        this.textureRegion = textureRegion;
        this.position.set(x, y);
        //tmp.set(targetX, targetY).sub(x, y).nor().scl(500.0f);
        this.velocity.set(targetX, targetY).sub(x, y).nor().scl(800.0f);
        this.active = true;
    }

    public void deactivate(){
        active = false;
    }

    //Отрисовка стрелы
    @Override
    public void render(SpriteBatch batch, BitmapFont font){
        batch.draw(textureRegion, position.x - 30, position.y - 30,
                30, 30, 60, 60, 1, 1, velocity.angle());
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720){
            deactivate();
        }
    }
}