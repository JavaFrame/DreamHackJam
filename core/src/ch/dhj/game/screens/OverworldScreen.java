package ch.dhj.game.screens;

import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Enemy;
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
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import java.awt.Dialog;
import java.util.*;

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
    private final TextButton inventory;
    private final TextButton frontFieldButton;
    private final TextButton lastFieldButton;
    private final TextButton saveAndQuit;
    private EnemyManager enemyManager;
    private String encounterBackground;
    private boolean lastScreenInv;
    private static String[] dialog = new String[]{"Johnny, wake up! You need to save the world.",
            "Ohh who I am? You don’t need to know Johnny, you don’t need to know.",
            "Well, well this land looks really f*cked up. What happened?",
            "Johnny watch out there might be danger around here.",
            "Huh Johnny, why do you have such a common name? You’re a goddamn main character.",
            "Something really bad happened here.",
            "Come on we ain’t got unlimited time to save the world.",
            "You don’t want to save the world? Well you don’t have a lot of other options, right?",
            "Thinking about it, how will you save the world anyway? Magic, time travel or what are you thinking of?",
            "I sense danger ahead if I were you I would prepare myself for a beating.",
            "Well this guy had a fancy crown didn’t he? I want to know where he got that from.",
            "Can’t we turn around and get that crown? No? Sad...",
            "Man what happened here? Looks kind of like some nuclear stuff.",
            "Maybe we can find answers at one of these craters.",
            "Johnny, may I ask you how you survived whatever happened? You don’t know either? I would have liked to know it.",
            "I think we’re getting closer to the source of it all.",
            "Weird that we haven’t seen a single other survivor on our way.",
            "There is something in that crater over there let’s check it out.",
            "Maybe we’ll get answers when we arrive in the middle.",
            "We’re getting close to the potential end of an amazing story.",
            "What the f*ck is this? Come one we need to find out.",
            "Trump?! Really?! I mean there are a lot of ways for an apocalypse to happen and the writer picked Trump?! Well, who am I to judge. Hope I see you again Johnny."};

    public enum EnemyTypes {Zombie, Alien}
    private static final int SIZE = EnemyTypes.values().length;
    private static final Random RANDOM = new Random();

    public OverworldScreen(AssetManager assetManager, SpriteBatch batch, Player p, EnemyManager em, boolean lastScreenInventory) {

		this.assetManager = assetManager;
		this.batch = batch;
        player = p;
        enemyManager = em;
        lastScreenInv = lastScreenInventory;

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
		inventory  = new TextButton("Inventory", skin);
		frontFieldButton = new TextButton("Go to next Field", skin);
		lastFieldButton = new TextButton("Go to last Field", skin);
		saveAndQuit = new TextButton("Exit", skin);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.bottom().right();

        inventory.pad(5,20,5,20);

        inventory.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Open Inventory
                inventory.setTouchable(Touchable.disabled);
                frontFieldButton.setTouchable(Touchable.disabled);
                lastFieldButton.setTouchable(Touchable.disabled);
                saveAndQuit.setTouchable(Touchable.disabled);
                ((Game)Gdx.app.getApplicationListener()).setScreen(new InventoryScreen(assetManager, batch, player, enemyManager));
                pause();
            }
        });

		frontFieldButton.pad(5,20,5,20);
		lastFieldButton.pad(5,20,5,20);
		saveAndQuit.pad(5,20,5,20);

		frontFieldButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
                if(player.getObjectPosIndex() < 22) {
                    inventory.setTouchable(Touchable.disabled);
                    frontFieldButton.setTouchable(Touchable.disabled);
                    lastFieldButton.setTouchable(Touchable.disabled);
                    saveAndQuit.setTouchable(Touchable.disabled);

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
                    inventory.setTouchable(Touchable.disabled);
                    frontFieldButton.setTouchable(Touchable.disabled);
                    lastFieldButton.setTouchable(Touchable.disabled);
                    saveAndQuit.setTouchable(Touchable.disabled);
                    targetPos = corners[player.getObjectPosIndex() - 1];
                    player.setObjectPosIndex(player.getObjectPosIndex() - 1);
                    alpha = 0;

                    float distance = (float)Math.sqrt((Math.pow(playerPos.x - targetPos.x,2)) + (Math.pow(playerPos.y - targetPos.y,2)));
                    float duration = distance/20;
                    alphaAdd =(float)0.1*duration;
                    alphaAdd = 1/alphaAdd;
                }
			}
		});
		saveAndQuit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
                inventory.setTouchable(Touchable.disabled);
                frontFieldButton.setTouchable(Touchable.disabled);
                lastFieldButton.setTouchable(Touchable.disabled);
                saveAndQuit.setTouchable(Touchable.disabled);

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

        if(!lastScreenInv) {
            com.badlogic.gdx.scenes.scene2d.ui.Dialog dialog = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Story", skin);
            dialog.text(this.dialog[player.getObjectPosIndex() - 1]);
            TextButton closeB = new TextButton("close", skin);
            final com.badlogic.gdx.scenes.scene2d.ui.Dialog finalDialog = dialog;
            dialog.button("close", closeB);
            dialog.show(stage);
        }
   }

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0f, .4f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jonnyWaveAnimationTime += Gdx.graphics.getDeltaTime();
        playerImage.setDrawable(new TextureRegionDrawable((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime)));

        if(targetPos != null ){

            player.getAnimationSet().setJonnyWalkUpAnimationTime(player.getAnimationSet().getJonnyWalkUpAnimationTime() + Gdx.graphics.getDeltaTime());
            player.getAnimationSet().setJonnyWalkDownAnimationTime(player.getAnimationSet().getJonnyWalkDownAnimationTime() + Gdx.graphics.getDeltaTime());
            player.getAnimationSet().setJonnyWalkLeftAnimationTime(player.getAnimationSet().getJonnyWalkLeftAnimationTime() + Gdx.graphics.getDeltaTime());
            player.getAnimationSet().setJonnyWalkRightAnimationTime(player.getAnimationSet().getJonnyWalkRightAnimationTime() + Gdx.graphics.getDeltaTime());

            float differenzX = 0;
            float differenzY = 0;
            boolean negativX = false;
            boolean negativY = false;

            if(playerPos.x > targetPos.x){
                negativX = true;
                differenzX = playerPos.x - targetPos.x;
            } else {
                differenzX = targetPos.x - playerPos.x;
            }

            if(playerPos.y > targetPos.y){
                negativY = true;
                differenzY = playerPos.y - targetPos.y;
            } else {
                differenzY = targetPos.y - playerPos.y;
            }

            if(differenzX > differenzY){
                if(!negativX){
                    jonny = (TextureRegion) player.getAnimationSet().getWalkRightAnimation().getKeyFrame(player.getAnimationSet().getJonnyWalkRightAnimationTime());
                } else {
                    jonny = (TextureRegion) player.getAnimationSet().getWalkLeftAnimation().getKeyFrame(player.getAnimationSet().getJonnyWalkLeftAnimationTime());
                }
            } else {
                if(!negativY){
                    jonny = (TextureRegion) player.getAnimationSet().getWalkUpAnimation().getKeyFrame(player.getAnimationSet().getJonnyWalkUpAnimationTime());
                } else {
                    jonny = (TextureRegion) player.getAnimationSet().getWalkDownAnimation().getKeyFrame(player.getAnimationSet().getJonnyWalkDownAnimationTime());
                }
            }

            playerPos.interpolate(targetPos, alpha, Interpolation.pow2);
            alpha += alphaAdd * delta;

            if(playerPos.epsilonEquals(targetPos,1)){
                inventory.setTouchable(Touchable.enabled);
                frontFieldButton.setTouchable(Touchable.enabled);
                lastFieldButton.setTouchable(Touchable.enabled);
                saveAndQuit.setTouchable(Touchable.enabled);

                encounterBackground = "textures/encounter_bg.png";

                switch(player.getObjectPosIndex()){
                    case 1:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 13:
                        encounterBackground = "textures/encounter_bg_street.png";
                        break;
                    case 2:
                    case 12:
                    case 15:
                    case 18:
                    case 20:
                        encounterBackground = "textures/encounter_bg_city.png";
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        encounterBackground = "textures/encounter_bg_beach.png";
                        break;
                    case 14:
                    case 16:
                    case 17:
                    case 19:
                        encounterBackground = "textures/encounter_bg.png";
                        break;
                    case 21:
                    case 22:
                        encounterBackground = "textures/encounter_bg_crater.png";
                        break;
                }
                if(player.getObjectPosIndex() == 11){
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new EncounterScreen(player,new EncounterScreen.EncounterConfig(0,encounterBackground,"",new Enemy[]{
                            enemyManager.modifyEnemy(enemyManager.getEnemyByName("Zombie King"), player.getLevel()*2)}),assetManager,batch));
                } else if(player.getObjectPosIndex() == 22){
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new EncounterScreen(player,new EncounterScreen.EncounterConfig(0,encounterBackground,"",new Enemy[]{
                            enemyManager.modifyEnemy(enemyManager.getEnemyByName("Trump"), player.getLevel()*3)}),assetManager,batch));
                } else {
                	if(player.getObjectPosIndex() <= 4){
						int enemyCount = RANDOM.nextInt(4);
						if(enemyCount == 0){enemyCount = 1;

						Enemy[] enemies = new Enemy[enemyCount];
						int pos = 100;
						boolean up = false;
						for(int i = 0; i < enemyCount; i++){

							int enemyLevel = RANDOM.nextInt(player.getLevel() + 5);
							if(enemyLevel == 0){enemyLevel = 1;}

							String enemyName = EnemyTypes.values()[RANDOM.nextInt(SIZE)].name();
							enemies[i] = enemyManager.modifyEnemy(enemyManager.getEnemyByName(enemyName),player.getLevel());
							enemies[i].getPosition().set(pos, (up?400:100));
							pos += 200;
							up = !up;
							enemies[i].setSize(new Vector2(500, 500));
						}
						((Game)Gdx.app.getApplicationListener()).setScreen(new EncounterScreen(player,new EncounterScreen.EncounterConfig(0,encounterBackground,"",enemies),assetManager,batch));
					} else {
						int enemyCount = RANDOM.nextInt(4);
						if(enemyCount == 0){enemyCount = 1;}

						Enemy[] enemies = new Enemy[enemyCount];
						int pos = 100;
						boolean up = false;
						for(int i = 0; i < enemyCount; i++){

							int enemyLevel = RANDOM.nextInt(player.getLevel() + 5);
							if(enemyLevel == 0){enemyLevel = 1;}

							String enemyName = EnemyTypes.values()[RANDOM.nextInt(SIZE)].name();
							enemies[i] = enemyManager.modifyEnemy(enemyManager.getEnemyByName("Zombie"),player.getLevel());
							enemies[i].getPosition().set(pos, (up?400:100));
							pos += 200;
							up = !up;
							enemies[i].setSize(new Vector2(500, 500));
						}
						((Game)Gdx.app.getApplicationListener()).setScreen(new EncounterScreen(player,new EncounterScreen.EncounterConfig(0,encounterBackground,"",enemies),assetManager,batch));
					}

                }

                jonny = (TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime);
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
