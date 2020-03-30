package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.lmarch.rpg.game.logic.utils.ObjectPool;
import com.lmarch.rpg.game.screens.utils.Assets;

import java.util.ArrayList;
import java.util.List;

public class HeroesController extends ObjectPool<Hero> {
    private GameController gc;
    private TextureRegion[] textures;
    private List<Hero> heroes;
    private List<Hero> selected;
    private Hero heroesCommander;

    @Override
    protected Hero newObject() {
        return new Hero(gc, textures[MathUtils.random(0, 3)]);
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public Hero getHeroesCommander() {
        return heroesCommander;
    }

    public List<Hero> getSelected() {
        return selected;
    }

    public HeroesController(GameController gc) {
        this.gc = gc;
        this.heroes = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.textures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("heroes")).split(64, 64)[0];
        //!!! Всего 4 текстуры!
        for (int i = 0; i < 3; i++) {
            heroes.add(new Hero(gc, textures[MathUtils.random(0, 3)]));
            //heroes.add(new Hero(gc));
        }
        selected.add(heroes.get(0));
        this.heroesCommander = heroes.get(0);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font){
        for (int i = 0; i < heroes.size(); i++) {
            heroes.get(i).renderGUI(batch, font, i*150);
        }
    }

    public void setCommander() {

    }

    public void update(float dt) {
        for (int i = 0; i < heroes.size(); i++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                selected.clear();
                selected.add(heroes.get(i));
                heroesCommander = heroes.get(i);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + heroes.size())) {
            selected.clear();
            selected.addAll(heroes);
        }
        for (Hero hero : heroes) {
            hero.update(dt);
        }
        checkPool();
        setCommander();
    }
}
