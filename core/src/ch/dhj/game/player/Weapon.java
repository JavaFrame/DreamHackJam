package ch.dhj.game.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	private ProjectileBluePrint projectileBluePrint;
	private WeaponType type;
	private Texture icon;

	public Weapon(WeaponType type) {
		this(type.getName(), type.getActionCost(), type.getDamge(), type.isMultipleTargets(), type.isMelee(), type.isSpell(), type.getProjectileBluePrint());
		this.type = type;
	}

	public Weapon(String name, int actionCost, int damge, boolean multipleTargets, boolean melee, boolean spell, ProjectileBluePrint projectileBluePrint) {
		this.name = name;
		this.actionCost = actionCost;
		this.damge = damge;
		this.multipleTargets = multipleTargets;
		this.melee = melee;
		this.spell = spell;
		this.icon = icon;
		this.projectileBluePrint = projectileBluePrint;
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

	public boolean hashProjectile() {
		return projectileBluePrint != null;
	}

	public ProjectileBluePrint getProjectileBluePrint() {
		return projectileBluePrint;
	}

	public WeaponType getType() {
		return type;
	}

	public Animation<TextureRegion> getAnimation(AnimationSet animationSet) {
		if(getType() != null)
			return animationSet.weaponMap.get(getType());
		else return null;
	}

	@Override
	public String toString() {
		return getName() + " (" + getActionCost() + " actions)";
	}

	public static enum WeaponType {
		Gun("Gun", 1, 3, false, false, false, null),
		Shotgun("Shotgun", 2, 4, true, false, false, null),
		Stab("Stab", 1, 2, false, true, false, null),
		Heal("Heal", 1, -3, false, false, true, null),
		Fireball("Fireball", 1, 5, false, false, true, null),
		Flamethrower("Flamethrower", 2, 5, false, true, false, null),

		ZombieAttack("Zombie Attack", 1, 1, false, true, false, null),
		ZombieKingAttack("Zombie King Attack", 3, 3, false, false, false, null),
		AlienAttack("Alien Attack", 2, 3, false, false, false, null),
		TrumpAttack("Trump Attack", 3, 5, false, false, false, null);

		private String name;
		private int actionCost;
		private int damge;
		private boolean multipleTargets;
		private boolean melee;
		private boolean spell;
		private ProjectileBluePrint projectileBluePrint;

		WeaponType(String name, int actionCost, int damge, boolean multipleTargets, boolean melee, boolean spell, ProjectileBluePrint projectileBluePrint) {
			this.name = name;
			this.actionCost = actionCost;
			this.damge = damge;
			this.multipleTargets = multipleTargets;
			this.melee = melee;
			this.spell = spell;
			this.projectileBluePrint = projectileBluePrint;
		}

		public String getName() {
			return name;
		}

		public int getActionCost() {
			return actionCost;
		}

		public int getDamge() {
			return damge;
		}

		public boolean isMultipleTargets() {
			return multipleTargets;
		}

		public boolean isMelee() {
			return melee;
		}

		public boolean isSpell() {
			return spell;
		}

		public ProjectileBluePrint getProjectileBluePrint() {
			return projectileBluePrint;
		}
	}

	public Texture getIcon() {return icon;}

	public Weapon setIcon(Texture icon) {
		this.icon = icon;
		return this;
	}

}
