package com.lmarch.rpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lmarch.rpg.game.screens.ScreenManager;

public class GeekRpgGame extends Game { //Меняем на Game для управления экранами
	private SpriteBatch batch; //

	// Домашнее задание 8 Урок:
	// +  Реализация рюкзака. От Монстра остается оружие, и именно его поднимает Герой.
	// -  Функция выброса оружия из рюкзака
	// +  Отображение оружия Игроков.
	// -  Не работает Scroll.
	// -  Управление второстепенным персонажем.
	// -  Картинка вылетевшей души (через специал эффект).
	// -  Добавить кнопку выключения звука, Оружие, перерисовать кнопки Меню.
	// -  Несколько видов Монстров (группы), враждующих между собой
	// -  Управление несколькими Героями
	// +  1. Добавте опыт и уровни всем персонажам ,когда персонаж наносит урон,
	//    то он получает опыт. Когда опыт достигает 1000, 2000, 4000 и т.д. ед.опыта,
	//    уровень персонажа растет.
	// +  2. При повышении уровня, может повышаться сила персонажа (у меня увеличивается урон)
	// +  3. Добавить отображение уровней
	// -  Сохранение игры
	// +  Добавить разный звук на оружие.
	// -  Освобождение ресурса sound в классе Weapon
	
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
