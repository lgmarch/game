package com.lmarch.rpg.game.logic;

import com.lmarch.rpg.game.logic.utils.ObjectPool;

public class MessageController extends ObjectPool<Message> {
    @Override
    protected Message newObject() {
        return new Message();
    }

    public void update(float dt){
        for(Message message : getActiveList()){
            message.subtractLifeTime(dt);
            message.moveToUp(dt);
        }
        checkPool();
    }
}
