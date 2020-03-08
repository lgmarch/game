package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero extends GameCharacter{
    private TextureRegion texturePointer;
    private int coins;
    private StringBuilder strBuilder;

    private Vector2 angle;

    public Hero(GameScreen gameScreen){
        super(gameScreen, 10, 200.0f);
        this.texture = Assets.getInstance().getAtlas().findRegion("pig1");
        this.texturePointer = Assets.getInstance().getAtlas().findRegion("pointer");
        this.position = new Vector2(100, 100);
        this.dst.set(position);
        this.strBuilder = new StringBuilder();

        this.angle = new Vector2(0, 0); //Вращение героя
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(texturePointer, dst.x - 32, dst.y - 32,
                32, 32, 64, 64, 0.5f, 0.5f, lifeTime * 90.0f);

        batch.draw(texture, position.x - 32, position.y - 32,
                32, 32, 64,64, 1, 1, angle.angle());

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
            dst.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            gameScreen.getProjectilesController().setup(position.x, position.y, Gdx.input.getX(), 720 - Gdx.input.getY());
        }

        angle.set(tmp);
        //System.out.println(angle.angle());
    }

    @Override
    public void onDeath() {
        coins = 0;
        hp = hpMax;
    }

    public void addCoins(int amount){
        coins += amount;
    }
}
