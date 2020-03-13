package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lmarch.rpg.game.screens.utils.Assets;

public class ProjectilesController extends ObjectPool<Projectile>{
    private TextureRegion projectileTextureRegion;

    @Override
    protected Projectile newObject() {
        return new Projectile();
    }

    public ProjectilesController() {
        this.projectileTextureRegion = Assets.getInstance().getAtlas().findRegion("arrow");
    }

    //Внешняя программа не должна копаться в контроллере!
    public void setup(float x, float y, float targetX, float targetY){
        getActiveElement().setup(projectileTextureRegion, x, y, targetX, targetY);
    }

    public void update(float dt){
        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).update(dt);
        }
        checkPool(); //проверка освобождения объектов
    }
}
