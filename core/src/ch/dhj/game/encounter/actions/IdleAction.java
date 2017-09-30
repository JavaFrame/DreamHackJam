package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.player.Player;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class IdleAction implements Action {

	@Override
	public boolean action(Player you, Player enemy) {
		return true;
	}

	@Override
	public TextureRegion getFrame() {
		return null;
	}
}
