package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Считает только общую логику игры. Вся прорисовка в WorldRenderer
 * Теперь отрисовка не перемешена с update()
 */
public class GameController {
    private ProjectilesController projectilesController;
    private MonsterController monsterController;
    private Map map;
    private Hero hero;
    private Vector2 tmp, tmp2;

    public Hero getHero() {
        return hero;
    }

    public Map getMap() {
        return map;
    }

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public MonsterController getMonsterController() {
        return monsterController;
    }

    public GameController() {
        this.projectilesController = new ProjectilesController();
        this.monsterController = new MonsterController(this);
        this.hero = new Hero(this);
        this.map = new Map();
        this.tmp = new Vector2(0, 0);
        this.tmp2 = new Vector2(0, 0);
    }

    public void update(float dt){
        hero.update(dt);
        monsterController.update(dt);

        checkCollisions();
        //Не вижу смысла в переборе всех объектов, надо заполнять,
        //например, карту позиций объектов и для каждого объекта брать из нее ближайшие координаты на проверку пересечений
        //collideUnits(hero, monster);
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
        for (int i = 0; i < projectilesController.getActiveList().size(); i++) {
            //Ссылка на один из активных снарядов
            Projectile p = projectilesController.getActiveList().get(i);
            if (!map.isAirPassable(p.getCellX(), p.getCellY())) { //Проверка прохождения ч/з стену
                p.deactivate();
                continue;
            }
            for (Monster o : monsterController.getActiveList()) {
                if (p.getPosition().dst(o.getPosition()) < 24) {
                    p.deactivate();
                    if (o.takeDamage(1)){
                        hero.addCoins(MathUtils.random(1, 10));
                        o.onDeath();
                    }
                }
            }
        }
    }
}
