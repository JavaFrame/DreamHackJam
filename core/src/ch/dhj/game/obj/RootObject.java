package ch.dhj.game.obj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class RootObject extends ParentObject {
	private final Logger logger = Logger.getLogger(getClass().getName());

	private float accumulator = 0;

	private WorldConfig worldConfig;

	private World world;

	private SpriteBatch batch;

	@Override
	public void init() {
		world = new World(getWorldConfig().getGravity(), true);
		batch = new SpriteBatch();

		super.init();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render(delta);

		physicsCalculations(delta);
	}

	@Override
	public void dispose() {
		super.dispose();

		getWorld().dispose();
		getBatch().dispose();
	}

	@Override
	protected void setParent(ParentObject parent) {
		logger.warning("RootObject was added to an other ParentObject. RootObject shouldn't be added to any parent.");
		super.setParent(parent);
	}

	/**
	 * Does the physics world update.
	 * @param delta the time between the frames
	 */
	private void physicsCalculations(float delta) {
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		while(accumulator >= TIME_STEPS) {
			getWorld().step(TIME_STEPS, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEPS;
		}
	}
}
