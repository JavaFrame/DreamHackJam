package ch.dhj.game.encounter;

import java.util.LinkedList;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Turn {
	private LinkedList<Action> actions = new LinkedList();
	private int index = 0;
	private boolean started = false;

	/**
	 * Adds an Action to this turn.
	 * @param a the action
	 */
	public void addAction(Action a) {
		actions.add(a);
	}

	public void startTurn() {
		started = true;
	}

	/**
	 * Calls {@link Action#action()} of the current {@link Action} and returns true if the turn is done.
	 * @return true if the turn is done else false. (It also returns false if the turn hasn't started yet)
	 */
	public boolean update() {
		if(!started) return false;
		Action a = actions.get(index);
		if(a.action())
			index++;
		return actions.size() == index+1; //+1 because index start at 0 and actions.size() at 1
	}

	/**
	 * Returns if the Turn already started or not.
	 */
	public boolean isStarted() {
		return started;
	}
}
