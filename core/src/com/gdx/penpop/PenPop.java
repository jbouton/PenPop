package com.gdx.penpop;

import com.badlogic.gdx.ApplicationListener;
		import com.badlogic.gdx.Game;
		import com.badlogic.gdx.Gdx;
		import com.badlogic.gdx.Screen;
		import com.badlogic.gdx.graphics.GL20;
		import com.badlogic.gdx.graphics.OrthographicCamera;
		import com.badlogic.gdx.graphics.Texture;
		import com.badlogic.gdx.graphics.Texture.TextureFilter;
		import com.badlogic.gdx.graphics.g2d.Sprite;
		import com.badlogic.gdx.graphics.g2d.SpriteBatch;
		import com.badlogic.gdx.graphics.g2d.TextureRegion;
		import com.gdx.penpop.screens.MainMenuScreen;
		import com.gdx.penpop.screens.PlayScreen;
		import com.gdx.penpop.screens.SplashScreen;




public class PenPop extends Game {

	public MainMenuScreen mainMenuScreen;
	public PlayScreen playScreen;
	public SplashScreen splashScreen;
	@Override
	public void create() {
		mainMenuScreen = new MainMenuScreen(this);
		playScreen = new PlayScreen(this);
		splashScreen = new SplashScreen(this);
		setScreen(splashScreen);


	}

	@Override
	public void dispose() {
		super.dispose();

	}

	@Override
	public void render() {
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}

