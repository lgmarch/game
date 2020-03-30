package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.Poolable;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Monster extends GameCharacter implements Poolable {

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public Monster(GameController gameController){
        super(gameController, 80, 50.0f);
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("dwarf60")).split(60, 60);
        this.changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.dst.set(this.position);
        this.visionRadius = 160.0f;
        this.weapon = gc.getWeaponsController().getOneFromAnyPrototype();
        this.color = Color.MAGENTA;
    }

    public void setup(){
        do {
            changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        } while (!gc.getMap().isGroundPassable(position));

        this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.speed = MathUtils.random(60, 90);
        hp = hpMax;

        if (MathUtils.random(0,50) > 25) {
            treasure = gc.getTreasureController().getActiveElement().setMoney();
        } else {
            treasure = gc.getTreasureController().getActiveElement().setElixir();
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        treasure.setupFree(position);
        gc.getWeaponsController().setup(this.weapon, position.x, position.y);
        //gc.getTreasureController().getActiveElement().setup(position, this.treasure);
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

        for (Hero hero : gc.getHeroesController().getHeroes()) {
            if (state != State.RETREAT && this.position.dst(hero.getPosition()) < visionRadius) {
                state = State.ATTACK;
                target = hero;
                stateTimer = 10.0f;
            }
        }
        //Если здоровье меньше, чем..., то монстр убегает
        if (hp < hpMax * 0.2 && state != State.RETREAT) {
            state = State.RETREAT;
            stateTimer = 1.0f;
            dst.set(position.x + MathUtils.random(100, 200) * Math.signum(position.x - lastAttacker.position.x),
                    position.y + MathUtils.random(100, 200) * Math.signum(position.y - lastAttacker.position.y));
        }
    }
}
