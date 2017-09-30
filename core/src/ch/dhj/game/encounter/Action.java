package ch.dhj.game.encounter;

import ch.dhj.game.encounter.obj.objects.Figure;
import ch.dhj.game.player.Player;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public interface Action {
	void init(Figure you, Figure enemy);

	/**
	 * Will be called every update call of the Turn until it returns true.
	 * @return Returns if the action is done or not
	 */
	boolean action(Figure you, Figure enemy);
}
