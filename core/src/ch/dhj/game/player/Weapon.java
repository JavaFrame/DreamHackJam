package ch.dhj.game.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Weapon {
	private String name;
	private int actionCost;
	private int damge;
	private boolean multipleTargets;
	private boolean melee;
	private boolean spell;

	private Animation animation;

	public Weapon(String name, int actionCost, int damge, boolean multipleTargets, boolean melee, boolean spell, Animation animation) {
		this.name = name;
		this.actionCost = actionCost;
		this.damge = damge;
		this.multipleTargets = multipleTargets;
		this.melee = melee;
		this.spell = spell;
		this.animation = animation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActionCost() {
		return actionCost;
	}

	public void setActionCost(int actionCost) {
		this.actionCost = actionCost;
	}

	public int getDamge() {
		return damge;
	}

	public void setDamge(int damge) {
		this.damge = damge;
	}

	public boolean isMultipleTargets() {
		return multipleTargets;
	}

	public void setMultipleTargets(boolean multipleTargets) {
		this.multipleTargets = multipleTargets;
	}

	public boolean isMelee() {
		return melee;
	}

	public boolean isSpell() {
		return spell;
	}

	public void setSpell(boolean spell) {
		this.spell = spell;
	}

	public Animation<Texture> getAnimation() {
		return animation;
	}

	public static enum WeaponTypes {
		Gun,
		Shotgun,
		
	}
}
