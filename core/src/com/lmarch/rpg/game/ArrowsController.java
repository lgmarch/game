package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ArrowsController {
    private Arrow[] arrows;
    private int numArrowsInQuiver;

    public ArrowsController(TextureAtlas atlas) {
        numArrowsInQuiver = 10;
        arrows = new Arrow[numArrowsInQuiver];

        for (int i = 0; i < arrows.length; i++) {
            arrows[i] = new Arrow(atlas);
        }
    }

    public void setup(float x, float y, float targetX, float targetY){
        for (Arrow o : arrows) {
            if (!o.isActive()) {
                o.setup(x, y, targetX, targetY);
                return;
            }
        }
    }

    public void update(float dt){
        for (Arrow o : arrows) {
            if (o.isActive()){
                o.update(dt);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (Arrow o : arrows) {
            if(o.isActive()){
                o.render(batch);
            }
        }
    }

    public Arrow[] getArrows() {
        return arrows;
    }
}
