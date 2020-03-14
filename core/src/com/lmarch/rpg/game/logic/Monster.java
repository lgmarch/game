package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Monster extends GameCharacter implements Poolable {

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public Monster(GameController gameController, WeaponsController wc){
        super(gameController, wc, 20, 50.0f);
        //weaponAction = new AxeAction();  //Изначально у монстра только топор
        weaponAction = wc.getActiveElement();
        initWeapon(weaponAction);
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.dst.set(this.position);
        this.visionRadius = 150.0f;
    }

    public void setup(){
        do {
            changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        } while (!gc.getMap().isGroundPassable(position));

        this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.speed = MathUtils.random(80, 120);
        hpMax = 20;
        hp = hpMax;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        //Монстр после гибели оставил оружие
        gc.getTreasureController().getActiveElement().setup(this.weaponAction, (int)this.position.x, (int)this.position.y);
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font){ //Прорисовка
        batch.setColor(1, 0 , 0, 1);
        batch.draw(texture, position.x - 30, position.y - 30,
                30, 30, 60,60, 1, 1, 0);
        batch.setColor(1, 1 , 1, 1);
        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
        batch.draw(textureWeapon, position.x - 35, position.y + 15, 30, 30);
    }

    public void update(float dt){
        super.update(dt);

        stateTimer -= dt;
        if (stateTimer < 0.0f) {
            if (state == State.ATTACK) {
                target = null;
            }
            state = State.values()[MathUtils.random(0, 1)];
            if (state == State.MOVE) {
                dst.set(MathUtils.random(1280), MathUtils.random(720));
            }
            stateTimer = MathUtils.random(2.0f, 5.0f);
        }

        if (state != State.RETREAT && this.position.dst(gc.getHero().getPosition()) < visionRadius) {
            state = State.ATTACK;
            target = gc.getHero();
            stateTimer = 10.0f;
        }
        //Если здоровье меньше, чем..., то монстр убегает
        if (hp < hpMax * 0.2 && state != State.RETREAT) {
            state = State.RETREAT;
            stateTimer = 1.0f;
            dst.set(position.x + MathUtils.random(100, 200) * Math.signum(position.x - lastAttacker.position.x),
                    position.y + MathUtils.random(100, 200) * Math.signum(position.y - lastAttacker.position.y));
        }

        //Преследование героя
//        if (this.position.dst(gc.getHero().getPosition()) < visionRadius) {
//            dst.set(gc.getHero().getPosition());
//        }
//        if (position.dst(dst) < 2.0f) {
//            dst.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
//        }
    }
}
