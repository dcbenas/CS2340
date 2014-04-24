package com.engsoc.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.engsoc.world.GameWorld;

public abstract class Actor {
	private String actorType;
	private Circle circle;
	protected Vector2 position;
	protected Vector2 velocity;
	protected Vector2 acceleration;
	
	public Actor(String actorType, float x, float y, float r) {
		this.actorType = actorType;
		position = new Vector2(x,y);
		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		circle = new Circle(x,y,r);
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	public boolean update(float delta, GameWorld world) {
		// Increase velocity due to acceleration and verify velocity limit
		velocity.add(acceleration.cpy().scl(delta));
		if (velocity.x > 300)
			velocity.x = 300;
		if (velocity.x < -300)
			velocity.x = -300;
		if (velocity.y > 300)
			velocity.y = 300;
		if (velocity.y < -300)
			velocity.y = -300;
		
		// Changing position of the actor
		position.add(velocity.cpy().scl(delta));		
		circle.setPosition(position);
		
		if (position.y > world.getH() + 50f)
			return true;
		if (position.y < -100)
			return true;
		return false;
	}
	
	public void setVelocity(Vector2 add) {
		velocity.add(add);
	}
	
	public void setAcceleration(Vector2 add) {
		acceleration.add(add);
	}
}
