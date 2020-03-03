package com.lmarch.rpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ProjectileController {
    private Projectile[] projectiles;

    public ProjectileController(TextureAtlas atlas) {
        projectiles = new Projectile[3];

        for (int i = 0; i < projectiles.length; i++) {
            projectiles[i] = new Projectile(atlas);
        }
    }

    public void setup(float x, float y, float targetX, float targetY){
        for (Projectile o : projectiles) {
            if (!o.isActive()) {
                o.setup(x, y, targetX, targetY);
                return;
            }
        }
    }

    public void update(float dt){
        for (Projectile o : projectiles) {
            if (o.isActive()){
                o.update(dt);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (Projectile o : projectiles) {
            if(o.isActive()){
                o.render(batch);
            }
        }
    }
}
