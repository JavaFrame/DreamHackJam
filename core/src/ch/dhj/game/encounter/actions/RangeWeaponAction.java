package ch.dhj.game.encounter.actions;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Figure;
import ch.dhj.game.player.ProjectileBluePrint;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
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
		if(you.getLifes() <= 0) return true;
		if(init) {
			Animation<TextureRegion> animation = weapon.getAnimation(you.getAnimationSet());
			you.setAnimation(animation);
			init = false;
		}

		Figure e = enemies[index];
		if(doAnimation(you, e)) {
			alpha = 0;
			timeElepsed = 0;
			index++;
		}

		return index >= enemies.length;
	}

	private boolean doAnimation(Figure you, Figure enemy) {
		//Animation<TextureRegion> animation = weapon.getAnimation(you.getAnimationSet());
		/*if(animation == null) {
			System.err.println("Animation of weapon " + weapon + " returned null!");
			return true;
		}*/
		//timeElepsed += Gdx.graphics.getDeltaTime();
		shootTimeElepsed += Gdx.graphics.getDeltaTime();
		//you.setTextureRegion(animation.getKeyFrame(timeElepsed));
		boolean isFinished = you.isAnnimationFinished();

		if(weapon.hashProjectile()) {
			if(shootTimeElepsed >= weapon.getProjectileBluePrint().getFireTime() && shootedCount == 0) {
				shootProjecitle(you.getPosition(), enemy.getPosition());
				shootTimeElepsed = 0;
			} else if(shootTimeElepsed >= weapon.getProjectileBluePrint().getRepeatedFireTime() && weapon.getProjectileBluePrint().getFireCount() < shootedCount){
				shootProjecitle(you.getPosition(), enemy.getPosition());
				shootTimeElepsed = 0;
			}
		} else {
			if(isFinished) {
				enemy.applayDamage(weapon.getDamge());
				you.setAnimation(you.getAnimationSet().encounterIdleAnimation);
			}
		}

		for(Projectile p : projecitlPos) {
			p.currentPos.set(p.currentPos.interpolate(p.targetPos, p.alpha, Interpolation.linear));
			alpha += weapon.getProjectileBluePrint().getVelocity() * Gdx.graphics.getDeltaTime();
			if(p.currentPos.epsilonEquals(p.targetPos, 1)) {

			}
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
