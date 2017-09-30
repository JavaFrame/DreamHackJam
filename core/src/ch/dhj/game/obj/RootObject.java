package ch.dhj.game.obj;

import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;

import java.util.logging.Logger;

import static ch.dhj.game.utils.WorldConfig.scale;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class RootObject extends ParentObject {
	private final Logger logger = Logger.getLogger(getClass().getName());

	private float accumulator = 0;

	private WorldConfig worldConfig;

	private World world;

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private TiledMap map;

	public RootObject(AssetManager assetManager, WorldConfig worldConfig, String mapPath) {
		this.worldConfig = worldConfig;

		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(mapPath, TiledMap.class);
		assetManager.finishLoading();
		map = assetManager.get(mapPath);
	}

	@Override
	public void init() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, scale(WorldConfig.VIEWPORT_WIDTH), scale(WorldConfig.VIEWPORT_HEIGHT));
		world = new World(getWorldConfig().getGravity(), true);
		batch = new SpriteBatch();

		super.init();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
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

	@Override
	public ParentObject getParent() {
		return this;
	}

	@Override
	public WorldConfig getWorldConfig() {
		return worldConfig;
	}

	@Override
	public OrthographicCamera getCamera() {
		return camera;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public TiledMap getMap() {
		return map;
	}
}
