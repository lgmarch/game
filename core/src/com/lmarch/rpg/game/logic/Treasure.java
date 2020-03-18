package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.Consumable;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.logic.utils.Poolable;
import com.lmarch.rpg.game.screens.utils.Assets;

/**
 * Класс для отображения на игровом поле выпадающих из Монстров сокровищ и хранения ссылки на WeaponAction (оружие)
 */
public class Treasure implements MapElement, Poolable, Consumable {

    public enum Type {
        ELIXIR, MONEY
    }

    private Type type;
    private TextureRegion texture;
    private Vector2 position;
    //private float lifeTime; //Время сохранности клада, потом он исчезает
    private int quantity;
    private boolean active;
    private boolean free; //Оружие свободно

    public Treasure() {
        this.position = new Vector2(0, 0);
        this.free = false;
    }
//
//    public Treasure(Treasure.Type type, TextureRegion texture, int quantity) {
//        this.type = type;
//        this.texture = texture;
//        //this.position = position;
//        this.quantity = quantity;
//        this.active = true;
//        this.free = false;
//        //this.lifeTime = 50.0f;
//    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        batch.draw(texture, position.x - 10, position.y - 10);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void consume(GameCharacter gameCharacter) {
        //gameCharacter.setTreasure(this);
    }

//    public void setLifeTime(float time) {
//        this.lifeTime = time;
//    }
//
//    public void subtractLifeTime(float dt) {
//        lifeTime -= dt;
//        if (lifeTime < 0) setLifeTime(0.0f); //Если не подобрали, оно уходитв пул....
//    }

    public Type getType() {
        return type;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCellX(){
        return (int) (position.x / 80);
    }

    public int getCellY(){
        return (int) (position.y / 80);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public void setupFree(Vector2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.free = true;
        this.active = true;
    }

    public Treasure setMoney() {
        this.type = Type.MONEY;
        this.texture = Assets.getInstance().getAtlas().findRegion("coin");
        this.quantity = 1; //MathUtils.random(1,20);
        this.active = true;
        this.free = false;
        return this;
    }

    public Treasure setElixir() {
        this.type = Type.ELIXIR;
        this.texture = Assets.getInstance().getAtlas().findRegion("potionBlue");
        this.quantity = 1; //MathUtils.random(1,20);
        this.active = true;
        this.free = false;
        return this;
    }
}
