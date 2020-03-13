package com.lmarch.rpg.game.logic;

public class MonsterController extends ObjectPool<Monster>{
    private GameController gameController;
    private float frequencyAdventMonster;

    public MonsterController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    protected Monster newObject() {
        return new Monster(gameController);
    }

    public void update(float dt){
        frequencyAdventMonster += dt;

        if (frequencyAdventMonster > 10.0f) {
            frequencyAdventMonster = 0.0f;
            getActiveElement().setup();
        }

        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).update(dt);
        }
        checkPool();
    }
}
