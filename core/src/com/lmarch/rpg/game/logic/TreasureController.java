package com.lmarch.rpg.game.logic;

import com.lmarch.rpg.game.logic.utils.ObjectPool;

public class TreasureController extends ObjectPool<Treasure> {
    @Override
    protected Treasure newObject() {
        return new Treasure();
    }

    public void update(float dt){
        for(Treasure treasure : getActiveList()){
            if (!treasure.isFree()) { //Если что-то вывалилось из монстра...
                treasure.subtractLifeTime(dt);
            }
        }
        checkPool();
    }
}
