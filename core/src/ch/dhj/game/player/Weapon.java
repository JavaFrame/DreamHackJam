package ch.dhj.game.player;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Weapon {
	private String name;
	private int actionCost;
	private int damge;
	private boolean multipleTargets;
	private boolean spell;

	public Weapon(String name, int actionCost, int damge, boolean multipleTargets, boolean spell) {
		this.name = name;
		this.actionCost = actionCost;
		this.damge = damge;
		this.multipleTargets = multipleTargets;
		this.spell = spell;
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

	public boolean isSpell() {
		return spell;
	}

	public void setSpell(boolean spell) {
		this.spell = spell;
	}
}
