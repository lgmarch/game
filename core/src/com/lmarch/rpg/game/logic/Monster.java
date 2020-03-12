package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Monster extends GameCharacter implements Poolable {
    private float attackTime;
    private boolean active;
    protected Vector2 trackedPositionHero; //Отслеживаемая позиция Героя

    public Monster(GameController gameController){
        super(gameController, 20, 50.0f);
        this.trackedPositionHero = new Vector2(0.0f, 0.0f); //Куда бежать за героем
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.changePosition(0.0f, 0.0f);
        this.active = true;
    }

    public void setup(){
        this.active = true;
    }

    @Override
    public void onDeath() {
        this.active = false;
        this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.hp = this.hpMax;
        this.speed = MathUtils.random(40, 100);
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font){ //Прорисовка
        batch.setColor(1, 0 , 0, 1);
        batch.draw(texture, position.x - 30, position.y - 30,
                30, 30, 60,60, 1, 1, 0);
        batch.setColor(1, 1 , 1, 1);
        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
    }

    //логика движения персонажа - расчет
    public void update(float dt){
        super.update(dt);

        if (this.position.dst(gc.getHero().getPosition()) < 40){
            attackTime += dt;
            if (attackTime > 0.3f) {
                attackTime = 0.0f;
                gc.getHero().takeDamage(1);
            }
        }

        if (gc.getHero().getArea().overlaps(this.getAttackCircle())) { //Если Герой попал в круг атаки
            this.dst.set(gc.getHero().getPosition()); //Бежим к герою
            trackedPositionHero.set(gc.getHero().getPosition()); //Запоминаем позицию Героя
            return;
        }
        if (this.position.epsilonEquals(this.trackedPositionHero, 0.01f)) { //Добежали до точки, где был Герой
            this.dst.set(MathUtils.random(0, 1280), MathUtils.random(0, 720)); //бежим в случайную точку
            trackedPositionHero.set(this.dst); //Запоминаем эту позицию
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
