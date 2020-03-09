package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameCharacter {
    protected GameScreen gameScreen;

    protected TextureRegion texture;
    protected TextureRegion textureHp;  //Показатель здоровья

    protected Vector2 position;
    protected Vector2 dst; //Точка, к которой двигаемся
    protected Vector2 tmp;

    protected Circle area; //окружности под ногами

    protected float lifeTime;
    protected float speed;
    protected int hp, hpMax;

    public GameCharacter(GameScreen gameScreen, int hpMax, float speed) {
        this.gameScreen = gameScreen;
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.tmp = new Vector2(0.0f, 0.0f);
        this.dst = new Vector2(0.0f, 0.0f);
        this.position = new Vector2(0.0f, 0.0f);
        this.area = new Circle(0.0f, 0.0f, 15);
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.speed = speed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getArea() {
        return area;
    }

    public void update(float dt) {
        lifeTime += dt;

        tmp.set(dst).sub(position).nor().scl(speed); //вектор скорости
        if (position.dst(dst) > speed * dt){
            position.mulAdd(tmp, dt);
        }else {
            position.set(dst);
        }
        area.setPosition(position.x, position.y - 20);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            onDeath();
            return true;
        }
        return false;
    }

    public abstract void onDeath();

    public abstract void render(SpriteBatch batch);

    public void changePosition(float x, float y){
        position.set(x, y);
        area.setPosition(x, y - 20);
    }

    public void changePosition(Vector2 newPosition){
        changePosition(newPosition.x, newPosition.y);
    }
}
