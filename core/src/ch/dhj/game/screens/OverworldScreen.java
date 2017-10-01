package ch.dhj.game.screens;

import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import java.util.Map;

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
    private Vector2[] corners;
    private Vector2 playerPos;
    private Vector2 targetPos;
    private float alpha = 0;
    private float alphaAdd;
    private TextureRegion jonny;

    public OverworldScreen(AssetManager assetManager, SpriteBatch batch, Player p) {
		this.assetManager = assetManager;
		this.batch = batch;
        player = p;

        this.assetManager.load("textures/jonnySprite.pack", TextureAtlas.class);
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.assetManager.load("map/Aktuelle_overworld.tmx", TiledMap.class);
        assetManager.finishLoading();

        map = assetManager.get("map/Aktuelle_overworld.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 3.5f);

        corners = new Vector2[map.getLayers().getCount()];

        for(int i = 0; i < map.getLayers().getCount(); i++){
            if(i != 0) {
                MapObject object = map.getLayers().get(i).getObjects().getByType(RectangleMapObject.class).first();
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                corners[i] = new Vector2((rect.x + rect.width / 2)*3.5f, (rect.y + rect.height / 2)*3.5f);
            }
        }

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

        jonny = (TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime);
        playerPos = corners[player.getObjectPosIndex()];

        atlasButtons = assetManager.get("textures/defaultSkin.pack");
		skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		camera = new OrthographicCamera();
		viewport = new StretchViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT,camera);
		viewport.apply();

		camera.update();

		stage = new Stage(new StretchViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT), this.batch);
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
                if(player.getObjectPosIndex() < 22) {
                    targetPos = corners[player.getObjectPosIndex() + 1];
                    player.setObjectPosIndex(player.getObjectPosIndex() + 1);
                    alpha = 0;

                    float distance = (float)Math.sqrt((Math.pow(playerPos.x - targetPos.x,2)) + (Math.pow(playerPos.y - targetPos.y,2)));
                    float duration = distance/20;
                    alphaAdd =(float)0.1*duration;
                    alphaAdd = 1/alphaAdd;
                }
			}
		});

		lastFieldButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
                if(player.getObjectPosIndex() > 1) {
                    targetPos = corners[player.getObjectPosIndex() - 1];
                    player.setObjectPosIndex(player.getObjectPosIndex() - 1);
                    alpha = 0;
                }
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

		final Table playerGUI = new Table();
        playerGUI.bottom().left();

        playerImage = new Image((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime));

        Table playerProfile = new Table();
        playerProfile.setFillParent(true);
        playerProfile.bottom().left();
        playerProfile.add(playerImage).width(150).height(150);

        final Table playerStats = new Table();
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
                ((Game)Gdx.app.getApplicationListener()).setScreen(new InventoryScreen(assetManager, batch, player));
                pause();
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
        playerInv.bottom().left();

        playerInv.add(playerStats);
        playerInv.row();
        playerInv.add(inventory).width(150);

        playerGUI.add(playerProfile);
        playerGUI.add(playerInv);

        Pixmap lightGrayBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        lightGrayBackground.setColor(Color.GRAY);
        lightGrayBackground.fill();
        playerGUI.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGrayBackground))));
        playerGUI.padRight(20);
        playerGUI.pack();
        stage.addActor(playerGUI);

   }

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0f, .4f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jonnyWaveAnimationTime += Gdx.graphics.getDeltaTime();
        playerImage.setDrawable(new TextureRegionDrawable((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime)));

        if(targetPos != null ){
            playerPos.interpolate(targetPos, alpha, Interpolation.pow2);
            alpha += alphaAdd * delta;
            if(playerPos.epsilonEquals(targetPos,1)){
                targetPos = null;
                alpha = 0;
            }
        }

        camera.position.set(playerPos.x , playerPos.y, 0);

        mapRenderer.render();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(jonny, playerPos.x, playerPos.y, 8*3.5f,8*3.5f);
        batch.end();

		stage.act();
		stage.draw();
		camera.update();

        mapRenderer.setView(camera);
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
