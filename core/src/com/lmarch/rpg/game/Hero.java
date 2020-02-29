package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private float speed;
    private float param;

    public Hero(){
        this.texture = new Texture("pig1.png");
        this.position = new Vector2(200, 200);
        this.speed = 100.0f;
        this.param = 1.0f;
    }

    //Прорисовка
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - 32, position.y - 32,
                32, 32, 64,64,
                1, 1, param, 0, 0, 64, 64, false, false);

//        batch.draw(texture, position.x, position.y, texture.getWidth()/2, texture.getHeight()/2,
//                texture.getWidth(),texture.getHeight(), param, param, 0, 0, 0,
//                600, 500, false, false);
    }

    //логика движения персонажа - расчет
    public void update(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            position.x -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            position.x += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            position.y -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            position.y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)){
            param += dt * 90.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
            param -= dt * 90.0f;
        }
    }

    public void moveToMouse(Vector2 pointerPosition, float dt){
//        position.x = pointerPosition.x;
//        position.y = pointerPosition.y;

        if (position.x <= pointerPosition.x)
            position.x += speed * dt;
        if (position.x >= pointerPosition.x)
            position.x -= speed * dt;

        if (position.y <= pointerPosition.y)
            position.y += speed * dt;
        if (position.y >= pointerPosition.y)
            position.y -= speed * dt;
    }
}
