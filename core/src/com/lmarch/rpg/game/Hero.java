package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private GameScreen gameScreen;
    private TextureRegion texture;
    private TextureRegion texturePointer;
    private TextureRegion textureHp;
    private Vector2 position; //Позиция героя
    private Vector2 dst; //Позиция поинтера
    private Vector2 tmp;
    private StringBuilder strBuilder;
    private float speed;
    private float rotation; //lifeTime
    private int hp; //здоровье героя
    private int hpMax;
    private int coins;

    private Vector2 angle;

    public Hero(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.texture = Assets.getInstance().getAtlas().findRegion("pig1");
        this.texturePointer = Assets.getInstance().getAtlas().findRegion("pointer");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.position = new Vector2(100, 100);
        this.dst = new Vector2(position);
        this.tmp = new Vector2(0, 0);
        this.strBuilder = new StringBuilder();
        this.speed = 200.0f;
        this.hp = 10;
        this.hpMax = 10;

        this.angle = new Vector2(0, 0); //Вращение героя
    }

    //Прорисовка
    public void render(SpriteBatch batch){
        batch.draw(texturePointer, dst.x - 32, dst.y - 32,
                32, 32, 64, 64, 0.5f, 0.5f, rotation * 90.0f);

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

    //логика движения персонажа - расчет
    public void update(float dt){
        rotation +=dt;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            dst.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            gameScreen.getProjectilesController().setup(position.x, position.y, Gdx.input.getX(), 720 - Gdx.input.getY());
        }
        tmp.set(dst).sub(position).nor().scl(speed); //вектор скорости

        angle.set(tmp);
        System.out.println(angle.angle());

        if (position.dst(dst) > speed * dt){
            position.mulAdd(tmp, dt);
        }else {
            position.set(dst);
        }

        //Данную строку использовать нельзя (метод cpy()...)
        //position.mulAdd(dst.cpy().sub(position).nor().scl(speed), dt);
    }

    public void addCoins(int amount){
        coins += amount;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0){
            hp = hpMax;
            coins = 0;
        }
    }
}
