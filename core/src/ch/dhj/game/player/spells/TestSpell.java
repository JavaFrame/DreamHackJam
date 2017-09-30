package ch.dhj.game.player.spells;

import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class TestSpell extends Weapon {
	public TestSpell() {
		super("Test Spell",
				1,
				2,
				false,
				false,
				true,
				new Animation(1, new Texture(Gdx.files.internal("textures/Johhny.png"))));
	}
}
