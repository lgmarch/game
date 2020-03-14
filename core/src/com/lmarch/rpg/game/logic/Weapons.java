package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Weapons implements WeaponAction, Poolable {

    public enum TypeWeapon {
        ARROW, AXE, SWORD
    }

    public Weapons() {
        //Случайно выбрали тип оружия
        this.typeWeapon =TypeWeapon.values()[MathUtils.random(0, 2)];
        this.active = true;
    }

    protected TypeWeapon typeWeapon;
    protected float attackRadius; //дальность атаки
    protected float damage;       // урон
    protected float attackTime; // скорость атаки
    protected boolean active;
    protected TextureRegion textureWeapon;

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getAttackRadius() {
        return attackRadius;
    }

    public float getAttackTime() {
        return attackTime;
    }

    public float getDamage() {
        return damage;
    }

    public TypeWeapon getTypeWeapon() {
        return typeWeapon;
    }

    public TextureRegion getTextureWeapon() {
        return textureWeapon;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
