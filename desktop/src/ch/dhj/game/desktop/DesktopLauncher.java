package ch.dhj.game.desktop;

import ch.dhj.game.obj.WorldConfig;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.dhj.game.DreamHackJamGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WorldConfig.VIEWPORT_WIDTH*3;
		config.height = WorldConfig.VIEWPORT_HEIGHT*3;
		new LwjglApplication(new DreamHackJamGame(), config);
	}
}
