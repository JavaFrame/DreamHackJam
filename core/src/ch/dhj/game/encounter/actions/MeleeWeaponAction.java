package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Figure;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
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

	private boolean init = true;


	public MeleeWeaponAction(Weapon weapon, Figure you, Figure[] enemies) {
		this.weapon = weapon;
		this.you = you;
		this.enemies = enemies;
		oldPosition = new Vector2(you.getPosition());
	}

	@Override
	public void init() {
		you.setAnimation(you.getAnimationSet().encounterWalkAnimation);
	}

	@Override
	public boolean action() {
		Figure e = enemies[index];
		if(doAnimation(you, e) || e.isDead()) {
			alpha = 0;
			timeElepsed = 0;
			index++;
			init = true;
		}

		return index >= enemies.length;
	}

	private boolean doAnimation(Figure you, Figure enemy) {
		if(enemy == you && init) {
			state = ActionState.ATTACK;
			timeElepsed = 0;
			alpha = 0;
			you.setAnimation(weapon.getAnimation(you.getAnimationSet()));
			init = false;
		}
		switch (state) {
			case GO_TO:
				Vector2 targetPos;
				if(enemy.getPosition().x < you.getPosition().x)
					targetPos = new Vector2(enemy.getPosition().x + (enemy.getSize().x/2), enemy.getPosition().y);
				else
					targetPos = new Vector2(enemy.getPosition().x - (enemy.getSize().x/2), enemy.getPosition().y);

				you.getPosition().set(you.getPosition().interpolate(targetPos, alpha, Interpolation.fade));
				alpha += 0.1 * Gdx.graphics.getDeltaTime();
				timeElepsed += Gdx.graphics.getDeltaTime();
				if(you.getPosition().equals(targetPos)) {
					state = ActionState.ATTACK;
					timeElepsed = 0;
					alpha = 0;
					you.setAnimation(weapon.getAnimation(you.getAnimationSet()));
				}
				break;
			case ATTACK:
				if(you.isAnnimationFinished()) {
					if(enemy == you)
					{
						you.setAnimation(you.getAnimationSet().encounterIdleAnimation);
						you.applayDamage(weapon.getDamge(), you);
						return true;
					}
					state = ActionState.GO_FROM;
					timeElepsed = 0;
					alpha = 0;
					//you.getTextureRegion().flip(true, false);
					you.setSize(new Vector2(-you.getSize().x, you.getSize().y));
					oldPosition.set(oldPosition.x-(you.getSize().x/2 + you.getSize().x/4), oldPosition.y);
					you.getPosition().set(you.getPosition().x-(you.getSize().x/2 + you.getSize().x/4), you.getPosition().y);
					you.setAnimation(you.getAnimationSet().encounterWalkAnimation);
					enemy.applayDamage(weapon.getDamge(), you);
				}

				break;
			case GO_FROM:
				you.getPosition().set(you.getPosition().interpolate(oldPosition, alpha, Interpolation.fade));
				alpha += 0.1 * Gdx.graphics.getDeltaTime();
				timeElepsed += Gdx.graphics.getDeltaTime();
				if(you.getPosition().equals(oldPosition)) {
					you.setSize(new Vector2(-you.getSize().x, you.getSize().y));
					you.getPosition().set(you.getPosition().x-(you.getSize().x/2 + you.getSize().x/4), you.getPosition().y);
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
