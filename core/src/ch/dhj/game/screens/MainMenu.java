package ch.dhj.game.screens;

import ch.dhj.game.obj.WorldConfig;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlasButtons;
    private TextureAtlas atlasBackground;
    protected Skin skin;
    private Sprite background;

    public MainMenu() {
        atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
        skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);
        atlasBackground = new TextureAtlas("textures/atlasMainMenu.pack");

        background = new Sprite(atlasBackground.findRegion("Placeholder_titel"));

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FillViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT,camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
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
                ((Game)Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(null));
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        mainTable.add(playButton);
        mainTable.row().pad(10,100,10,100);
        mainTable.add(exitButton);
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
