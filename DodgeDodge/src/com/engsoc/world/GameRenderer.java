package com.engsoc.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.engsoc.actors.Enemy;

public class GameRenderer {
	
	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private String geoStatus;
	private SpriteBatch batch;

	public GameRenderer(GameWorld world) {
		this.world = world;
		cam = new OrthographicCamera();
		cam.setToOrtho(true);
		font = new BitmapFont(true);
		font.setColor(Color.RED);
		batch = new SpriteBatch(); 
		
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
	}

	public void render() {
        /*
         * White background
         */
		
		batch.begin();
		
		String status = world.player.geoStatus() + "\nScore:" + world.getScore();
		 	
		font.drawMultiLine(batch, status, 0, 0);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeType.Filled);
        if (!world.gameOver) {
			batch.draw(world.getPlayer().getPic(),world.getPlayer().getCircle().x, world.getPlayer().getCircle().y);
		}
        shapeRenderer.setColor(0/255f, 0/255f, 255/255f, 1);
		for(Enemy e: world.getEnemy()) {
			batch.draw(Enemy.getPic(),e.getCircle().x, e.getCircle().y);
		}
		
		shapeRenderer.end();
		batch.end();
	}

}
