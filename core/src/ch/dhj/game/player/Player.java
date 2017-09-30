package ch.dhj.game.player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Player {
	public static final float EXP_FACTOR = 1.5f;
	public static final float LIFE_FACTOR = 1.5f;
	public static final float ACTIONS_FACTOR = 2f;
	public static final int MAX_ACTIONS = 10;

	private String name;
	private int level;
	private int exp;
	private int totalExpToNextLevel;
	private int actionCount;
	private int lifes;
	private int maxLifes;
	private List<Weapon> weapons;
	//private List<Player> allies;
	private List<String> changes = new ArrayList<String>();


	public Player(String name, int level, int exp, int totalExpToNextLevel, int actionCount, int lifes, int maxLifes, List<Weapon> weapons) {
		this.name = name;
		this.level = level;
		this.exp = exp;
		this.totalExpToNextLevel = totalExpToNextLevel;
		this.actionCount = actionCount;
		this.lifes = lifes;
		this.maxLifes = maxLifes;
		this.weapons = weapons;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void addExp(int exp) {
		setExp(getExp() + exp);
		if(getExp() >= getTotalExpToNextLevel()) {
			changes.clear();
			setExp(0);
			setTotalExpToNextLevel((int) (getTotalExpToNextLevel() * EXP_FACTOR));
			setLevel(getLevel() + 1);
			changes.add(String.format("%i lvl -> %i lvl", getLevel()-1, getLevel()));

			int oldMaxLife = getMaxLifes();
			setMaxLifes((int) (getMaxLifes() * LIFE_FACTOR));
			setLifes(getMaxLifes());
			changes.add(String.format("%i lifes -> %i lifes", oldMaxLife, getMaxLifes()));

			int oldActionCount = getActionCount();
			setActionCount((int) (getActionCount() * ACTIONS_FACTOR));
			if(getActionCount() > MAX_ACTIONS)
				setActionCount(MAX_ACTIONS);
			changes.add(String.format("%i ap -> %i ap", oldActionCount, getActionCount()));
		}
	}

	public int getTotalExpToNextLevel() {
		return totalExpToNextLevel;
	}

	public void setTotalExpToNextLevel(int totalExpToNextLevel) {
		this.totalExpToNextLevel = totalExpToNextLevel;
	}

	public int getActionCount() {
		return actionCount;
	}

	public void setActionCount(int actionCount) {
		this.actionCount = actionCount;
	}

	public int getLifes() {
		return lifes;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}

	public int applayDamage(int  damage) {
		return getLifes() - damage;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public int getMaxLifes() {
		return maxLifes;
	}

	public void setMaxLifes(int maxLifes) {
		this.maxLifes = maxLifes;
	}
}
