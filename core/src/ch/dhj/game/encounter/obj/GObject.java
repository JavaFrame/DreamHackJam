package ch.dhj.game.encounter.obj;

import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.screens.EncounterScreen;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Sebastian on 29.09.2017.
 * Represents a general object. This object will be updated by the {@link ParentObject}
 */
public abstract class GObject {
	private ParentObject parent;
	private TextureRegion texture;
	private Animation<TextureRegion> animation;
	private float stateTime;

	private Vector2 position;
	private Vector2 size;

	public GObject(Vector2 position) {
		this.position = position;
		this.size = new Vector2(1, 1);
		this.texture = null;
	}

	public GObject(Sprite texture, Vector2 position, Vector2 size) {
		this.texture = texture;
		this.position = position;
		this.size = size;
	}

	/**
	 * called from {@link ParentObject#init()} which gets called from the {@link Screen}
	 */
	public abstract void init();

	/**
	 * called from {@link ParentObject#render(float, SpriteBatch)} which gets called from {@link Screen#render(float)}
	 * @param delta
	 */
	public void render(float delta, SpriteBatch batch) {
		if(getTextureRegion() != null && getAnimation() == null) {
			batch.draw(getTextureRegion(), getPosition().x, getPosition().y, getSize().x, getSize().y);
		} else if(getAnimation() != null) {
			stateTime += delta;
			if(stateTime == 0)
				stateTime = 0.1f;
			if(getAnimation().getKeyFrames().length == 0) {
				System.err.println("Animation of " + toString() + " has no keyframes!");
				return;
			}
			batch.draw(getAnimation().getKeyFrame(stateTime), getPosition().x, getPosition().y, getSize().x, getSize().y);
		}
	}

	public void renderUi(float delta, SpriteBatch batch) {}

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

	public TextureRegion getTextureRegion() {
		return texture;
	}

	public void setTextureRegion(TextureRegion texture) {
		this.texture = texture;
	}

	public OrthographicCamera getCamera() {
		if(getParent() == null) return null;
		return getParent().getCamera();
	}


	public TiledMap getMap() {
		if(getParent() == null) return null;
		return getParent().getMap();
	}

	public TurnManager getTurnManager() {
		if(getParent() == null) return null;
		return getParent().getTurnManager();
	}

	public EncounterScreen.EncounterConfig getEncounterConfig() {
		if(getParent() == null) return null;
		return getParent().getEncounterConfig();
	}

	public Viewport getViewport() {
		if(getParent() == null) return null;
		return getParent().getViewport();
	}

	public EncounterScreen getEncounterScreen() {
		if(getParent() == null) return null;
		return getParent().getEncounterScreen();
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
		stateTime = 0;
	}

	public boolean isAnnimationFinished() {
		if(getAnimation() == null) return true;
		return getAnimation().isAnimationFinished(getStateTime());
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public float getStateTime() {
		return stateTime;
	}
}
