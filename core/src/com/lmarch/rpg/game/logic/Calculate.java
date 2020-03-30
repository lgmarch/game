package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.math.MathUtils;

public class Calculate {
    //ЛовкостьГероя * УровеньГероя - УровеньМонстра * ЗащитаМонстра = Урон

    public static int getMeleeDamage(GameCharacter attacker, GameCharacter target) {
        int damage = MathUtils.random(attacker.getWeapon().getMinDamage(), attacker.getWeapon().getMaxDamage());

        int dmg = damage + attacker.getLevel();

        if (dmg < 1) {
            dmg = 1;
        }
        return dmg;
    }

    public static int getRangedDamage(GameCharacter attacker, GameCharacter target) {
        int damage = MathUtils.random(attacker.getWeapon().getMinDamage(), attacker.getWeapon().getMaxDamage());

        int dmg = damage + attacker.getLevel();

        if (dmg < 1) {
            dmg = 1;
        }
        return dmg;
    }

    public static int calculateDamage(GameCharacter attacker, GameCharacter target) {
        if(attacker.getWeapon().getType() == Weapon.Type.MELEE) {
            return getMeleeDamage(attacker, target);
        } else {
            return getRangedDamage(attacker, target);
        }
    }
}
