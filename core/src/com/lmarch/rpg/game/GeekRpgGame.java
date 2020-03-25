package com.lmarch.rpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lmarch.rpg.game.screens.ScreenManager;

public class GeekRpgGame extends Game { //Меняем на Game для управления экранами
	private SpriteBatch batch; //

	// Домашнее задание 7 Урок:
	// -  Несколько видов Монстров (группы), враждующих между собой
	// +  Прорисовка текстовки для выпавшего оружия при наведении на него мышью
	// +  Добавить контроллер и класс для вывода сообщений: например, при получении урона вылетает надпись "-3"
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
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
