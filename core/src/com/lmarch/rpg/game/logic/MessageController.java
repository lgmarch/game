package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public void render(SpriteBatch batch, BitmapFont font) {
        for (int i = 0; i < activeList.size(); i++) {
            Message msg = activeList.get(i);
            font.setColor(msg.getColor());
            font.draw(batch, msg.getMessage(), msg.getPosition().x, msg.getPosition().y);
        }
        font.setColor(Color.WHITE);
    }
}
