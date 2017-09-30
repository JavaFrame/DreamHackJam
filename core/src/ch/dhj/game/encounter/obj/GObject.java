package ch.dhj.game.encounter.obj;

import ch.dhj.game.encounter.TurnManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Sebastian on 29.09.2017.
 * Represents a general object. This object will be updated by the {@link ParentObject}
 */
public abstract class GObject {
	private ParentObject parent;
	private Sprite sprite;

	private Vector2 position;


	public GObject(Vector2 position) {
		this.position = position;
		this.sprite = new Sprite();
	}

	/**
	 * called from {@link ParentObject#init()} which gets called from the {@link Screen}
	 */
	public abstract void init();

	/**
	 * called from {@link ParentObject#render(float)} which gets called from {@link Screen#render(float)}
	 * @param delta
	 */
	public abstract void render(float delta);

	public void resize(int width, int height) {}

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

	public Vector2 getPosition() {
		return position;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public OrthographicCamera getCamera() {
		if(getParent() == null) return null;
		return getParent().getCamera();
	}

	/**
	 * Returns the batch of the parent
	 * @return
	 */
	public SpriteBatch getBatch() {
		if(getParent() == null) return null;
		return getParent().getBatch();
	}

	public TiledMap getMap() {
		if(getParent() == null) return null;
		return getParent().getMap();
	}

	public TurnManager getTurnManager() {
		if(getParent() == null) return null;
		return getParent().getTurnManager();
	}
}
