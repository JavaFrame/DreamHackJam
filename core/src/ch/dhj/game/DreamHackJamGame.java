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
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DreamHackJamGame extends Game {

	private AssetManager assetManager;
	private SpriteBatch batch;

	@Override
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("textures/johhny.atlas", TextureAtlas.class);
		batch = new SpriteBatch();
		assetManager.finishLoading();

		TextureAtlas atlas = assetManager.get("textures/johhny.atlas");

		AnimationSet masterAnimationSet = new AnimationSet(
			null,
				null,
				null,
				null,
				null,
				new Animation<TextureRegion>(1/10, atlas.findRegions("johhny_stab")),
				new Weapon.WeaponType[]{Weapon.WeaponType.Gun, Weapon.WeaponType.Shotgun, Weapon.WeaponType.Stab, Weapon.WeaponType.Heal},
				new Animation[]{new Animation(1/6, atlas.findRegions("johhny_gun")),
						new Animation(1/6, atlas.findRegions("johhny_shotgun")),
						new Animation(1/2, atlas.findRegions("johhny_stab")),
						new Animation(1/6, atlas.findRegions("johhny_heal")),
			}
		);

		AnimationSet playerAnimationSet = new AnimationSet(masterAnimationSet);
		List<Weapon> weapons = new ArrayList(Arrays.asList(new Weapon(Weapon.WeaponType.Gun), new Weapon(Weapon.WeaponType.Shotgun), new Weapon(Weapon.WeaponType.Stab), new Weapon(Weapon.WeaponType.Heal)));
		Player p = new Player(new Sprite(atlas.findRegion("johnny_gun",0)), new Vector2(1300, 200), new Vector2(500, 500), "Joshua", new AnimationSet(masterAnimationSet));
		p.setMaxActionCount(2);
		p.setCurrentWeapon(weapons.get(2));
		p.getSpells().add(weapons.get(3));
		p.setMaxLifes(10);
		p.setLifes(10);

		//this.setScreen(new MainMenu(assetManager, batch));
		Enemy tE = new Enemy(new Vector2(100, 300), new Vector2(500, 500), "BadBoy 3", new AnimationSet(masterAnimationSet)) {
			@Override
			public Action[] getActions() {
				return new Action[0];
			}
		};
		tE.setMaxLifes(3);
		tE.setLifes(3);
		tE.setLevel(3);

		this.setScreen(
				new EncounterScreen(p, new EncounterScreen.EncounterConfig(0, "textures/encounter_bg.png", "map/test.tmx",
						new Enemy[]{tE, new Enemy(
								new Vector2(300, 200),
								new Vector2(500, 500),
								"BadBoy2",
								new AnimationSet(masterAnimationSet)) {
							@Override
							public Action[] getActions() {
								return new Action[0];
							}
						}}),
						assetManager,
						batch));
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
