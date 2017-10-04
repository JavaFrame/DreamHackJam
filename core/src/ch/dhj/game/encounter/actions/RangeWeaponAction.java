package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Figure;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sebastian on 01.10.2017.
 */
public class RangeWeaponAction implements Action {
	private Weapon weapon;
	private Figure you;
	private Figure[] enemies;
	private float alpha;
	private float timeElepsed;
	private float shootTimeElepsed;

	private int index = 0;

	private int shootedCount = 0;

	private boolean init = true;

	private Array<Projectile> projecitlPos = new Array<>();

	public RangeWeaponAction(Weapon weapon, Figure you, Figure[] enemies) {
		this.weapon = weapon;
		this.you = you;
		this.enemies = enemies;
	}

	@Override
	public void init() {
	}

	@Override
	public boolean action() {
		if(you.isDead()) return true;
		Figure e = enemies[index];
		if(init & (e.isDead() || you.isDead())) {
			alpha = 0;
			timeElepsed = 0;
			index++;
			init = true;
		}
		if(init && !(e.isDead() || you.isDead())) {
			you.setAnimation(weapon.getAnimation(you.getAnimationSet()));
			init = false;
		}

		if(doAnimation(you, e)) {
			alpha = 0;
			timeElepsed = 0;
			index++;
			init = true;
		}

		return index >= enemies.length;
	}

	private boolean doAnimation(Figure you, Figure enemy) {
		boolean isFinished = you.isAnnimationFinished();

		if(isFinished) {
			enemy.applyDamage(weapon.getDamge(), you);
			you.setAnimation(you.getAnimationSet().encounterIdleAnimation);
		}

		return isFinished;
	}

	private void shootProjecitle(Vector2 startPos, Vector2 targetPos) {
		projecitlPos.add(new Projectile(startPos, targetPos));
	}

	private class Projectile {
		public Vector2 currentPos;
		public Vector2 targetPos;
		public float alpha;

		public Projectile(Vector2 currentPos, Vector2 targetPos) {
			this.currentPos = currentPos;
			this.targetPos = targetPos;
			this.alpha = 0;
		}

		public Vector2 getCurrentPos() {
			return currentPos;
		}

		public Vector2 getTargetPos() {
			return targetPos;
		}

		public float getAlpha() {
			return alpha;
		}
	}
}
