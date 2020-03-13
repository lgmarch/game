package com.lmarch.rpg.game.logic;

/**
 * Реализация топора
 */
public class AxeAction implements WeaponAction{
    private float attackRadius;  // дальность атаки
    private float attackTime; // период атаки
    private float damage;       // урон

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
        return 0;
    }

    public AxeAction() {
        this.attackRadius = 50;
        this.attackTime = 0.3f;
        this.damage = 5;
    }
}
