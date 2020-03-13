package com.lmarch.rpg.game.logic;

public abstract class Weapons implements WeaponAction{
    public enum TypeWeapon {
        ARROW, AXE, SWORD
    }

    protected TypeWeapon typeWeapon;
    protected float attackRadius; //дальность атаки
    protected float damage;       // урон
    protected float attackTime; // скорость атаки

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
}
