package com.lmarch.rpg.game.logic;

/**
 * Реализация меча
 */
public class SwordAction extends Weapons{

    @Override
    public void battle() {

    }

    public SwordAction() {
        this.attackRadius = 25;
        this.attackTime = 0.3f;
        this.damage = 4;
        this.typeWeapon = TypeWeapon.SWORD;
    }
}
