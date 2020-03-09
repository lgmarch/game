package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends AbstractScreen {
    private BitmapFont font24;
    private ProjectilesController projectilesController;
    private Map map;
    private Hero hero;
    private Monster monster;
    private Vector2 tmp, tmp2;

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    public Hero getHero() {
        return hero;
    }

    @Override //Инициализация
    public void show() {
        this.projectilesController = new ProjectilesController();
        this.hero = new Hero(this);
        this.monster = new Monster(this);
        this.map = new Map();
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.tmp = new Vector2(0, 0);
        this.tmp2 = new Vector2(0, 0);
    }

    @Override //Отрисовка
    public void render(float delta) {
        update(delta);
        //Цвет очистки экрана: выбор цвета
        Gdx.gl.glClearColor(1, 1, 1, 1);
        //Очистка экрана
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        map.render(batch);
        hero.render(batch);
        monster.render(batch);
        projectilesController.render(batch);
        hero.renderGUI(batch, font24);//GUI желательно рисовать отдельно
        batch.end();
    }

    public void update(float dt){
        hero.update(dt);
        monster.update(dt);

        checkCollisions();
        collideUnits(hero, monster);
        projectilesController.update(dt);
    }

    public void collideUnits(GameCharacter u1, GameCharacter u2){
        if (u1.getArea().overlaps(u2.getArea())){
            tmp.set(u1.getArea().x, u1.getArea().y); //Записали центр первой окружности
            tmp.sub(u2.getArea().x, u2.getArea().y); //из центра первой окр. вычитаем центр второй
            float halfInterLen = ((u1.getArea().radius + u2.getArea().radius) - tmp.len()) / 2.0f;
            tmp.nor(); //нормируем

            tmp2.set(u1.getPosition()).mulAdd(tmp, halfInterLen);
            u1.changePosition(tmp2);

            tmp2.set(u2.getPosition()).mulAdd(tmp, -halfInterLen);
            u2.changePosition(tmp2);
        }
    }

    public void checkCollisions(){
        for (int i = 0; i < projectilesController.getActiveList().size(); i++) {
            //Ссылка на один из активных снарядов
            Projectile p = projectilesController.getActiveList().get(i);
            if (p.getPosition().dst(monster.getPosition()) < 24) {
                p.deactivate();
                if (monster.takeDamage(1)){
                    hero.addCoins(MathUtils.random(1, 10));
                }
            }
        }
    }
}
