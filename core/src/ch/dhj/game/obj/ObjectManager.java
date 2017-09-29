package ch.dhj.game.obj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class ObjectManager extends GObject {
	public static final float TIME_STEPS = 1/45;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;

	private ArrayList<GObject> objects = new ArrayList<GObject>();

	private float accumulator = 0;

	private boolean alreadyStarted = false;

	private World world;

	private SpriteBatch batch;

	public ObjectManager() {
	}

	public void add(GObject obj) {
		objects.add(obj);
		obj.setParent(this);
		if(alreadyStarted) //when an object is added after the init call of this objecmanager
			obj.init();
	}

	@Override
	public void init() {

		for(GObject obj : objects) {
			obj.init();
		}
		alreadyStarted = true;
	}

	@Override
	public void render(float delta) {
		for(GObject obj : objects) {
			obj.render(delta);
		}

		physicsCalculations(delta);
	}

	@Override
	public void dispose() {
		for(GObject obj : objects) {
			obj.dispose();
		}
		getWorld().dispose();
	}

	/**
	 * Returns either {@link ObjectManager#world} if super.get
	 * @return
	 */
	@Override
	public World getWorld() {
		if(super.getWorld() == null)
			return world;
		return super.getWorld();
	}

	@Override
	public SpriteBatch getBatch() {
		if(super.getBatch() == null)
			return batch;
		return super.getBatch();
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
