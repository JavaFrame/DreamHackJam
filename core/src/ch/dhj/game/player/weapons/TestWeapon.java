package ch.dhj.game.player.weapons;

import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class TestWeapon extends Weapon {
	public TestWeapon() {
		super("Test Weapon", 2, 5, false, true, false, new Animation(1, Gdx.files.internal("Johhny.png")));
	}
}
