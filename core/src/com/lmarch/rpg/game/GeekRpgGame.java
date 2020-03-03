package com.lmarch.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GeekRpgGame extends ApplicationAdapter {
	private SpriteBatch batch; //
	private BitmapFont font24;
	private TextureAtlas atlas;
	private TextureRegion textureGrass;
	private Hero hero;

	//Домашнее задание:
	// 1. Добавить на экран яблоко и попробовать отследить попадание стрелы в яблоко.
	//    При попадании яблоко должно появиться в новом месте.
	// 2. ** Попробуйте заставить героя выпускать несколько стрел.
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.atlas = new TextureAtlas("game.pack"); //Загрузка атласа текстур в память
		this.hero = new Hero(atlas);
		this.textureGrass = atlas.findRegion("grass");
		this.font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
	}

	@Override
	public void render () {
		//x += v(100px/sec)*dt
		float dt = Gdx.graphics.getDeltaTime();

		update(dt);

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
		hero.renderGUI(batch, font24);//GUI желательно рисовать отдельно

		batch.end();
	}

	public void update(float dt){
		hero.update(dt);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
