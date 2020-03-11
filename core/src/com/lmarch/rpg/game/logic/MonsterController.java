package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
            System.out.println("*** All Monster ***" + getActiveList().size() + " " + getFreeList().size());
            frequencyAdventMonster = 0.0f;
            getActiveElement().setup();
            System.out.println(" After add Monster " + getActiveList().size() + " " + getFreeList().size());
        }

        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).update(dt);
        }
        checkPool();
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).render(batch, null);
        }
    }
}
