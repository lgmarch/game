package com.lmarch.rpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lmarch.rpg.game.GeekRpgGame;
import com.lmarch.rpg.game.screens.utils.Assets;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME
    }

    public static final int WORLD_WIDTH = 1280;
    public static final int HALF_WORLD_WIDTH = WORLD_WIDTH / 2;
    public static final int WORLD_HEIGHT = 720;
    public static final int HALF_WORLD_HEIGHT = WORLD_HEIGHT / 2;

    private GeekRpgGame game; //Доступ к корневой игре
    private SpriteBatch batch;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private Screen targetScreen;
    private Viewport viewport; //Как выводить картинку на экран
    private Camera camera; //Указывает какую точку рисовать

    //Синглтон - глобальный объект в единствнном экземпляре
    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

//    public Camera getCamera() {
//        return camera;
//    }

    private ScreenManager() {
    }

    public void init(GeekRpgGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT); //2D камера
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera); //Вписываем Мир в окно с сохранением пропорций
        //Получаем ссылку на все экраны приложения
        this.menuScreen = new MenuScreen(batch);
        this.gameScreen = new GameScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    //Сбрасывает камеру в 0
    public void resetCamera() {
        camera.position.set(HALF_WORLD_WIDTH, HALF_WORLD_HEIGHT, 0);
        camera.update();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

    //Выставляет камеру в указанную точку
    public void pointCameraTo(Vector2 position) {
        camera.position.set(position, 0);
        camera.update();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        Gdx.input.setInputProcessor(null); //!!! чтобы забыть о прошлом экране!
        if (screen != null) {
            screen.dispose();
        }

        resetCamera();
        game.setScreen(loadingScreen);

        switch (type) {
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}
