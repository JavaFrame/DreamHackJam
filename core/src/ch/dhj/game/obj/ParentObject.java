package ch.dhj.game.obj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class ParentObject extends GObject {
	public static final float TIME_STEPS = 1/45;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;

	private ArrayList<GObject> objects = new ArrayList<GObject>();

	private boolean alreadyStarted = false;

	public ParentObject() {
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
	}

	@Override
	public void dispose() {
		for(GObject obj : objects) {
			obj.dispose();
		}
	}

}
