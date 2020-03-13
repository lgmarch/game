package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Monster extends GameCharacter implements Poolable {
    private float attackTime;

    public Monster(GameController gameController){
        super(gameController, 20, 50.0f);
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.dst.set(this.position);
        this.visionRadius = 100.0f;
    }

    public void setup(){
        do {
            changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        } while (!gc.getMap().isGroundPassable(position));

        this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.speed = MathUtils.random(40, 100);
    }

    @Override
    public void onDeath() {
        this.hp = hpMax;
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font){ //Прорисовка
        batch.setColor(1, 0 , 0, 1);
        batch.draw(texture, position.x - 30, position.y - 30,
                30, 30, 60,60, 1, 1, 0);
        batch.setColor(1, 1 , 1, 1);
        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
    }

    public void update(float dt){
        super.update(dt);

        //Преследование героя
        if (this.position.dst(gc.getHero().getPosition()) < visionRadius) {
            dst.set(gc.getHero().getPosition());
        }
        if (position.dst(dst) < 2.0f) {
            dst.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }

        //Нанесение урона герою
        if (this.position.dst(gc.getHero().getPosition()) < 40){
            attackTime += dt;
            if (attackTime > 0.3f) {
                attackTime = 0.0f;
                gc.getHero().takeDamage(1);
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }
}
