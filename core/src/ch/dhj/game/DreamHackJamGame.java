package ch.dhj.game;

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

public class DreamHackJamGame extends Game {

	private AssetManager assetManager;
	private SpriteBatch batch;

	@Override
	public void create () {
		this.setScreen(new MainMenu());
		assetManager = new AssetManager();
		batch = new SpriteBatch();
		this.setScreen(new EncounterScreen(0, assetManager, batch));
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
