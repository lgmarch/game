package com.lmarch.rpg.game.logic;

public interface WeaponAction {
    void battle();

    float getAttackRadius();

    float getAttackTime();

    float getDamage();

    int getTypeWeapon();
}
