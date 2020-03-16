package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;
import com.badlogic.gdx.math.MathUtils;

public class Weapon implements MapElement, Poolable {
    public enum Type {
        MELEE, RANGED
    }

    private TextureRegion texture;
    private Type type;
    private String title;
    private Vector2 position;
    private int minDamage;
    private int maxDamage;
    private float speed;
    private float range;
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

//    @Override
//    public void consume(GameCharacter gameCharacter) {
//        gameCharacter.setWeapon(this);
//        active = false;
//    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        batch.draw(texture, position.x - 32, position.y - 32);
    }

    @Override
    public int getCellX() {
        return (int) (position.x / 80);
    }

    @Override
    public int getCellY() {
        return (int) (position.y / 80);
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public int generateDamage() {
        return MathUtils.random(minDamage, maxDamage);
    }

    public float getSpeed() {
        return speed;
    }

    public float getRange() {
        return range;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Weapon() {
        this.position = new Vector2(0, 0);
    }

    public Weapon(Type type, String title, int minDamage, int maxDamage, float speed, float range) {
        this.type = type;
        this.title = title;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.speed = speed;
        this.range = range;
    }

    public void setup(Type type, String title, int minDamage, int maxDamage, float speed, float range) {
        this.type = type;
        if (type == Type.MELEE) {
            texture = Assets.getInstance().getAtlas().findRegion("weaponMelee");
        } else {
            texture = Assets.getInstance().getAtlas().findRegion("weaponRanged");
        }
        this.title = title;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.speed = speed;
        this.range = range;
        this.active = true;
    }

    public static Weapon createSimpleRangedWeapon() {
        return new Weapon(
                Type.RANGED,
                "Bow",
                MathUtils.random(1, 2),
                MathUtils.random(3, 5),
                0.5f,
                150.0f
        );
    }

    public static Weapon createSimpleMeleeWeapon() {
        return new Weapon(
                Type.MELEE,
                "Sword",
                MathUtils.random(1, 2),
                MathUtils.random(3, 4),
                0.4f,
                60.0f
        );
    }
}
