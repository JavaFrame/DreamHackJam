package ch.dhj.game;

import ch.dhj.game.screens.MainMenu;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DreamHackJamGame extends Game {
	
	@Override
	public void create () {
		this.setScreen(new MainMenu());
	}

	@Override
	public void render () {
	}
	
	@Override
	public void dispose () {
	}
}
