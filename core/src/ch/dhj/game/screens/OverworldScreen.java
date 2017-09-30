package ch.dhj.game.screens;

import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
	private Sprite spritePlayerImage;

	public OverworldScreen(AssetManager assetManager, SpriteBatch batch) {
		this.assetManager = assetManager;
		this.batch = batch;

        this.assetManager.load("textures/playerImage.pack", TextureAtlas.class);
        assetManager.finishLoading();

        atlasPlayerImage = assetManager.get("textures/playerImage.pack");
        spritePlayerImage = new Sprite(atlasPlayerImage.findRegion("square-rounded-512"));

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

        Table playerProfile = new Table();
        playerProfile.setFillParent(true);
        playerProfile.bottom().left();
        playerProfile.add(new Image(spritePlayerImage)).width(150).height(150);

        Table playerStats = new Table();
        playerStats.setFillParent(true);
        playerStats.bottom().left();

        Label hp = new Label("Hp: ",skin);
        Label level = new Label("Level: ",skin);
        TextButton inventory = new TextButton("Inventory", skin);

        inventory.pad(5,20,5,20);
        inventory.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Open Inventory
            }
        });

        playerStats.add(hp);
        playerStats.row();
        playerStats.add(level);
        playerStats.row();
        playerStats.add(inventory).width(150);

        player.add(playerProfile);
        player.add(playerStats);
        stage.addActor(player);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1.0f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		// DRAW
		batch.end();

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
	}
}
