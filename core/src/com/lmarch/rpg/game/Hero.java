package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Texture texturePointer;
    private Vector2 position; //Позиция героя
    private Vector2 dst; //Полиция поинтера

    private float speed;
    private float lifeTime;
    private float param;

    public Hero(){
        this.texture = new Texture("pig1.png");
        texturePointer = new Texture("pointer.png");
        this.position = new Vector2(100, 100);
        this.dst = new Vector2(position);
        this.speed = 100.0f;
        this.param = 1.0f;
    }

    //Прорисовка
    public void render(SpriteBatch batch){
        batch.draw(texturePointer, dst.x - 32, dst.y - 32,
                32, 32, 64,64,
                1, 1, lifeTime * 90.0f, 0, 0, 64, 64, false, false);

        batch.draw(texture, position.x - 32, position.y - 32,
                32, 32, 64,64,
                1, 1, param, 0, 0, 64, 64, false, false);
    }

    //логика движения персонажа - расчет
    public void update(float dt){
        lifeTime +=dt;
        if (Gdx.input.justTouched()){
            dst.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }

        if (position.x > dst.x){
            position.x -= speed * dt;
        }
        if (position.x < dst.x){
            position.x += speed * dt;
        }
        if (position.y > dst.y){
            position.y -= speed * dt;
        }
        if (position.y < dst.y){
            position.y += speed * dt;
        }
    }
}
