package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.logic.utils.Poolable;

/**
 * Класс для отображения на игровом поле выпадающих из Монстров сокровищ
 */
public class Message implements MapElement, Poolable {

    private StringBuilder message;
    private Vector2 position;
    private Vector2 tmp;
    private Vector2 tmp1;
    private Vector2 dst;
    protected float speed;
    private Color color;
    private float lifeTime;

    public Message() {
        this.message = new StringBuilder();
        this.position = new Vector2(0.0f, 0.0f );
        this.tmp = new Vector2(0.0f, 0.0f );
        this.tmp1 = new Vector2(0.0f, 0.0f );
        this.dst = new Vector2(0.0f, 0.0f );
        this.lifeTime = 3.0f;
        this.speed = 1.0f;
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        font.setColor(color);
        font.draw(batch, message, position.x, position.y);
    }

    @Override
    public boolean isActive() {
        return lifeTime > 0;
    }

    public void subtractLifeTime(float dt) {
        lifeTime -= dt;
        if (lifeTime < 0) {
            lifeTime = 0.0f;
        }
    }

    @Override
    public float getY() {
        return position.y;
    }

    public int getCellX(){
        return (int) (position.x / Map.CELL_WIDTH);
    }

    public int getCellY(){
        return (int) (position.y / Map.CELL_HEIGHT);
    }

    public void setMessage(String str, Vector2 position, Color color) {
        this.message.setLength(0);
        this.message.append(str);
        this.position.x = position.x + 25;
        this.position.y = position.y + 45;
        this.dst.set(position.x + 30, position.y + 80);
        this.color = color;
        this.lifeTime = 3.0f;
    }

    public void moveToUp(float dt) {
        if (position.dst(dst) > speed * dt) {
            tmp.set(dst).sub(position).nor().scl(speed); //вектор скорости
            position.set(position.x + tmp.x * dt, position.y + tmp.y * dt);
            tmp1.set(50.0f * MathUtils.sinDeg(MathUtils.random(-60, 60)), 50.0f * MathUtils.cosDeg(MathUtils.random(-60, 60)));
            position.mulAdd(tmp1, dt);
        } else {
            this.lifeTime = 0.0f;
        }
    }
}
