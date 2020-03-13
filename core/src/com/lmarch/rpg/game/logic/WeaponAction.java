package com.lmarch.rpg.game.logic;

public interface WeaponAction {
    void battle();  //На будущее. Реализация удара оружием

    float getAttackRadius();

    float getAttackTime();

    float getDamage();

    Weapons.TypeWeapon getTypeWeapon();
}
