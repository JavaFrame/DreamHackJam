package ch.dhj.game.screens;

import ch.dhj.game.obj.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static ch.dhj.game.TexturesConst.ENCOUNTER_1_BG;
import static ch.dhj.game.obj.WorldConfig.PPM;
import static ch.dhj.game.obj.WorldConfig.scale;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class EncounterScreen implements Screen {
	private AssetManager assetManager;
	private SpriteBatch batch;

	private Viewport viewport;
	private OrthographicCamera camera;

	private TiledMap map;
	private MapRenderer mapRenderer;

	private Sprite background;

	private EncounterState state = EncounterState.PLAYER_TURN;

	private Skin skin;
	private TextureAtlas atlasButtons;
	private Stage stage;
	private Table table;

	public EncounterScreen(int encounter_id, AssetManager assetManager, SpriteBatch batch) {
		this.assetManager = assetManager;
		this.batch = batch;

		viewport = new StretchViewport(scale(WorldConfig.VIEWPORT_WIDTH), scale(WorldConfig.VIEWPORT_WIDTH));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, scale(WorldConfig.VIEWPORT_WIDTH), scale(WorldConfig.VIEWPORT_WIDTH));
		//camera.position.set(new Vector3(viewport.getScreenWidth()/2, viewport.getScreenHeight()/2, 0));

		//asset loading
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("map/test.tmx", TiledMap.class);
		assetManager.load(ENCOUNTER_1_BG, Texture.class);
		assetManager.finishLoading();

		//map loading
		map = assetManager.get("map/test.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

		//background loading
		background = new Sprite((Texture) assetManager.get(ENCOUNTER_1_BG, Texture.class));
		background.setPosition(0, 0);
		background.setSize(scale(WorldConfig.VIEWPORT_WIDTH), scale(WorldConfig.VIEWPORT_WIDTH));

		//ui stuff
		atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
		skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		stage = new Stage(new StretchViewport(1920/2, 1080/2));
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		table.setDebug(true);

		ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
		TextButton attackB = new TextButton("Attack", skin);
		TextButton spellB = new TextButton("Spell", skin);
		TextButton defendB = new TextButton("Defend", skin);
		TextButton fleeB = new TextButton("Flee", skin);

		table.left();
		table.add(progressBar);
		table.row();
		table.add(attackB).width(100);
		table.row();
		table.add(spellB).width(100);
		table.row();
		table.add(defendB).width(100);
		table.row();
		table.add(fleeB).width(100);
	}


	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		//render stuff
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch);
		batch.end();
		mapRenderer.setView(camera);
		mapRenderer.render();

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private enum EncounterState {
		ENEMY_TURN,
		PLAYER_TURN
	}

	public class EncounterConfig {
		public int id;
		public String background;
		public String map;

	}
}
