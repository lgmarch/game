package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.Consumable;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.logic.utils.Poolable;
import com.lmarch.rpg.game.screens.utils.Assets;

/**
 * Класс для отображения на игровом поле выпадающих из Монстров сокровищ
 */
public class Treasure implements MapElement, Poolable, Consumable {

    public enum Type {
        ELIXIR, MONEY
    }

    private Type type;
    private TextureRegion texture;
    private Vector2 position;
    private float lifeTime;
    private int quantity;
    private boolean free; //Оружие свободно

    public Treasure() {
        this.position = new Vector2(0, 0);
        this.free = false;
        this.lifeTime = 30.0f;
    }


    @Override
    public void consume(GameController gc, GameCharacter player) {
        switch (type) {
            case ELIXIR:
                player.addHp(this.quantity);
                break;
            case MONEY:
                player.addCoins(this.quantity);
                break;
        }
        //gc.getMessageController().getActiveElement().setMessage(getAddTreasureString(), player.position, Color.GOLD);
        lifeTime = 5.0f;
        free = false;
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        batch.draw(texture, position.x - 10, position.y - 10);
    }

    @Override
    public boolean isActive() {
        return lifeTime > 0;
    }

    public void subtractLifeTime(float dt) {
        this.lifeTime -= dt;
        if (this.lifeTime < 0) {
            this.lifeTime = 0.0f;
        }
    }

    public Type getType() {
        return type;
    }

//    public TextureRegion getTexture() {
//        return texture;
//    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getY() {
        return position.y;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCellX(){
        return (int) (position.x / Map.CELL_WIDTH);
    }

    public int getCellY(){
        return (int) (position.y / Map.CELL_HEIGHT);
    }

    public String getTreasureInfo() {
        return "Add: " + "\n" + getType() + " " + quantity;
    }

    public boolean isFree() {
        return free;
    }

    public void setupFree(Vector2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.free = true;
    }

    public Treasure setMoney() {
        this.type = Type.MONEY;
        this.texture = Assets.getInstance().getAtlas().findRegion("coin");
        this.quantity = MathUtils.random(1,10);
        this.lifeTime = 30.0f;
        this.free = false;
        return this;
    }

    public Treasure setElixir() {
        this.type = Type.ELIXIR;
        this.texture = Assets.getInstance().getAtlas().findRegion("potionBlue");
        this.quantity = MathUtils.random(1,15);
        this.lifeTime = 30.0f;
        this.free = false;
        return this;
    }
}
