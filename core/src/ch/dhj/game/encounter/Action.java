package ch.dhj.game.encounter;

import ch.dhj.game.player.Player;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public interface Action {
	void init(Player you, Player enemy);

	/**
	 * Will be called every update call of the Turn until it returns true.
	 * @return Returns if the action is done or not
	 */
	boolean action(Player you, Player enemy);

	/**
	 * Returns the current frame when it is null it doen't gets applied.
	 */
	TextureRegion getFrame(Player you, Player enemy);
}
