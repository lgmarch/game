package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lmarch.rpg.game.logic.utils.ObjectPool;

import java.util.ArrayList;
import java.util.List;

public class HeroesController extends ObjectPool<Hero> {
    private GameController gc;
    private java.util.List<Hero> heroes;
    private java.util.List<Hero> selected;

    @Override
    protected Hero newObject() {
        return new Hero(gc);
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public List<Hero> getSelected() {
        return selected;
    }

    public HeroesController(GameController gc) {
        this.gc = gc;
        this.heroes = new ArrayList<>();
        this.selected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            heroes.add(new Hero(gc));
        }
        selected.add(heroes.get(0));
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        for (int i = 0; i < heroes.size(); i++) {
            heroes.get(i).renderGUI(batch, font, i*150);
        }
    }

    public void update(float dt) {
        for (Hero hero : heroes) {
            hero.update(dt);
        }
        checkPool();
    }
}
