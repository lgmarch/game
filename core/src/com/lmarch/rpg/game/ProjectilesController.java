package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ProjectilesController extends ObjectPool<Projectile>{
    private TextureRegion projectileTextureRegion;

    @Override
    protected Projectile newObject() {
        return new Projectile();
    }

    public ProjectilesController(TextureAtlas atlas) {
        this.projectileTextureRegion = atlas.findRegion("arrow");
    }

    //Внешняя программа не должна копаться в контроллере!
    public void setup(float x, float y, float targetX, float targetY){
        getActiveElement().setup(projectileTextureRegion, x, y, targetX, targetY);
    }
    
    public void render(SpriteBatch batch){
        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).render(batch);
        }
    }

    public void update(float dt){
        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).update(dt);
        }
        checkPool(); //проверка освобождения объектов
    }
}
