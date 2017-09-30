package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.player.Player;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class WeaponAction implements Action {
	private Weapon weapon;

	private AttackState state;
	private Vector2 initialPos;
	private float alpha = 0;

	public WeaponAction(Weapon weapon) {
		this.weapon = weapon;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	@Override
	public void init(Player you, Player enemy) {
		initialPos.set(you.getPosition());
	}

	@Override
	public boolean action(Player you, Player enemy) {
		switch (state) {
			case GO_TO:
				you.getPosition().set(you.getPosition().interpolate(enemy.getPosition(), alpha, new Interpolation.Bounce(1)));
				alpha += 1 * Gdx.graphics.getDeltaTime();
				if(you.getPosition().equals(enemy.getPosition())) {
					state = AttackState.ATTACK;
					alpha = 0;
				}
				break;
			case ATTACK:
				break;
			case GO_FROM:
				you.getPosition().set(you.getPosition().interpolate(enemy.getPosition(), alpha, new Interpolation.Bounce(1)));
				alpha += 1 * Gdx.graphics.getDeltaTime();
				if(you.getPosition().equals(initialPos)) {
					return true;
				}
				break;
		}
		return false;
	}

	@Override
	public TextureRegion getFrame(Player you, Player enemy) {
		switch (state) {
			case GO_TO:

				break;
			case ATTACK:
				break;
			case GO_FROM:

				break;
		}
		return null;
	}

	private enum AttackState {
		GO_TO,
		ATTACK,
		GO_FROM
	}
}
