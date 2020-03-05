package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Monster {
    private GameScreen gameScreen;
    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position; //Позиция монстра
    private Vector2 tmp;
    private float speed;
    private int hp; //здоровье монстра
    private int hpMax;
    private boolean healingMonster; //true - монстр на лечении (не преследует героя)

    public Monster(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.position = new Vector2(800, 300);
        this.tmp = new Vector2(0, 0);
        this.speed = 100.0f;
        this.hp = 200;
        this.hpMax = 200;
        this.healingMonster = false;
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
        if(!healingMonster) { //Если монстр не на лечении
            tmp.set(gameScreen.getHero().getPosition()).sub(position).nor().scl(speed); //вектор скорости
            position.mulAdd(tmp, dt);
        }else {
            if (hp++ >= hpMax) {
                healingMonster = false;
            }
        }
    }

    public void reincarnation(){
        position.set((float) Math.random() * 1230, (float) Math.random() * 730);
        healingMonster = true;
    }

    public boolean takeDamage(int amount){
        return (hp -= amount) <= 0;
    }

    public Vector2 getPosition() {
        return position;
    }
}
