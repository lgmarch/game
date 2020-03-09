package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster extends GameCharacter{
    private float attackTime;

    public Monster(GameScreen gameScreen){
        super(gameScreen, 20, 100.0f);
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.changePosition(800.0f, 300.0f);
    }

    @Override
    public void onDeath() {
        this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.hp = this.hpMax;
    }

    public void render(SpriteBatch batch){ //Прорисовка
        batch.setColor(1, 0 , 0, 1);
        batch.draw(texture, position.x - 30, position.y - 30,
                30, 30, 60,60, 1, 1, 0);
        batch.setColor(1, 1 , 1, 1);
        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
    }

    //логика движения персонажа - расчет
    public void update(float dt){
        super.update(dt);

        dst.set(gameScreen.getHero().getPosition());
        if (this.position.dst(gameScreen.getHero().getPosition()) < 40){
            attackTime += dt;
            if (attackTime > 0.3f) {
                attackTime = 0.0f;
                gameScreen.getHero().takeDamage(1);
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}
