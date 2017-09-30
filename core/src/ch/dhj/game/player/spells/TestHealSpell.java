package ch.dhj.game.player.spells;

import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

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
				true,
				new Animation(1, new Texture(Gdx.files.internal("textures/Johhny.png"))));
	}
}
