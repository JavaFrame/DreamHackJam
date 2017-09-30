package ch.dhj.game.encounter;

import ch.dhj.game.encounter.obj.objects.Figure;

/**
 * Created by Sebastian on 30.09.2017.
 */
public interface Action {

	/**
	 * Will be called every update call of the Turn until it returns true.
	 * @return Returns if the action is done or not
	 */
	boolean action();
}
