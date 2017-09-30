package ch.dhj.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class WorldConfig {
	public static final int VIEWPORT_WIDTH = 1920;
	public static final int VIEWPORT_HEIGHT = 1080;

	/**
	 * pixels per meters
	 */
	public static final float PPM = 100;

	/**
	 * Scales the float f with {@link #PPM}
	 * @param f
	 * @return a scaled f
	 */
	public static float scale(float f) {
		return f / PPM;
	}

	private Vector2 gravity;

	public WorldConfig(Vector2 gravity) {
		this.gravity = gravity;
	}

	public Vector2 getGravity() {
		return gravity;
	}
}
