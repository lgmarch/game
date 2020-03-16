package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Считает только общую логику игры. Вся прорисовка в WorldRenderer
 * Теперь отрисовка не перемешена с update()
 */
public class GameController {
    private ProjectilesController projectilesController;
    private MonstersController monstersController;
    private List<GameCharacter> allCharacters;
    private Map map;
    private Hero hero;
    private Vector2 tmp, tmp2;

    public Hero getHero() {
        return hero;
    }

    public Map getMap() {
        return map;
    }

    public List<GameCharacter> getAllCharacters() {
        return allCharacters;
    }

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public MonstersController getMonstersController() {
        return monstersController;
    }

    public GameController() {
        this.projectilesController = new ProjectilesController();
        this.allCharacters = new ArrayList<>();
        this.hero = new Hero(this);
        this.map = new Map();
        this.monstersController = new MonstersController(this, 5);
        this.tmp = new Vector2(0, 0);
        this.tmp2 = new Vector2(0, 0);
    }

    public void update(float dt){
        allCharacters.clear();
        allCharacters.add(hero);
        allCharacters.addAll(monstersController.getActiveList());

        hero.update(dt);
        monstersController.update(dt);

        checkCollisions();
        projectilesController.update(dt);
    }

    public void collideUnits(GameCharacter u1, GameCharacter u2){
        if (u1.getArea().overlaps(u2.getArea())){
            tmp.set(u1.getArea().x, u1.getArea().y); //Записали центр первой окружности
            tmp.sub(u2.getArea().x, u2.getArea().y); //из центра первой окр. вычитаем центр второй
            float halfInterLen = ((u1.getArea().radius + u2.getArea().radius) - tmp.len()) / 2.0f;
            tmp.nor(); //нормируем

            tmp2.set(u1.getPosition()).mulAdd(tmp, halfInterLen);
            if (map.isGroundPassable(tmp2)) {
                u1.changePosition(tmp2);
            }
            tmp2.set(u2.getPosition()).mulAdd(tmp, -halfInterLen);
            if (map.isGroundPassable(tmp2)) {
                u2.changePosition(tmp2);
            }
        }
    }

    public void checkCollisions(){
        //Проверка столкновений
        for (int i = 0; i < monstersController.getActiveList().size(); i++) {
            Monster m = monstersController.getActiveList().get(i);
            collideUnits(hero, m);
        }
        for (int i = 0; i < monstersController.getActiveList().size(); i++) {
            Monster m = monstersController.getActiveList().get(i);
            for (int j = 0; j < monstersController.getActiveList().size(); j++) {
                Monster m2 = monstersController.getActiveList().get(i);
                collideUnits(m, m2);
            }
        }

        for (int i = 0; i < projectilesController.getActiveList().size(); i++) {
            Projectile p = projectilesController.getActiveList().get(i);
            if (!map.isAirPassable(p.getCellX(), p.getCellY())) { //Проверка прохождения ч/з стену
                p.deactivate();
                continue;
            }
            if (p.getPosition().dst(hero.getPosition()) < 24 && p.getOwner() != hero) {
                p.deactivate();
                hero.takeDamage(p.getOwner(), p.getDamage());
            }
            for (Monster o : monstersController.getActiveList()) {
                if (p.getOwner() == o) {
                    continue;
                }
                if (p.getPosition().dst(o.getPosition()) < 24) {
                    p.deactivate();
                    if (o.takeDamage(p.getOwner(), p.getDamage())){
                        hero.addCoins(MathUtils.random(1, 10));
                    }
                }
            }
        }
    }
}
