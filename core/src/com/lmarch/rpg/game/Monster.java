package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster {
    private GameScreen gameScreen;
    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position; //Позиция героя
    private Vector2 dst; //Позиция поинтера
    private Vector2 tmp;
    private float lifeTime; //lifeTime
    private float attackTime;
    private float speed;
    private int hp; //здоровье героя
    private int hpMax;

    public Monster(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.position = new Vector2(800, 300);
        this.dst = new Vector2(position);
        this.tmp = new Vector2(0, 0);
        this.speed = 100.0f;
        this.hp = 30;
        this.hpMax = 30;

    }

    public void render(SpriteBatch batch){ //Прорисовка
        batch.setColor(1, 0 , 0, 1);
        batch.draw(texture, position.x - 32, position.y - 32,
                32, 32, 64,64, 1, 1, 0);
        batch.setColor(1, 1 , 1, 1);
        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
    }

    //логика движения персонажа - расчет
    public void update(float dt){
        lifeTime +=dt;
        if (this.position.dst(gameScreen.getHero().getPosition()) < 40){
            attackTime += dt;
            if (attackTime > 0.3f) {
                attackTime = 0.0f;
                gameScreen.getHero().takeDamage(1);
            }
        }

        tmp.set(gameScreen.getHero().getPosition()).sub(position).nor().scl(speed); //вектор скорости
        position.mulAdd(tmp, dt);

        //Данную строку использовать нельзя (метод cpy()...)
        //position.mulAdd(dst.cpy().sub(position).nor().scl(speed), dt);
    }

    public boolean takeDamage(int amount){
        return (hp -= amount) <= 0;
    }

    public void recreate(){
        this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.hp = this.hpMax;
    }

    public Vector2 getPosition() {
        return position;
    }
}
