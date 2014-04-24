package com.engsoc.world;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.engsoc.actors.Dodger;
import com.engsoc.actors.Enemy;

import java.util.ArrayList;

public class GameWorld {
	
	public Dodger player;
	private ArrayList<Enemy> enemy;
	private float timer, difficulty;
	private ArrayList<Enemy> toRemove;
	private long score;
	public boolean gameOver;
	private float w;
	private float h;
	
	public GameWorld(float w, float h) {
		//init initial variables
		enemy = new ArrayList<Enemy>();
		player = new Dodger(w, h);
		timer = 0f;
		difficulty = 1f;
		score = 0;
		toRemove = new ArrayList<Enemy>();
		this.w = w;
		this.h = h;
		
		//initialize each enemy with random velocity and x-acceleration, but always a random positive y-acceleration
		for(int i = 0; i < 10; i++)
			enemy.add(new Enemy((float) Enemy.random(w/2) + (w/2), -50f, new Vector2(Enemy.random(10),Enemy.random(10)),new Vector2(Enemy.random(10),(float) Math.random() * 1000), w + h));
		gameOver = false;
	}
	
	public Dodger getPlayer() {
		return player;
	}
	
	public ArrayList<Enemy> getEnemy() {
		return enemy;
	}
	
	private void increaseEnemy() {
		enemy.add(new Enemy((float) Enemy.random(w/2) + (w/2), -50f, new Vector2(Enemy.random(10),Enemy.random(10)),new Vector2(Enemy.random(10),(float) Math.random() * 1000), w + h));
	}
	
	public void update(float delta) {
		timer = timer + delta;
		if (timer > 1.0f) {
			for (int i = 0; i < 10; i++)
				increaseEnemy();
			timer = timer - difficulty;
			if (difficulty > .201f)
				difficulty -= 0.01f;
			score += 100;
		}
		
		if (!gameOver) {
			for (Enemy e : enemy) {
				if (Intersector.overlaps(player.getCircle(), e.getCircle())) {
					gameOver = true;
					System.out.println("GameOVER");
				}
			}
			player.update(delta, this);
		}
		int enemySize = enemy.size();
		for (int i = 0; i < enemy.size(); i++) {
			if (enemy.get(i).update(delta, this))
        		enemy.remove(i);
		}
	}
	
	public float getH() {
		return h;
	}
	
	public float getW() {
		return w;
	}

	public long getScore() {
		return score;
	}
}
