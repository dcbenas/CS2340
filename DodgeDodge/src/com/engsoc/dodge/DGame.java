package com.engsoc.dodge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.engsoc.screen.GameScreen;
 
public class DGame extends Game {

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		System.out.println("DGame created");
		setScreen(new GameScreen(w, h));
	}
	
}