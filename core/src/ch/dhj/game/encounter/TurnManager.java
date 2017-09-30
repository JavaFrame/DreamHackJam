package ch.dhj.game.encounter;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class TurnManager {
	private Array<Turn> turns = new Array<Turn>();
	private boolean run = false;

	public TurnManager() {
	}

	public Array<Turn> getTurns() {
		return turns;
	}

	public void update() {
		if(!run) return;
		Turn t = turns.first();
		if(t.update()) {
			turns.removeIndex(0);
		}
	}

	public void start() {
		run = true;
	}

	public boolean isRun() {
		return run;
	}
}
