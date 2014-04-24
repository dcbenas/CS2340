package com.engsoc.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.engsoc.world.GameWorld;

public class Dodger extends Actor {
	
	private Texture picture;
	
	public Dodger(float w, float h) {
		super("Dodger", w/2, h/2, (w+h)/125);
		picture = new Texture("data/player.png");
	}
	
	@Override
	public boolean update(float delta, GameWorld world) {
		// Update velocity
		float x = Gdx.input.getPitch();
		float y = Gdx.input.getRoll();
		
		if (x < -30)
			x = -30;
		if (x >  30)
			x = 30;
		if (y < -30)
			y = -30;
		if (y >  30)
			y = 30;
		
		// Multiply by acc of 10, so 30 / 10 = 3
		x = x * -30;
		y = y * -30;
		
		velocity = new Vector2(x , y);
		
		// Changing position of the actor
		position.add(velocity.cpy().scl(delta));

		getCircle().setPosition(position);
		
		return false;
	}
	
	public String geoStatus() {
		float x = Gdx.input.getPitch();
		float y = Gdx.input.getRoll();
		String s = new String("Pitch: " + x + "\nRoll: " + y);
		return s;
	}
	
	public Texture getPic() {
		return picture;
	}
}
