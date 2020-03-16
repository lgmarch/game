package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lmarch.rpg.game.screens.utils.Assets;

public class Hero extends GameCharacter{
    private TextureRegion texturePointer;
    private int coins;
    private StringBuilder strBuilder;

    public Hero(GameController gc){
        super(gc, 10, 200.0f);
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("pig1")).split(60, 60);
        this.texturePointer = Assets.getInstance().getAtlas().findRegion("pointer");
        this.changePosition(100.0f, 100.0f);
        this.dst.set(position);
        this.strBuilder = new StringBuilder();
        this.weapon = Weapon.createSimpleMeleeWeapon();
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font){
        batch.draw(texturePointer, dst.x - 32, dst.y - 32,
                32, 32, 64, 64, 0.5f, 0.5f, lifeTime * 90.0f);

        batch.draw(texture[0][0], position.x - 32, position.y - 32,
                32, 32, 64,64, 1, 1, 1);

        batch.draw(textureHp, position.x - 35, position.y + 35, 60 * ((float) hp / hpMax), 8);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        strBuilder.setLength(0); //Очистка
        strBuilder.append("Class: ").append("Pig").append("\n");
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        strBuilder.append("Coins: ").append(coins).append("\n");
        font.draw(batch, strBuilder, 10, 710);
    }

    @Override
    public void update(float dt){
        super.update(dt);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            for (int i = 0; i < gc.getMonstersController().getActiveList().size(); i++) {
                Monster m = gc.getMonstersController().getActiveList().get(i);
                if (m.getPosition().dst(Gdx.input.getX(), 720.0f - Gdx.input.getY()) < 30.0f) {
                    state = State.ATTACK;
                    target = m;
                    return;
                }
            }
            dst.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            state = State.MOVE;
            target = null;
        }
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
//            gc.getProjectilesController().setup(position.x, position.y, Gdx.input.getX(), 720 - Gdx.input.getY());
//        }
        //System.out.println(angle.angle());
    }

    @Override
    public void onDeath() {
        super.onDeath();
        coins = 0;
        hp = hpMax;
    }

    public void addCoins(int amount){
        coins += amount;
    }
}
