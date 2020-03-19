package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.math.MathUtils;
import com.lmarch.rpg.game.logic.utils.ObjectPool;

public class WeaponsController extends ObjectPool<Weapon> {
    private GameController gc;

    @Override
    protected Weapon newObject() {
        return new Weapon();
    }

    public WeaponsController(GameController gc) {
        this.gc = gc;
    }

    public void setup(float x, float y) {
        Weapon w = getActiveElement();
        int maxDamage = MathUtils.random(3, 4);
        for (int i = 0; i < 10; i++) {
            if (MathUtils.random(100) < 50 - i * 5) {
                maxDamage++;
            }
        }
        Weapon.Type type = Weapon.Type.MELEE;
        String title;
        float range = 60.0f;
        title = "Melee";
        float attackSpeed = 0.4f;
        if (MathUtils.random(100) < 40) {
            type = Weapon.Type.RANGED;
            title = "Ranged";
            range = 160.0f;
            attackSpeed = 0.5f;
        }
        w.setup(type, "Weapon", MathUtils.random(1, 4), maxDamage, 0.4f, range);
        w.setPosition(x, y);
    }

    public void update(float dt) {
        checkPool();
    }
}
