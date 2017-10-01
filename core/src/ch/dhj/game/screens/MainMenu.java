package ch.dhj.game.screens;

import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

public class MainMenu implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlasButtons;
    private TextureAtlas atlasBackground;
    protected Skin skin;
    private Sprite background;
    private AssetManager assetManager;
    private Player player;
    private EnemyManager enemyManager;

    public MainMenu(AssetManager assetManager, SpriteBatch batch, Player p, EnemyManager em) {
        this.assetManager = assetManager;
        this.batch = batch;
        player = p;
        enemyManager = em;

        this.assetManager.load("textures/defaultSkin.pack", TextureAtlas.class);
        this.assetManager.load("textures/atlasMainMenu.pack", TextureAtlas.class);
        this.assetManager.finishLoading();

        atlasButtons = this.assetManager.get("textures/defaultSkin.pack");
        skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);
        atlasBackground = this.assetManager.get("textures/atlasMainMenu.pack");

        background = new Sprite(atlasBackground.findRegion("Title"));
        background.setSize(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT,camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, this.batch);
    }

    @Override
    public void show() {
        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);

        //Create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.pad(10,100,10,100);
        exitButton.pad(10,100,10,100);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(assetManager, batch, player, enemyManager, false));
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        mainTable.add(playButton).width(250);
        mainTable.row().pad(10,100,10,100);
        mainTable.add(exitButton).width(250);
        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        background.draw(batch);
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
        atlasBackground.dispose();
        atlasButtons.dispose();
        batch.dispose();
        skin.dispose();
        stage.dispose();
    }}
