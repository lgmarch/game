package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface WeaponAction {
    //void battle();  //На будущее. Реализация удара оружием

    float getAttackRadius();

    float getAttackTime();

    float getDamage();

    Weapons.TypeWeapon getTypeWeapon();

    TextureRegion getTextureWeapon();

    void setActive(boolean active);
}
