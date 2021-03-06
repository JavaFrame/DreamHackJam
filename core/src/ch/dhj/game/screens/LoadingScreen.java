package ch.dhj.game.screens;

import ch.dhj.game.DreamHackJamGame;
import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.obj.objects.*;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sun.awt.windows.WEmbeddedFrame;

import java.util.Comparator;

/**
 * Created by Sebastian on 01.10.2017.
 */
public class LoadingScreen extends ScreenAdapter {
	private static final float PROGRESS_BAR_WIDTH = WorldConfig.VIEWPORT_WIDTH / 2f;
	private static final float PROGRESS_BAR_HEIGHT = 50f;

	private DreamHackJamGame game ;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private ShapeRenderer shapeRenderer;
	private EnemyManager enemyManager = new EnemyManager();
	private Player player;

	private float jonnyWalkDuration;
	private TextureAtlas walkUpAtlas;
	private TextureAtlas walkDownAtlas;
	private TextureAtlas walkLeftAtlas;
	private TextureAtlas walkRightAtlas;

	public LoadingScreen() {
		game = (DreamHackJamGame) Gdx.app.getApplicationListener();
		assetManager = game.getAssetManager();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT);
		camera.setToOrtho(false, WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT);
		Viewport viewport = new StretchViewport(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT, camera);
	}

	@Override
	public void render(float delta) {
		camera.update();
		renderProgressBar();
		if(assetManager.update()){
			TextureAtlas atlas = assetManager.get("textures/sprites.pack", TextureAtlas.class);
			buildPlayer(atlas);
			buildingEnemies(atlas);
			buildOverworldAnimations();

			game.setScreen(new MainMenu(assetManager, game.getBatch(), player, enemyManager));
		}
	}

	private void buildingEnemies(TextureAtlas atlas) {
		AnimationSet zombieSet = new AnimationSet();
		zombieSet.setEncounterWalkAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("zombie_attack")));
		zombieSet.setEncounterIdleAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("zombie_idle")));
		zombieSet.setEncounterDieAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("zombie_death")));
		zombieSet.setEncounterDamagedAnimation(new Animation<TextureRegion>((float)1/10, atlas.findRegions("zombie_damaged")));
		zombieSet.getWeaponMap().put(Weapon.WeaponType.ZombieAttack, new Animation<TextureRegion>((float)1/10, atlas.findRegions("zombie_attack")));
		Enemy zombieEnemy = new ZombieEnemy(new Vector2(100, 100), new Vector2(500, 500), "Zombie", zombieSet,1);
		zombieEnemy.setMaxLifes(5);
		zombieEnemy.setMeleeWeapon(new Weapon(Weapon.WeaponType.ZombieAttack));
		zombieEnemy.setMaxActionCount(1);
		enemyManager.addEnemy(zombieEnemy);

		AnimationSet zombieKingSet = new AnimationSet();
		zombieKingSet.setEncounterWalkAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("zombie_king_idol")));
		zombieKingSet.setEncounterIdleAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("zombie_king_idol")));
		zombieKingSet.setEncounterDieAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("zombie_king_death")));
		zombieKingSet.setEncounterDamagedAnimation(new Animation<TextureRegion>((float)1/10, atlas.findRegions("zombie_king_damaged")));
		zombieKingSet.getWeaponMap().put(Weapon.WeaponType.ZombieKingAttack, new Animation<TextureRegion>((float)1/6, atlas.findRegions("rock")));
		Enemy zombieKingEnemy = new ZombieKingEnemy(Vector2.Zero, Vector2.Zero, "Zombie King", zombieKingSet, 5);
		zombieKingEnemy.setMaxLifes(10);
		zombieKingEnemy.setLifes(10);
		zombieKingEnemy.setRangeWeapon(new Weapon(Weapon.WeaponType.ZombieKingAttack));
		zombieKingEnemy.setMaxActionCount(2);
		enemyManager.addEnemy(zombieKingEnemy);

		AnimationSet alienSet = new AnimationSet();
		alienSet.setEncounterWalkAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("alien_idle")));
		alienSet.setEncounterIdleAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("alien_idle")));
		alienSet.setEncounterDieAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("alien_death")));
		alienSet.setEncounterDamagedAnimation(new Animation<TextureRegion>((float)1/10, atlas.findRegions("alien_damged")));
		alienSet.getWeaponMap().put(Weapon.WeaponType.AlienAttack, new Animation<TextureRegion>((float)1/6, atlas.findRegions("alien_shoot")));
		alienSet.encounterIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);
		alienSet.encounterWalkAnimation.setPlayMode(Animation.PlayMode.LOOP);
		Enemy alienEnemy = new AlienEnemy(Vector2.Zero, Vector2.Zero, "Alien", alienSet,3);
		alienEnemy.setMaxLifes(10);
		alienEnemy.setLifes(10);
		alienEnemy.setRangeWeapon(new Weapon(Weapon.WeaponType.AlienAttack));
		alienEnemy.setMaxActionCount(2);
		enemyManager.addEnemy(alienEnemy);

		AnimationSet trumpSet = new AnimationSet();
		trumpSet.setEncounterWalkAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("trump_idle")));
		trumpSet.setEncounterIdleAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("trump_idle")));
		trumpSet.setEncounterDieAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("trump_death")));
		trumpSet.setEncounterDamagedAnimation(new Animation<TextureRegion>((float)1/10, atlas.findRegions("trump_damged")));
		trumpSet.getWeaponMap().put(Weapon.WeaponType.TrumpAttack, new Animation<TextureRegion>((float)1/6, atlas.findRegions("trump_twitter")));
		Enemy trumpEnemy = new TrumpEnemy(Vector2.Zero, Vector2.Zero, "Trump", trumpSet,10);
		trumpEnemy.setMaxLifes(10);
		trumpEnemy.setRangeWeapon(new Weapon(Weapon.WeaponType.TrumpAttack));
		trumpEnemy.setMaxActionCount(3);
		enemyManager.addEnemy(trumpEnemy);

		/*AnimationSet zombieKingSet = new AnimationSet();
		zombieSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_attack")));
		zombieKingSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_king_idle")));
		zombieKingSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_king_death_")));
		zombieKingSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_king_damaged")));
		zombieKingSet.getWeaponMap().put(Weapon.WeaponType.ZombieKingAttack, new Animation<TextureRegion>(1/6, atlas.findRegions("rock")));
		Enemy zombieKingEnemy = new ZombieEnemy(Vector2.Zero, Vector2.Zero, "ZombieKing", zombieKingSet);
		enemyManager.addEnemy(zombieKingEnemy);*/
	}

	private void buildPlayer(TextureAtlas atlas) {
		AnimationSet playerSet = new AnimationSet();
		playerSet.setEncounterWalkAnimation(new Animation<TextureRegion>((float)1/4, atlas.findRegions("johhny_hover")));
		playerSet.setEncounterIdleAnimation(new Animation<TextureRegion>((float)1/2, atlas.findRegions("johhny_idle")));
		playerSet.setEncounterDieAnimation(new Animation<TextureRegion>((float)1/6, atlas.findRegions("johhny_death")));
		playerSet.setEncounterDamagedAnimation(new Animation<TextureRegion>((float)1/10, atlas.findRegions("johhny_damaged")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Gun, new Animation<TextureRegion>((float) 1/3, atlas.findRegions("johhny_gun")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Shotgun, new Animation<TextureRegion>((float)1/3f, atlas.findRegions("johhny_shotgun")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Stab, new Animation<TextureRegion>((float)1/2, atlas.findRegions("johhny_stab")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Heal, new Animation<TextureRegion>((float)1/6, atlas.findRegions("johhny_heal")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Fireball, new Animation<TextureRegion>((float)1/5, atlas.findRegions("fireball")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Flamethrower, new Animation<TextureRegion>((float) 1/3, atlas.findRegions("johhny_flamethrower")));

		playerSet.encounterIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);
		playerSet.encounterWalkAnimation.setPlayMode(Animation.PlayMode.LOOP);

		player = new Player(null , new Vector2(1200, 100), new Vector2(500, 500), "Johnny", playerSet);
		player.setAnimation(playerSet.encounterIdleAnimation);
		player.getWeapons().add(new Weapon(Weapon.WeaponType.Stab).setIcon((Texture) assetManager.get("textures/Stabbything.png")));
		/*player.getWeapons().add(new Weapon(Weapon.WeaponType.Gun).setIcon((Texture) assetManager.get("textures/Gun.png")));
		player.getWeapons().add(new Weapon(Weapon.WeaponType.Heal));
		player.getWeapons().add(new Weapon(Weapon.WeaponType.Fireball));
		player.getWeapons().add(new Weapon(Weapon.WeaponType.Shotgun).setIcon((Texture) assetManager.get("textures/shotguunGun.png")));
		player.getWeapons().add(new Weapon(Weapon.WeaponType.Flamethrower).setIcon((Texture) assetManager.get("textures/Flamethrower_gun_mit_orange.png")));*/
		player.setEnemyManager(enemyManager);
		player.setMaxLifes(10);
		player.setLifes(10);
		player.setMaxActionCount(2);
		player.setLevel(1);
		player.setTotalExpToNextLevel(10);
		player.setMeleeWeapon(new Weapon(Weapon.WeaponType.Stab).setIcon((Texture) assetManager.get("textures/Stabbything.png")));
	}
	@Override
	public void show() {
		super.show();
		assetManager.load("textures/sprites.pack", TextureAtlas.class);
		assetManager.load("textures/Gun.png", Texture.class);
		assetManager.load("textures/shotguunGun.png", Texture.class);
		assetManager.load("textures/Stabbything.png", Texture.class);
		assetManager.load("textures/Flamethrower_gun_mit_orange.png", Texture.class);
		assetManager.load("textures/walkUp.pack", TextureAtlas.class);
		assetManager.load("textures/walkDown.pack", TextureAtlas.class);
		assetManager.load("textures/walkLeft.pack", TextureAtlas.class);
		assetManager.load("textures/walkRight.pack", TextureAtlas.class);
	}

	private void renderProgressBar() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		float progress = assetManager.getProgress();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(
				(WorldConfig.VIEWPORT_WIDTH - PROGRESS_BAR_WIDTH) / 2f,
				(WorldConfig.VIEWPORT_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f,
				PROGRESS_BAR_WIDTH * progress,
				PROGRESS_BAR_HEIGHT
		);
		shapeRenderer.end();
	}

	private void buildOverworldAnimations(){

		walkUpAtlas = assetManager.get("textures/walkUp.pack");
		walkDownAtlas = assetManager.get("textures/walkDown.pack");
		walkLeftAtlas = assetManager.get("textures/walkLeft.pack");
		walkRightAtlas = assetManager.get("textures/walkRight.pack");

		jonnyWalkDuration = 1.0f / 2f;

		Array<TextureAtlas.AtlasRegion> jonnyWalkUpRegions = new Array<TextureAtlas.AtlasRegion>(walkUpAtlas.getRegions());
		jonnyWalkUpRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		player.getAnimationSet().setWalkUpAnimation(new Animation(jonnyWalkDuration, jonnyWalkUpRegions, Animation.PlayMode.LOOP));
		player.getAnimationSet().setJonnyWalkUpAnimationTime(Gdx.graphics.getDeltaTime());

		Array<TextureAtlas.AtlasRegion> jonnyWalkDownRegions = new Array<TextureAtlas.AtlasRegion>(walkDownAtlas.getRegions());
		jonnyWalkDownRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		player.getAnimationSet().setWalkDownAnimation(new Animation(jonnyWalkDuration, jonnyWalkDownRegions, Animation.PlayMode.LOOP));
		player.getAnimationSet().setJonnyWalkDownAnimationTime(Gdx.graphics.getDeltaTime());

		Array<TextureAtlas.AtlasRegion> jonnyWalkLeftRegions = new Array<TextureAtlas.AtlasRegion>(walkLeftAtlas.getRegions());
		jonnyWalkLeftRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		player.getAnimationSet().setWalkLeftAnimation(new Animation(jonnyWalkDuration, jonnyWalkLeftRegions, Animation.PlayMode.LOOP));
		player.getAnimationSet().setJonnyWalkLeftAnimationTime(Gdx.graphics.getDeltaTime());

		Array<TextureAtlas.AtlasRegion> jonnyWalkRightRegions = new Array<TextureAtlas.AtlasRegion>(walkRightAtlas.getRegions());
		jonnyWalkRightRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		player.getAnimationSet().setWalkRightAnimation(new Animation(jonnyWalkDuration, jonnyWalkRightRegions, Animation.PlayMode.LOOP));
		player.getAnimationSet().setJonnyWalkRightAnimationTime(Gdx.graphics.getDeltaTime());
	}
}
