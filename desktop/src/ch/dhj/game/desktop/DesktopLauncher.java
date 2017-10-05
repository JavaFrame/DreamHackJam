package ch.dhj.game.desktop;

import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.dhj.game.DreamHackJamGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WorldConfig.VIEWPORT_WIDTH;
		config.height = WorldConfig.VIEWPORT_HEIGHT;
		config.fullscreen = false;
		config.addIcon("textures/icon_game.png", Files.FileType.Internal);
		new LwjglApplication(new DreamHackJamGame(), config);
	}
}
