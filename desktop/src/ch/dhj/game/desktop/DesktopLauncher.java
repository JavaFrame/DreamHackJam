package ch.dhj.game.desktop;

import ch.dhj.game.obj.WorldConfig;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.dhj.game.DreamHackJamGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WorldConfig.VIEWPORT_WIDTH;
		config.height = WorldConfig.VIEWPORT_HEIGHT;
		new LwjglApplication(new DreamHackJamGame(), config);
	}
}
