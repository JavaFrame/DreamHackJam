package ch.dhj.game.obj;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Sebastian on 29.09.2017.
 * Represents a general object. This object will be updated by the {@link ParentObject}
 */
public abstract class GObject {
	private ParentObject parent;

	/**
	 * called from {@link ParentObject#init()} which gets called from the {@link Screen}
	 */
	public abstract void init();

	/**
	 * called from {@link ParentObject#render(float)} which gets called from {@link Screen#render(float)}
	 * @param delta
	 */
	public abstract void render(float delta);

	/**
	 * called from {@link ParentObject#dispose()} which gets called from {@link Screen#dispose()}
	 */
	public abstract void dispose();


	/**
	 * Sets the parent of this GObject. This function is called from {@link ParentObject#add(GObject)} when the object is added.
	 * @param parent
	 */
	protected void setParent(ParentObject parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent of this object or null if it either isn't added to a parent or
	 * @return
	 */
	public ParentObject getParent() {
		return parent;
	}

	public boolean hasParent() {
		return getParent() != null;
	}

	public WorldConfig getWorldConfig() {
		return getParent().getWorldConfig();
	}
	/**
	 * Returns the world of its parent or if its parent is null (because it isn't set to an parent or it is the root object) it will return null.
	 * @return the world
	 */
	public World getWorld() {
		if(getParent() == null) return null;
		return getParent().getWorld();
	}

	/**
	 * Returns the batch of the parent
	 * @return
	 */
	public SpriteBatch getBatch() {
		return getParent().getBatch();
	}
}
