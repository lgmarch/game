package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.lmarch.rpg.game.logic.utils.ObjectPool;
import com.lmarch.rpg.game.screens.utils.Assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeaponsController extends ObjectPool<Weapon> {
    private TextureRegion[] textures;
    private List<Weapon> prototypes;

    @Override
    protected Weapon newObject() {
        return new Weapon(textures);
    }

    public WeaponsController() {
        this.textures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("weapons")).split(60, 60)[0];
        this.loadPrototypes(textures);
    }

    public void setup(Weapon weapon, float x, float y) {
        Weapon out = getActiveElement();
        //out.copyFrom(prototypes.get(MathUtils.random(0, prototypes.size() - 1)));
        out.copyFrom(weapon); //Поднимаем то оружие, которое обронили
        forge(out);
        out.setPosition(x, y);
        out.activate();
    }

    public Weapon getOneFromAnyPrototype() {
        Weapon out = new Weapon(textures);
        out.copyFrom(prototypes.get(MathUtils.random(0, prototypes.size() - 1)));
        forge(out);
        return out;
    }

    public void forge(Weapon w) {
        for (int i = 0; i < 30; i++) {
            if (MathUtils.random(100) < 5) {
                w.setMinDamage(w.getMinDamage() + 1);
            }
        }
        for (int i = 0; i < 30; i++) {
            if (MathUtils.random(100) < 10) {
                w.setMaxDamage(w.getMaxDamage() + 1);
            }
        }
        if (w.getMinDamage() > w.getMaxDamage()) {
            w.setMinDamage(w.getMaxDamage());
        }
    }

    public void update() {
        checkPool();
    }

    public void loadPrototypes(TextureRegion[] textures) {
        prototypes = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = Gdx.files.internal("data/weapons.csv").reader(8192);
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                prototypes.add(new Weapon(line, textures));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}