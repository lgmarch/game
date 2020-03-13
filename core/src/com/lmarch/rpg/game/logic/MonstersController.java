package com.lmarch.rpg.game.logic;

public class MonstersController extends ObjectPool<Monster>{
    private GameController gameController;
    private float innerTimer;
    private float spawnPeriod;

    public MonstersController(GameController gameController, int initialCount) {
        this.gameController = gameController;
        this.spawnPeriod = 10.0f;
        for (int i = 0; i < initialCount; i++) {
            getActiveElement().setup();
        }
    }

    @Override
    protected Monster newObject() {
        return new Monster(gameController);
    }

    public void update(float dt){
        innerTimer += dt;

        if (innerTimer > spawnPeriod) {  //10 seconds
            innerTimer = 0.0f;
            getActiveElement().setup();
        }

        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).update(dt);
        }
        checkPool();
    }
}
