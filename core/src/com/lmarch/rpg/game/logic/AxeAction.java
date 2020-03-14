package com.lmarch.rpg.game.logic;

import com.lmarch.rpg.game.screens.utils.Assets;

/**
 * Реализация топора
 */
public class AxeAction extends Weapons{

//    @Override
//    public void battle() {
//
//    }

    public TypeWeapon getTypeWeapon() {
        return typeWeapon;
    }

    public AxeAction() {
        this.attackRadius = 50;
        this.attackTime = 0.3f;
        this.damage = 5;
        this.typeWeapon = TypeWeapon.AXE;
        this.textureWeapon = Assets.getInstance().getAtlas().findRegion("axe");
    }
}
