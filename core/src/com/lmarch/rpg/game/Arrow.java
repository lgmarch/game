package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Arrow {
    private TextureRegion textureRegion;
    private Vector2 position;
    private Vector2 velocity; //скорость
    private boolean active;
    private Circle arrowArea; //отслеживание попаданий

    public Arrow(TextureAtlas atlas) {
        this.textureRegion = atlas.findRegion("arrow");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        this.arrowArea = new Circle(position.x+30, position.y+30, 20);
    }

    public void setup(float x, float y, float targetX, float targetY){
        position.set(x, y);
        velocity.set(targetX, targetY).sub(x, y).nor().scl(500.0f);
        arrowArea.setPosition(position.x+30, position.y+30);
        active = true;
    }

    public void deactivate(){
        active = false;
    }

    //Отрисовка стрелы
    public void render(SpriteBatch batch){
        batch.draw(textureRegion, position.x - 30, position.y - 30,
                30, 30, 60, 60, 1, 1, velocity.angle());
    }

    public void update(float dt){
        position.mulAdd(velocity, dt);
        if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720){
            deactivate();
        }
        arrowArea.setPosition(position.x+30, position.y+30);
    }

    public boolean isActive() {
        return active;
    }

    public Circle getArrowArea() {
        return arrowArea;
    }
}
