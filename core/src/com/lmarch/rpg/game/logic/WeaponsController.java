package com.lmarch.rpg.game.logic;

public class WeaponsController extends ObjectPool<Weapons>{

    @Override
    protected Weapons newObject() {
        return new Weapons();
    }

    public void update(float dt){
        checkPool();
    }
}
