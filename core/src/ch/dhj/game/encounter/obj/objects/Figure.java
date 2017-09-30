package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.obj.GObject;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import javafx.animation.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Figure extends GObject {
	private String name;
	private int level;
	private int exp;
	private int totalExpToNextLevel;
	private int maxActionCount;
	private int lifes;
	private int maxLifes;
	private Array<Weapon> weapons = new Array<>();
	private Weapon currentWeapon;
	private Array<Weapon> spells = new Array<>(3);
	private AnimationSet animationSet;

	public Figure(Vector2 position, String name, AnimationSet animationSet) {
		super(position);
		this.name = name;
		this.animationSet = animationSet;
	}

	@Override
	public void init() {

	}

	@Override
	public void render(float delta) {
		getSprite().draw(getBatch());
	}

	@Override
	public void dispose() {
		
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public int getTotalExpToNextLevel() {
		return totalExpToNextLevel;
	}

	public int getMaxActionCount() {
		return maxActionCount;
	}

	public int getLifes() {
		return lifes;
	}

	public int getMaxLifes() {
		return maxLifes;
	}

	public Array<Weapon> getWeapons() {
		return weapons;
	}

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public Array<Weapon> getSpells() {
		return spells;
	}

	public AnimationSet getAnimationSet() {
		return animationSet;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setTotalExpToNextLevel(int totalExpToNextLevel) {
		this.totalExpToNextLevel = totalExpToNextLevel;
	}

	public void setMaxActionCount(int maxActionCount) {
		this.maxActionCount = maxActionCount;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}

	public void setMaxLifes(int maxLifes) {
		this.maxLifes = maxLifes;
	}

	public void setCurrentWeapon(Weapon currentWeapon) {
		this.currentWeapon = currentWeapon;
	}
}
