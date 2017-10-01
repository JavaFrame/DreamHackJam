package ch.dhj.game;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.player.spells.TestHealSpell;
import ch.dhj.game.player.spells.TestSpell;
import ch.dhj.game.player.weapons.TestWeapon;
import ch.dhj.game.screens.EncounterScreen;
import ch.dhj.game.screens.MainMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DreamHackJamGame extends Game {

	private AssetManager assetManager;
	private SpriteBatch batch;
	private float jonnyWalkDuration;
	private TextureAtlas walkUpAtlas;
	private TextureAtlas walkDownAtlas;
	private TextureAtlas walkLeftAtlas;
	private TextureAtlas walkRightAtlas;

	@Override
	public void create () {
		assetManager = new AssetManager();
		batch = new SpriteBatch();

		List<Weapon> weapons = new ArrayList(Arrays.asList(new TestWeapon(), new TestSpell(), new TestHealSpell()));
		Player p = new Player(null, new Vector2(1200, 100), new Vector2(500, 500), "Johhy", new AnimationSet());
		p.setMaxActionCount(2);
		p.setCurrentWeapon(weapons.get(0));
		p.getSpells().add(weapons.get(1));
		p.getSpells().add(weapons.get(2));

		this.assetManager.load("textures/walkUp.pack", TextureAtlas.class);
		this.assetManager.load("textures/walkDown.pack", TextureAtlas.class);
		this.assetManager.load("textures/walkLeft.pack", TextureAtlas.class);
		this.assetManager.load("textures/walkRight.pack", TextureAtlas.class);
		assetManager.finishLoading();

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
		p.getAnimationSet().setWalkUpAnimation(new Animation(jonnyWalkDuration, jonnyWalkUpRegions, Animation.PlayMode.LOOP));
		p.getAnimationSet().setJonnyWalkUpAnimationTime(Gdx.graphics.getDeltaTime());

		Array<TextureAtlas.AtlasRegion> jonnyWalkDownRegions = new Array<TextureAtlas.AtlasRegion>(walkDownAtlas.getRegions());
		jonnyWalkDownRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		p.getAnimationSet().setWalkDownAnimation(new Animation(jonnyWalkDuration, jonnyWalkDownRegions, Animation.PlayMode.LOOP));
		p.getAnimationSet().setJonnyWalkDownAnimationTime(Gdx.graphics.getDeltaTime());

		Array<TextureAtlas.AtlasRegion> jonnyWalkLeftRegions = new Array<TextureAtlas.AtlasRegion>(walkLeftAtlas.getRegions());
		jonnyWalkLeftRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		p.getAnimationSet().setWalkLeftAnimation(new Animation(jonnyWalkDuration, jonnyWalkLeftRegions, Animation.PlayMode.LOOP));
		p.getAnimationSet().setJonnyWalkLeftAnimationTime(Gdx.graphics.getDeltaTime());

		Array<TextureAtlas.AtlasRegion> jonnyWalkRightRegions = new Array<TextureAtlas.AtlasRegion>(walkRightAtlas.getRegions());
		jonnyWalkRightRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
			@Override
			public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		p.getAnimationSet().setWalkRightAnimation(new Animation(jonnyWalkDuration, jonnyWalkRightRegions, Animation.PlayMode.LOOP));
		p.getAnimationSet().setJonnyWalkRightAnimationTime(Gdx.graphics.getDeltaTime());

		this.setScreen(new MainMenu(assetManager, batch, p));
		/*this.setScreen(
				new EncounterScreen(p, new EncounterScreen.EncounterConfig(0, "textures/encounter_bg.png", "map/test.tmx",
						new Enemy[]{new Enemy(
								new Vector2(100, 100),
								"BadBoy",
								new AnimationSet()) {
					@Override
					public Action[] getActions() {
						return new Action[0];
					}
				}, new Enemy(
								new Vector2(100, 100),
								"BadBoy2",
								new AnimationSet()) {
							@Override
							public Action[] getActions() {
								return new Action[0];
							}
						}}),
						assetManager,
						batch));*/
	}

	@Override
	public void render () {
		super.render();
		assetManager.update();
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
	}
}
