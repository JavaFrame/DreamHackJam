package ch.dhj.game.encounter;

import java.util.LinkedList;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Turn {
	private LinkedList<Action> actions = new LinkedList();
	private int index = 0;

	/**
	 * Adds an Action to this turn.
	 * @param a the action
	 */
	public void addAction(Action a) {
		actions.add(a);
	}


	/**
	 * Calls {@link Action#action()} of the current {@link Action} and returns true if the turn is done.
	 * @return true if the turn is done else false. (It also returns false if the turn hasn't started yet)
	 */
	public boolean update() {
		Action a = actions.get(index);
		if(a.action())
			index++;
		return actions.size() == index;
	}

}
