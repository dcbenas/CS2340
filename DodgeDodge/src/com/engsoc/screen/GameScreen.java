package com.engsoc.screen;

import com.engsoc.world.GameWorld;
import com.engsoc.world.GameRenderer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameScreen implements Screen {
	
	private GameWorld world;
	private GameRenderer renderer;
	

	
	public GameScreen (float w, float h) {
		world = new GameWorld(w, h);
		renderer = new GameRenderer(world);
		System.out.println("GameScreen Attached");
	}

	@Override
	public void render(float delta) {
		world.update(delta);
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("resized");
	}

	@Override
	public void show() {
		System.out.println("show'ed");
	}

	@Override
	public void hide() {
		System.out.println("hidden");
	}

	@Override
	public void pause() {
		System.out.println("paused");
	}

	@Override
	public void resume() {
		System.out.println("resumed");
	}

	@Override
	public void dispose() {
		System.out.println("disposed");
	}

}
