package com.lmarch.rpg.game.logic;

import com.lmarch.rpg.game.screens.utils.Assets;

/**
 * Реализация лука
 */
public class ArrowAction extends Weapons{

//    @Override
//    public void battle() {
//
//    }

    public ArrowAction() {
        //Радиус выбран не случайно. Он равен радиусу видимости у Монстра. Чтобы Герой с расстояния монстров не валил
        this.attackRadius = 180;
        this.attackTime = 0.2f;
        this.damage = 1;
        this.typeWeapon = TypeWeapon.ARROW;
        this.textureWeapon = Assets.getInstance().getAtlas().findRegion("crossbow");
    }
}
