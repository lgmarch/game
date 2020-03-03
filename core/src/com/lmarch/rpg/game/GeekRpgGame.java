package com.lmarch.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GeekRpgGame extends ApplicationAdapter {
	private SpriteBatch batch; //
	private Texture textureGrass;
	private Hero hero;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		hero = new Hero();
		textureGrass = new Texture("grass.png");
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
