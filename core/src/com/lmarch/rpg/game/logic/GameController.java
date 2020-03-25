package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.ScreenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Считает только общую логику игры. Вся прорисовка в WorldRenderer
 * Теперь отрисовка не перемешена с update()
 */
public class GameController {
    private ProjectilesController projectilesController;
    private MonstersController monstersController;
    private WeaponsController weaponsController;
    private SpecialEffectsController effectsController;
    private TreasureController treasureController;
    private MessageController messageController;
    private List<GameCharacter> allCharacters;
    private Map map;
    private Hero hero;
    private Vector2 tmp, tmp2;
    private Vector2 mouse; //Точка в Мире
    private float worldTimer;
    private Circle circleView;

    public Vector2 getMouse() {
        return mouse;
    }

    public float getWorldTimer() {
        return worldTimer;
    }

    public Hero getHero() {
        return hero;
    }

    public Map getMap() {
        return map;
    }

    public SpecialEffectsController getEffectsController() {
        return effectsController;
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

    public WeaponsController getWeaponsController() {
        return weaponsController;
    }

    public TreasureController getTreasureController() {
        return treasureController;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public GameController() {
        this.allCharacters = new ArrayList<>();
        this.projectilesController = new ProjectilesController(this);
        this.weaponsController = new WeaponsController(this);
        this.effectsController = new SpecialEffectsController();
        this.treasureController = new TreasureController();
        this.messageController = new MessageController();
        this.hero = new Hero(this);
        this.map = new Map();
        this.monstersController = new MonstersController(this, 5);
        this.tmp = new Vector2(0, 0);
        this.tmp2 = new Vector2(0, 0);
        this.mouse = new Vector2(0, 0);
        this.circleView = new Circle(0, 0 , 25);
    }

    public void update(float dt){
        mouse.set(Gdx.input.getX(), Gdx.input.getY()); //В мышку зашиваем координаты x, y
        ScreenManager.getInstance().getViewport().unproject(mouse); //Преобразование координат в мировые

        worldTimer += dt;
        allCharacters.clear();
        allCharacters.add(hero);
        allCharacters.addAll(monstersController.getActiveList());

        hero.update(dt);
        monstersController.update(dt);
        checkCollisions(dt);
        projectilesController.update(dt);
        weaponsController.update(dt);
        effectsController.update(dt);
        treasureController.update(dt);
        messageController.update(dt);
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

    public void checkCollisions(float dt){
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
        for (int i = 0; i < weaponsController.getActiveList().size(); i++) {
            Weapon w = weaponsController.getActiveList().get(i);
            if (hero.getPosition().dst(w.getPosition()) < 20) {
                w.consume(this, hero);
            }
            //Данные об валяющемся оружии
            if (mouse.dst(w.getPosition()) < 20 && !circleView.contains(mouse)) {
                messageController.getActiveElement().setMessage(w.getWeaponInfo(), mouse, Color.BROWN);
                circleView.setPosition(mouse);
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
                    o.takeDamage(p.getOwner(), p.getDamage());
                }
            }
        }
        //Подбираем сокровища
        for (Treasure treasure : treasureController.getActiveList()) {
            if (treasure.isFree()) {
                if (hero.getPosition().dst(treasure.getPosition()) < 20) {
                    treasure.consume(this, hero);
                    if (treasure.getType() == Treasure.Type.ELIXIR) {
                        messageController.getActiveElement().setMessage("+" + String.valueOf(treasure.getQuantity()), hero.position, Color.GREEN);
                    } else {
                        messageController.getActiveElement().setMessage(treasure.getTreasureInfo(), hero.position, Color.GOLD);
                    }
                }
            }
        }
    }
}
