package com.lmarch.rpg.game.logic;

/**
 * Реализация меча
 */
public class SwordAction implements WeaponAction{
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

    public SwordAction() {
        this.attackRadius = 25;
        this.attackTime = 0.3f;
        this.damage = 4;
    }
}
