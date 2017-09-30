package ch.dhj.game.encounter;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class TurnManager {
	private Array<Turn> turns = new Array<>();
	private boolean run = false;

	private Array<TurnManagerListener> roundDoneListeners = new Array<>();

	public TurnManager() {
	}

	public Array<Turn> getTurns() {
		return turns;
	}

	public void addRoundDoneListener(TurnManagerListener l) {
		roundDoneListeners.add(l);
	}

	public void update() {
		if(!isRun()) return;
		if(turns.size == 0) {
			for(TurnManagerListener l : roundDoneListeners)
				l.triggered(this);
			run = false;
			return;
		}
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

	public static interface TurnManagerListener {
		void triggered(TurnManager manager);
	}
}
