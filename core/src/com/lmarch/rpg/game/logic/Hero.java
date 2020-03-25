package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Hero extends GameCharacter{
    private StringBuilder strBuilder;

    public Hero(GameController gc){
        super(gc, 100, 120.0f);
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("pig1")).split(60, 60);
        this.changePosition(100.0f, 100.0f);
        this.dst.set(position);
        this.strBuilder = new StringBuilder();
        this.weapon = gc.getWeaponsController().getOneFromAnyPrototype();
        this.color = Color.RED;
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        strBuilder.setLength(0); //Очистка
        strBuilder.append("Class: ").append("Pig").append("\n");
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        strBuilder.append("Coins: ").append(coins).append("\n");
        strBuilder.append("Weapon: ").append(weapon.getTitle()).append(" [").append(weapon.getMinDamage()).
                append("-").append(weapon.getMaxDamage()).append("]").append("\n");
        font.draw(batch, strBuilder, 10, 710);
    }

    @Override
    public void update(float dt){
        super.update(dt);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            for (int i = 0; i < gc.getMonstersController().getActiveList().size(); i++) {
                Monster m = gc.getMonstersController().getActiveList().get(i);
                if (m.getPosition().dst(gc.getMouse()) < 30.0f) {
                    state = State.ATTACK;
                    target = m;
                    return;
                }
            }
            dst.set(gc.getMouse());
            state = State.MOVE;
            target = null;
        }
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
//            gc.getProjectilesController().setup(position.x, position.y, Gdx.input.getX(), 720 - Gdx.input.getY());
//        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        coins = 0;
        hp = hpMax;
    }
}
