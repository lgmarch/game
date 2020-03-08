package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GameCharacter {
    protected GameScreen gameScreen;

    protected TextureRegion texture;
    protected TextureRegion textureHp;  //Показатель здоровья

    protected Vector2 position;
    protected Vector2 tmp;

    protected float lifeTime;
    protected float speed;
    protected int hp, hpMax;

    public GameCharacter(GameScreen gameScreen, int hpMax, float speed) {
        this.gameScreen = gameScreen;
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.tmp = new Vector2(0.0f, 0.0f);
        this.speed = speed;
        this.hpMax = hpMax;
        this.hp = this.hpMax;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float dt) {
        lifeTime += dt;
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
}
