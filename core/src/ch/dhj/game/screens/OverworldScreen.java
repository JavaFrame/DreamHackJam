package ch.dhj.game.screens;

import ch.dhj.game.obj.RootObject;
import ch.dhj.game.obj.WorldConfig;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 29.09.2017.
 */
public class OverworldScreen implements Screen {
	private RootObject rootObject;

	public OverworldScreen(AssetManager assetManager) {
		rootObject = new RootObject(assetManager, new WorldConfig(new Vector2(0, 0)), "map/Test.tmx");
		rootObject.init();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		rootObject.render(delta);
	}

	@Override
	public void resize(int width, int height) {

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
		rootObject.dispose();
	}
}
