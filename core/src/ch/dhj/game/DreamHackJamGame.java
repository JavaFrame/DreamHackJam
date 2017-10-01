package ch.dhj.game;

import ch.dhj.game.screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DreamHackJamGame extends Game {

	private AssetManager assetManager;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		assetManager = new AssetManager();
		batch = new SpriteBatch();

		this.setScreen(new LoadingScreen());
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

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}
}
