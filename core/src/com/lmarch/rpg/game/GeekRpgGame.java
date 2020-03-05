package com.lmarch.rpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GeekRpgGame extends Game { //Меняем на Game для управления экранами
	private SpriteBatch batch; //

	//Домашнее задание:
	// 1. Если здоровье монстра падает до 0, перекидываем его в другую точку
	//   и залечиваем полностью, герою даем монетку (от 3 до 10)
	// 2. ** Если монстр подошел близко к герою, то раз в 0.5 сек он должен
	//   наносить герою 1 урона
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();
		getScreen().render(dt); //Рисуем текущий экран
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
