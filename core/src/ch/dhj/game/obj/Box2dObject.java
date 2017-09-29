package ch.dhj.game.obj;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Sebastian on 29.09.2017.
 * Represents an object which has an physical position in the world. There is a body attached to the object and the {@link #position} variable is automaticliy updated.
 */
public abstract class Box2dObject extends GObject {
	private Vector2 position;
	private Body body;

	/**
	 * Constructor
	 * @param position
	 * @param body
	 */
	public Box2dObject(Vector2 position, Body body) {
		this.position = position;
		this.body = body;
	}

	@Override
	public void render(float delta) {
		position.set(body.getPosition());
	}

	public Vector2 getPosition() {
		return position;
	}

	protected void setPosition(Vector2 position) {
		this.position = position;
	}

	public Body getBody() {
		return body;
	}
}
