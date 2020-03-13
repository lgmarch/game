package com.lmarch.rpg.game.logic;

/**
 * Реализация лука
 */
public class ArrowAction implements WeaponAction{
    private float attackRadius; //дальность атаки
    private float damage;       // урон
    private float attackTime; // скорость атаки

    @Override
    public void battle() {

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

    public int getTypeWeapon() {
        return 1;
    }

    public ArrowAction() {
        this.attackRadius = 150;
        this.attackTime = 0.2f;
        this.damage = 1;
    }
}
