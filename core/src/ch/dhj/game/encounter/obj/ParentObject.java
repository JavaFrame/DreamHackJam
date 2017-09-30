package ch.dhj.game.encounter.obj;

import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.screens.EncounterScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class ParentObject {
	public static final float TIME_STEPS = (float) 1/45;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;

	private ArrayList<GObject> objects = new ArrayList<GObject>();

	private boolean alreadyStarted = false;

	private final Logger logger = Logger.getLogger(getClass().getName());

	private float accumulator = 0;

	private OrthographicCamera camera;

	private TiledMap map;

	private TurnManager turnManager;
	private EncounterScreen.EncounterConfig encounterConfig;
	private AssetManager assetManager;
	private Viewport viewport;

	public ParentObject(TiledMap map, OrthographicCamera camera, SpriteBatch batch, EncounterScreen.EncounterConfig encounterConfig, AssetManager assetManager, Viewport viewport) {
		this.map = map;
		this.encounterConfig = encounterConfig;
		this.assetManager = assetManager;
		this.viewport = viewport;
		this.turnManager = new TurnManager();
		this.camera = camera;
	}

	public void add(GObject obj) {
		objects.add(obj);
		obj.setParent(this);
		if(alreadyStarted) //when an object is added after the init call of this objecmanager
			obj.init();
	}

	public void addAll(GObject[] objs) {
		for(GObject obj: objs) {
			add(obj);
		}
	}

	public void addAll(Array<GObject> objs) {
		for(GObject obj: objs) {
			add(obj);
		}
	}


	public void init() {
		for(GObject obj : objects)
			obj.init();
	}

	public void render(float delta, SpriteBatch batch) {
		for(GObject obj :  objects)
			obj.render(delta, batch);

	}

	public void resize(int width, int height) {
		for(GObject obj : objects)
			obj.resize(width, height);
	}

	public void dispose() {
		for(GObject obj : objects)
			obj.dispose();
	}

	public ParentObject getParent() {
		return this;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public TiledMap getMap() {
		return map;
	}

	public TurnManager getTurnManager() {
		return turnManager;
	}

	public EncounterScreen.EncounterConfig getEncounterConfig() {
		return encounterConfig;
	}

	public Viewport getViewport() {
		return viewport;
	}
}
