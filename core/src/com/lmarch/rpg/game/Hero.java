package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private TextureRegion texture;
    private TextureRegion texturePointer;
    private TextureRegion textureHp;
    private Vector2 position; //Позиция героя
    private Vector2 dst; //Полиция поинтера
    private Vector2 tmp;
    private StringBuilder strBuilder;
    private float speed;
    private float rotation; //lifeTime
    private int hp; //здоровье героя
    private int hpMax;

    private float param;

    public Hero(TextureAtlas atlas){
        this.texture = atlas.findRegion("pig1");
        this.texturePointer = atlas.findRegion("pointer");
        this.textureHp = atlas.findRegion("hp");
        this.position = new Vector2(100, 100);
        this.dst = new Vector2(position);
        this.tmp = new Vector2(0, 0);
        this.strBuilder = new StringBuilder();
        this.speed = 200.0f;
        this.hp = 10;
        this.hpMax = 10;
        this.param = 10.0f;
    }

    //Прорисовка
    public void render(SpriteBatch batch){
        batch.draw(texturePointer, dst.x - 32, dst.y - 32,
                32, 32, 64, 64, 0.5f, 0.5f, rotation * 90.0f);

        batch.draw(texture, position.x - 32, position.y - 32,
                32, 32, 64,64, 1, 1, param);

        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        strBuilder.setLength(0); //Очистка
        strBuilder.append("Class: ").append("Pig").append("\n");
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        font.draw(batch, strBuilder, 10, 710);
    }

    //логика движения персонажа - расчет
    public void update(float dt){
        rotation +=dt;

        if (Gdx.input.justTouched()){
            dst.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }

        tmp.set(dst).sub(position).nor().scl(speed); //вектор скорости
        if (position.dst(dst) > speed * dt){
            position.mulAdd(tmp, dt);
        }else {
            position.set(dst);
        }
        //position.mulAdd(tmp, dt);

        //Данную строку использовать нельзя (метод cpy()...)
        //position.mulAdd(dst.cpy().sub(position).nor().scl(speed), dt);
    }
}
