package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс для отображения на игровом поле оружия и хранения ссылки на WeaponAction (оружие)
 */
public class Treasure implements MapElement, Poolable {

    private WeaponAction weaponActions;
    private TextureRegion textureRegion;
    private int positionX;
    private int positionY;
    private float lifeTime; //Время сохранности клада, потом он исчезает

    public Treasure() {
        this.lifeTime = 50.0f;
    }

    //TODO Убрать прорисовку из Мар?
    @Override
    public void render(SpriteBatch batch, BitmapFont font) {

    }

    @Override
    public boolean isActive() {
        return lifeTime > 0;
    }

    public void setLifeTime(float time) {
        this.lifeTime = time;
    }

    public void subtractLifeTime(float dt) {
        lifeTime -= dt;
        if (lifeTime < 0) setLifeTime(0.0f); //Если оружие не подобрали, оно уходитв пул....
    }

    public WeaponAction getWeaponActions() {
        return weaponActions;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getCellX(){
        return positionX;
    }

    public int getCellY(){
        return (positionY);
    }

    public void setup(WeaponAction w, int x, int y) {
        this.weaponActions = w;
        this.textureRegion = w.getTextureWeapon();
        this.positionX = x;
        this.positionY = y;
        this.lifeTime = 50.0f;
    }
}
