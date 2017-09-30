package ch.dhj.game.screens;

import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.Comparator;

import static ch.dhj.game.TexturesConst.ENCOUNTER_1_BG;
import static ch.dhj.game.utils.WorldConfig.*;
import static java.awt.Color.*;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class OverworldScreen implements Screen {

	private TextureAtlas atlasButtons;
	protected Skin skin;
	private SpriteBatch batch;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private TextureAtlas atlasPlayerImage;
    private float jonnyWaveDuration;
    private Animation jonnyWaveAnimation;
    private float jonnyWaveAnimationTime;
    private Image playerImage;
    private TiledMap map;
    private MapRenderer mapRenderer;
    private Player player;


    public OverworldScreen(AssetManager assetManager, SpriteBatch batch, Player p) {
		this.assetManager = assetManager;
		this.batch = batch;
        player = p;

        this.assetManager.load("textures/jonnySprite.pack", TextureAtlas.class);
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.assetManager.load("map/test.tmx", TiledMap.class);
        assetManager.finishLoading();

        map = assetManager.get("map/test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 10);


        atlasPlayerImage = assetManager.get("textures/jonnySprite.pack");

        jonnyWaveDuration = 1.0f / 1.5f;
        Array<TextureAtlas.AtlasRegion> jonnyWaveRegions = new Array<TextureAtlas.AtlasRegion>(atlasPlayerImage.getRegions());
        jonnyWaveRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
            @Override
            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        jonnyWaveAnimation = new Animation(jonnyWaveDuration, jonnyWaveRegions, Animation.PlayMode.LOOP);
        jonnyWaveAnimationTime = Gdx.graphics.getDeltaTime();

		atlasButtons = assetManager.get("textures/defaultSkin.pack");
		skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		camera = new OrthographicCamera();
		viewport = new StretchViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT,camera);
		viewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, this.batch);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.bottom().right();

		TextButton frontFieldButton = new TextButton("Go to next Field", skin);
		TextButton lastFieldButton = new TextButton("Go to last Field", skin);
		TextButton saveAndQuit = new TextButton("Save and Exit", skin);

		frontFieldButton.pad(5,20,5,20);
		lastFieldButton.pad(5,20,5,20);
		saveAndQuit.pad(5,20,5,20);

		frontFieldButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
			// Move
			}
		});

		lastFieldButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Move
			}
		});
		saveAndQuit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// SAVE
				Gdx.app.exit();
			}
		});

		mainTable.add(frontFieldButton).width(150);
		mainTable.row().pad(5,20,5,20);
		mainTable.add(lastFieldButton).width(150);
		mainTable.row().pad(5,20,5,20);
		mainTable.add(saveAndQuit).width(150);
		stage.addActor(mainTable);

		Table player = new Table();
        player.setFillParent(true);
        player.bottom().left();

        playerImage = new Image((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime));

        Table playerProfile = new Table();
        playerProfile.setFillParent(true);
        playerProfile.bottom().left();
        playerProfile.add(playerImage).width(150).height(150);

        Table playerStats = new Table();
        playerStats.setFillParent(true);
        playerStats.bottom().left();

        Label hp = new Label("Hp: ",skin);
        Label level = new Label("Level: ",skin);
        Label exp = new Label("EXP: ",skin);
        Label hpPlayer = new Label(this.player.getLifes() + "/" + this.player.getMaxLifes(),skin);
        Label levelPlayer = new Label(String.valueOf(this.player.getLevel()),skin);
        Label expPlayer = new Label(this.player.getExp() + "/" + this.player.getTotalExpToNextLevel(),skin);

        TextButton inventory = new TextButton("Inventory", skin);

        inventory.pad(5,20,5,20);
        inventory.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Open Inventory
            }
        });
        playerStats.add(hp).left();
        playerStats.add(hpPlayer).left();
        playerStats.row();
        playerStats.add(level).left();
        playerStats.add(levelPlayer).left();
        playerStats.row();
        playerStats.add(exp).left();
        playerStats.add(expPlayer).left();

        Table playerInv = new Table();
        playerInv.setFillParent(true);
        playerInv.bottom().left();

        playerInv.add(playerStats);
        playerInv.row();
        playerInv.add(inventory).width(150);

        player.add(playerProfile);
        player.add(playerInv);

        stage.addActor(player);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1.0f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jonnyWaveAnimationTime += Gdx.graphics.getDeltaTime();
        playerImage.setDrawable(new TextureRegionDrawable((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime)));

        batch.begin();
		batch.setProjectionMatrix(camera.combined);
        batch.end();
        mapRenderer.setView(camera);
        mapRenderer.render();

		stage.act();
		stage.draw();
		camera.update();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
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
		atlasButtons.dispose();
		batch.dispose();
		skin.dispose();
		stage.dispose();
		atlasPlayerImage.dispose();
		map.dispose();
	}
}
