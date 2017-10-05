package ch.dhj.game;

import android.os.Bundle;

import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ch.dhj.game.DreamHackJamGame;
import com.badlogic.gdx.backends.android.surfaceview.RatioResolutionStrategy;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.resolutionStrategy = new RatioResolutionStrategy(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT);
		initialize(new DreamHackJamGame(), config);
	}
}
