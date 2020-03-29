package com.lmarch.rpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lmarch.rpg.game.logic.GameController;
import com.lmarch.rpg.game.logic.InventoryGui;
import com.lmarch.rpg.game.logic.WorldRenderer;
import com.lmarch.rpg.game.screens.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gc;
    private WorldRenderer worldRenderer;
    private InventoryGui inventoryGui;
    private Stage stage;
    private Skin skin;
    private boolean paused;

    public GameScreen(SpriteBatch batch) {
        super(batch);
        paused = false;
    }

    @Override //Инициализация
    public void show() {
        this.gc = new GameController();
        this.worldRenderer = new WorldRenderer(gc, batch);
        createGui();
        this.inventoryGui = new InventoryGui(stage, skin, gc.getHero().getInventory());
    }

    @Override //Отрисовка
    public void render(float delta) {
        if (!paused) {
            gc.update(delta);
            //gc.getMusic().play();
        }
        worldRenderer.render();
        //gc.getMusic().pause();
        stage.draw();
    }

    public void createGui() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage); //Обработка ввода пользователя для stage
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        BitmapFont font14 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");

        TextButton.TextButtonStyle menuBtnStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("shortButton"), null, null, font14);

        TextButton btnPause = new TextButton("P", menuBtnStyle);
        btnPause.setPosition(1200, 650);
        TextButton btnExit = new TextButton("E", menuBtnStyle);
        btnExit.setPosition(1100, 650);

        btnPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
                if (paused) {
                    stage.getActors().get(0).setColor(Color.BLUE);
                    //btnPause.setText("Paus");
                } else {
                    stage.getActors().get(0).setColor(Color.GREEN);
                    //btnPause.setText("Run");
                }
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                inventoryGui.switchVisible();
            }
        });

        stage.addActor(btnPause); //добавление на сцену
        stage.addActor(btnExit);
        skin.dispose();
    }

    //Обработка событий сцены
    public void update(float dt) {
        stage.act(dt);
        inventoryGui.update();
    }

    @Override
    public void dispose() {
        gc.dispose();
    }
}
