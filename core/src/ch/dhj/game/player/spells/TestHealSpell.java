package ch.dhj.game.player.spells;

import ch.dhj.game.player.Weapon;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class TestHealSpell extends Weapon {
	public TestHealSpell() {
		super("Test Heal Spell",
				1,
				-3,
				false,
				false,
				true, projectile);
	}
}
