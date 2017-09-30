package ch.dhj.game.player;

import com.badlogic.gdx.math.Vector2;

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
	private int maxActionCount;
	private int lifes;
	private int maxLifes;
	private List<Weapon> weapons;
	private Weapon currentWeapon;
	private List<Weapon> spells = new ArrayList<Weapon>(3);
	//private List<Figure> allies;
	private List<String> changes = new ArrayList<String>();
	private AnimationSet animationSet;

	private Vector2 position;

	public Player(String name, int level, int exp, int totalExpToNextLevel, int actionCount, int lifes, int maxLifes, List<Weapon> weapons, AnimationSet animationSet) {
		this.name = name;
		this.level = level;
		this.exp = exp;
		this.totalExpToNextLevel = totalExpToNextLevel;
		this.maxActionCount = actionCount;
		this.lifes = lifes;
		this.maxLifes = maxLifes;
		this.weapons = weapons;
		this.animationSet = animationSet;

		this.position = new Vector2(0, 0);
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

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public void setCurrentWeapon(Weapon currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	public List<Weapon> getSpells() {
		return spells;
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

			int oldActionCount = getMaxActionCount();
			setMaxActionCount((int) (getMaxActionCount() * ACTIONS_FACTOR));
			if(getMaxActionCount() > MAX_ACTIONS)
				setMaxActionCount(MAX_ACTIONS);
			changes.add(String.format("%i ap -> %i ap", oldActionCount, getMaxActionCount()));
		}
	}

	public int getTotalExpToNextLevel() {
		return totalExpToNextLevel;
	}

	public void setTotalExpToNextLevel(int totalExpToNextLevel) {
		this.totalExpToNextLevel = totalExpToNextLevel;
	}

	public int getMaxActionCount() {
		return maxActionCount;
	}

	public void setMaxActionCount(int maxActionCount) {
		this.maxActionCount = maxActionCount;
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

	public boolean isDead() {
		return getLifes() <= 0;
	}

	public AnimationSet getAnimationSet() {
		return animationSet;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
}
