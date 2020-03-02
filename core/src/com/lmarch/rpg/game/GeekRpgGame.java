package com.lmarch.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GeekRpgGame extends ApplicationAdapter {
	private SpriteBatch batch; //
	private Texture textureGrass;
	private Texture texturePointer;
	private Hero hero;

	private Vector2 pointerPosition;
	private float rt;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		hero = new Hero();
		textureGrass = new Texture("grass.png");
		texturePointer = new Texture("pointer.png");
		pointerPosition = new Vector2(0, 0);
	}

	@Override
	public void render () {
		//x += v(100px/sec)*dt
		float dt = Gdx.graphics.getDeltaTime();

		update(dt);

		//Цвет очистки экрана: выбор цвета
		Gdx.gl.glClearColor(0, 0, 0, 1);
		//Очистка экрана
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				batch.draw(textureGrass, i * 80, j * 80);
			}
		}
		batch.draw(texturePointer, pointerPosition.x-32, pointerPosition.y-32,
				32, 32, 64, 64, 1, 1, rt,
				0, 0, 64, 64, false, false);
		hero.render(batch);

		batch.end();
	}

	public void update(float dt){
		rt -= dt * 90.0f;
		hero.update(dt);

		if (Gdx.input.justTouched()){
			pointerPosition.set(Gdx.input.getX(), 720.0f - Gdx.input.getY());
		}
		hero.moveToMouse(pointerPosition, dt);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
