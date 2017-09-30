package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Figure;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class WeaponAction implements Action {
	@Override
	public void init(Figure you, Figure enemy) {

	}

	@Override
	public boolean action(Figure you, Figure enemy) {
		return true;
	}
}
