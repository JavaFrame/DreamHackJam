package ch.dhj.game.screens;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.Turn;
import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.encounter.actions.WeaponAction;
import ch.dhj.game.utils.WorldConfig;
import ch.dhj.game.player.Enemy;
import ch.dhj.game.player.Player;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;

import static ch.dhj.game.TexturesConst.ENCOUNTER_1_BG;
import static ch.dhj.game.utils.WorldConfig.PPM;
import static ch.dhj.game.utils.WorldConfig.scale;

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

	private EncounterConfig config;
	private Player player;

	private Skin skin;
	private TextureAtlas atlasButtons;
	private Stage stage;
	private Table table;
	private Label actionsL;

	private int currentActionCount = 0;
	private Turn playerTurn;

	private TurnManager turnManager = new TurnManager();

	private EncounterState state = EncounterState.PLAYER_TURN;

	public EncounterScreen(Player p, EncounterConfig config, AssetManager assetManager, SpriteBatch batch) {
		this.player = p;
		this.config = config;
		this.assetManager = assetManager;
		this.batch = batch;

		viewport = new StretchViewport(scale(WorldConfig.VIEWPORT_WIDTH), scale(WorldConfig.VIEWPORT_WIDTH));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, scale(WorldConfig.VIEWPORT_WIDTH), scale(WorldConfig.VIEWPORT_WIDTH));
		//camera.position.set(new Vector3(viewport.getScreenWidth()/2, viewport.getScreenHeight()/2, 0));

		//asset loading
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(config.map, TiledMap.class);
		assetManager.load(config.background, Texture.class);
		assetManager.finishLoading();

		//map loading
		map = assetManager.get(config.map);
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

		//table.setDebug(true);

		actionsL = new Label(String.format("%d/%d Actions", currentActionCount, p.getMaxActionCount()), skin);
		final TextButton attackB = new TextButton("Attack", skin);
		attackB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addPlayerAction(new WeaponAction(player.getCurrentWeapon()));
			}
		});
		TextButton spellB = new TextButton("Spell", skin);
		TextButton defendB = new TextButton("Defend", skin);
		TextButton fleeB = new TextButton("Flee", skin);

		table.left();
		table.add(actionsL);
		table.row();
		table.add(attackB).width(100);
		table.row();
		table.add(spellB).width(100);
		table.row();
		table.add(defendB).width(100);
		table.row();
		table.add(fleeB).width(100);
	}

	private void addPlayerAction(Action a) {
		playerTurn.addAction(a);
		currentActionCount++;
		actionsL.setText(String.format("%d/%d Actions", currentActionCount, player.getMaxActionCount()));
		if(currentActionCount >= player.getMaxActionCount()) {
			turnManager.getTurns().add(playerTurn);
			playerTurn = new Turn();
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		turnManager.update();
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

	public static class EncounterConfig {
		public int id;
		public String background;
		public String map;
		public ArrayList<Enemy> enemies;

		public EncounterConfig(int id, String background, String map, Enemy[] enemies) {
			this.id = id;
			this.background = background;
			this.map = map;
			this.enemies = new ArrayList(Arrays.asList(enemies));
		}
	}
}
