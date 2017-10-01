package ch.dhj.game.screens;

import ch.dhj.game.DreamHackJamGame;
import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.encounter.obj.objects.ZombieEnemy;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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
			TextureAtlas atlas = assetManager.get("textures/sprites.atlas", TextureAtlas.class);
			buildPlayer(atlas);
			buildingEnemies(atlas);

			game.setScreen(new EncounterScreen(player, new EncounterScreen.EncounterConfig(0, "textures/encounter_bg.png", "map/test.tmx", new Enemy[]{
				enemyManager.modifyEnemy(enemyManager.getEnemyByName("Zombie"), 2)
			}), assetManager, new SpriteBatch()));
		}
	}

	private void buildingEnemies(TextureAtlas atlas) {
		AnimationSet zombieSet = new AnimationSet();
		zombieSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_attack")));
		zombieSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_idle")));
		zombieSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_death")));
		zombieSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_damaged")));
		zombieSet.getWeaponMap().put(Weapon.WeaponType.ZombieAttack, new Animation<TextureRegion>(1/6, atlas.findRegions("zombie_attack")));
		Enemy zombieEnemy = new ZombieEnemy(new Vector2(100, 100), new Vector2(500, 500), "Zombie", zombieSet);
		zombieEnemy.setMaxLifes(10);
		zombieEnemy.setLifes(10);
		enemyManager.addEnemy(zombieEnemy);

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
		playerSet.setEncounterWalkAnimation(new Animation<TextureRegion>(1/4, atlas.findRegions("johhny_hover")));
		playerSet.setEncounterIdleAnimation(new Animation<TextureRegion>(1/2, atlas.findRegions("johhny_idle")));
		playerSet.setEncounterDieAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_death")));
		playerSet.setEncounterDamagedAnimation(new Animation<TextureRegion>(1/6, atlas.findRegions("johhny_damaged")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Gun, new Animation<TextureRegion>(1, atlas.findRegions("johhny_gun")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Shotgun, new Animation<TextureRegion>(1/2, atlas.findRegions("johhny_shotgun")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Stab, new Animation<TextureRegion>(1/2, atlas.findRegions("johhny_stab")));
		playerSet.getWeaponMap().put(Weapon.WeaponType.Heal, new Animation<TextureRegion>(1/2, atlas.findRegions("johhny_heal")));
		playerSet.encounterIdleAnimation.setPlayMode(Animation.PlayMode.LOOP);
		playerSet.encounterWalkAnimation.setPlayMode(Animation.PlayMode.LOOP);

		player = new Player(null , new Vector2(1200, 100), new Vector2(500, 500), "Johhny", playerSet);
		player.setMaxLifes(10);
		player.setLifes(10);
		player.setMaxActionCount(3);
		player.setMeleeWeapon(new Weapon(Weapon.WeaponType.Stab));
		player.setAnimation(playerSet.encounterIdleAnimation);
	}
	@Override
	public void show() {
		super.show();
		assetManager.load("textures/sprites.atlas", TextureAtlas.class);

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
}
