package ch.dhj.game;

import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Enemy;
import ch.dhj.game.player.Player;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.player.spells.TestHealSpell;
import ch.dhj.game.player.spells.TestSpell;
import ch.dhj.game.player.weapons.TestWeapon;
import ch.dhj.game.screens.MainMenu;
import ch.dhj.game.screens.EncounterScreen;
import ch.dhj.game.screens.OverworldScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DreamHackJamGame extends Game {

	private AssetManager assetManager;
	private SpriteBatch batch;

	@Override
	public void create () {
		this.setScreen(new MainMenu());
		assetManager = new AssetManager();
		batch = new SpriteBatch();


		List<Weapon> weapons = new ArrayList(Arrays.asList(new TestWeapon(), new TestSpell(), new TestHealSpell()));
		Player p = new Player("Joshua", -999, 10, 1000, 3, 10, 20, weapons, new AnimationSet());
		p.setCurrentWeapon(weapons.get(0));
		p.getSpells().add(weapons.get(1));
		p.getSpells().add(weapons.get(2));
		p.setCurrentWeapon(null);

		this.setScreen(
				new EncounterScreen(p,
						new EncounterScreen.EncounterConfig(0, "textures/encounter_bg.png", "map/test.tmx", new Enemy[]{}),
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
