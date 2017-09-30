package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Figure;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class MeleeWeaponAction implements Action {
	private Weapon weapon;
	private Figure you;
	private Figure[] enemies;
	private ActionState state = ActionState.GO_TO;
	private float alpha;
	private float timeElepsed;

	private Vector2 oldPosition;

	private int index = 0;


	public MeleeWeaponAction(Weapon weapon, Figure you, Figure[] enemies) {
		this.weapon = weapon;
		this.enemies = enemies;
		oldPosition = new Vector2(you.getPosition());
	}

	@Override
	public boolean action() {
		Figure e = enemies[index];
		if(doAnimation(you, e)) {
			alpha = 0;
			timeElepsed = 0;
			index++;
		}

		return index >= enemies.length;
	}

	private boolean doAnimation(Figure you, Figure enemy) {
		switch (state) {
			case GO_TO:
				you.getPosition().set(you.getPosition().interpolate(enemy.getPosition(), alpha, new Interpolation.Bounce(1)));
				alpha += 0.01 * Gdx.graphics.getDeltaTime();
				timeElepsed += Gdx.graphics.getDeltaTime();
				you.setTexture(((TextureRegion) you.getAnimationSet().encounterWalkAnimation.getKeyFrame(timeElepsed)).getTexture());
				if(you.getPosition().equals(enemy.getPosition())) {
					state = ActionState.ATTACK;
					timeElepsed = 0;
					alpha = 0;
				}
				break;
			case ATTACK:
				Animation a = you.getCurrentWeapon().getAnimation();
				you.setTexture(((TextureRegion) a.getKeyFrame(timeElepsed)).getTexture());
				timeElepsed += Gdx.graphics.getDeltaTime();
				if(a.isAnimationFinished(timeElepsed)) {
					state = ActionState.GO_FROM;
					timeElepsed = 0;
					alpha = 0;
				}

				break;
			case GO_FROM:
				you.getPosition().set(you.getPosition().interpolate(oldPosition, alpha, new Interpolation.Bounce(1)));
				alpha += 0.01 * Gdx.graphics.getDeltaTime();
				timeElepsed += Gdx.graphics.getDeltaTime();
				you.setTexture(((TextureRegion) you.getAnimationSet().encounterWalkAnimation.getKeyFrame(timeElepsed)).getTexture());
				if(you.getPosition().equals(oldPosition)) {
					return true;
				}
				break;
		}
		return false;
	}

	private enum ActionState {
		GO_TO,
		ATTACK,
		GO_FROM
	}
}
