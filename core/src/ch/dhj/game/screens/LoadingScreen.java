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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

/**
 * Created by Sebastian on 01.10.2017.
 */
public class LoadingScreen extends ScreenAdapter {
	private static final float PROGRESS_BAR_WIDTH = WorldConfig.VIEWPORT_WIDTH / 2f;
	private static final float PROGRESS_BAR_HEIGHT = 50f;

	private DreamHackJamGame game ;
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
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		renderProgressBar();
		if(assetManager.update()){
			TextureAtlas atlas = assetManager.get("textures/sprites.pack", TextureAtlas.class);
			buildPlayer(atlas);
			buildingEnemies(atlas);
			buildOverworldAnimations();
			game.setScreen(new MainMenu(assetManager, game.getBatch(), player));
		}
	}

	private void buildingEnemies(TextureAtlas atlas) {
		AnimationSet zombieSet = new AnimationSet();
		zombieSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_attack")));
		zombieSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_idle")));
		zombieSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_death")));
		zombieSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_damaged")));
		zombieSet.getWeaponMap().put(Weapon.WeaponType.ZombieAttack, new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_attack")));
		Enemy zombieEnemy = new ZombieEnemy(Vector2.Zero, Vector2.Zero, "Zombie", zombieSet);
		enemyManager.addEnemy(zombieEnemy);

		AnimationSet zombieKingSet = new AnimationSet();
		zombieKingSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("rock")));
		zombieKingSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_king_idol")));
		zombieKingSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_king_death")));
		zombieKingSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_king_damaged")));
		zombieKingSet.getWeaponMap().put(Weapon.WeaponType.ZombieKingAttack, new Animation<TextureRegion>(1/6, atlas.findRegions("rock")));
		Enemy zombieKingEnemy = new ZombieKingEnemy(Vector2.Zero, Vector2.Zero, "Zombie King", zombieKingSet);
		enemyManager.addEnemy(zombieKingEnemy);

		AnimationSet alienSet = new AnimationSet();
		alienSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("alien_attack")));
		alienSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("alien_idle")));
		alienSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("alien_death")));
		alienSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("alien_damaged")));
		alienSet.getWeaponMap().put(Weapon.WeaponType.AlienAttack, new Animation<TextureRegion>(1/6, atlas.findRegions("alien_attack")));
		Enemy alienEnemy = new AlienEnemy(Vector2.Zero, Vector2.Zero, "Alien", alienSet);
		enemyManager.addEnemy(alienEnemy);

		AnimationSet trumpSet = new AnimationSet();
		trumpSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("trump_attack")));
		trumpSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("trump_idle")));
		trumpSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("trump_death")));
		trumpSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("trump_damaged")));
		trumpSet.getWeaponMap().put(Weapon.WeaponType.TrumpAttack, new Animation<TextureRegion>(1/6, atlas.findRegions("trump_attack")));
		Enemy trumpEnemy = new TrumpEnemy(Vector2.Zero, Vector2.Zero, "Trump", trumpSet);
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
		playerSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_idle")));
		playerSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_death")));
		playerSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_damaged")));
		playerSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegion("johhny_hover")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Gun, new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_gun")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Shotgun, new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_shotgun")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Stab, new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_stab")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Heal, new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_heal")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Flamethrower, new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_flamethrower")));
		player = new Player(null, new Vector2(100,1200), new Vector2(500, 500), "Johhny", playerSet);
	}
	@Override
	public void show() {
		super.show();
		assetManager.load("textures/sprites.pack", TextureAtlas.class);
		assetManager.load("textures/walkUp.pack", TextureAtlas.class);
		assetManager.load("textures/walkDown.pack", TextureAtlas.class);
		assetManager.load("textures/walkLeft.pack", TextureAtlas.class);
		assetManager.load("textures/walkRight.pack", TextureAtlas.class);
	}

	private void renderProgressBar() {
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
