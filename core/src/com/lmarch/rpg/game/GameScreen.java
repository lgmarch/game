package com.lmarch.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen extends AbstractScreen {
    private BitmapFont font24;
    private TextureRegion textureGrass;
    private ProjectilesController projectilesController;
    private Hero hero;
    private Monster monster;
    private float halfSecond;

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
        this.textureGrass = Assets.getInstance().getAtlas().findRegion("grass");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.halfSecond = 0;
    }

    @Override //Отрисовка
    public void render(float delta) {
        update(delta);
        //Цвет очистки экрана: выбор цвета
        Gdx.gl.glClearColor(1, 1, 1, 1);
        //Очистка экрана
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(textureGrass, i * 80, j * 80);
            }
        }
        hero.render(batch);
        monster.render(batch);
        projectilesController.render(batch);
        hero.renderGUI(batch, font24);//GUI желательно рисовать отдельно
        batch.end();
    }

    public void update(float dt){
        hero.update(dt);
        monster.update(dt);
        checkCollisions(dt);
        projectilesController.update(dt);
    }

    public void checkCollisions(float dt){
        for (int i = 0; i < projectilesController.getActiveList().size(); i++) {
            //Ссылка на один из активных снарядов
            Projectile p = projectilesController.getActiveList().get(i);
            if (p.getPosition().dst(monster.getPosition()) < 24) {
                p.deactivate();
                if (monster.takeDamage(20)){
                    monster.reincarnation();
                    hero.setCoin(3);
                };
            }
        }
        if (hero.getPosition().dst(monster.getPosition()) < 20){
            halfSecond += dt;
            if (halfSecond > 0.5f){
                halfSecond = 0.0f;
                hero.reducingHealthHero(1);
            }
        }
    }
}
