package ch.dhj.game.player;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class AnimationSet {
	//overworld
	private Animation walkUpAnimation;
	private Animation walkDownAnimation;
	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;

	private float jonnyWalkUpAnimationTime;
	private float jonnyWalkDownAnimationTime;
	private float jonnyWalkLeftAnimationTime;
	private float jonnyWalkRightAnimationTime;

	//encounter
	public Animation encounterWalkAnimation;

	public Animation getWalkUpAnimation() {
		return walkUpAnimation;
	}

	public void setWalkUpAnimation(Animation walkUpAnimation) {
		this.walkUpAnimation = walkUpAnimation;
	}

	public Animation getWalkDownAnimation() {
		return walkDownAnimation;
	}

	public void setWalkDownAnimation(Animation walkDownAnimation) {
		this.walkDownAnimation = walkDownAnimation;
	}

	public Animation getWalkLeftAnimation() {
		return walkLeftAnimation;
	}

	public void setWalkLeftAnimation(Animation walkLeftAnimation) {
		this.walkLeftAnimation = walkLeftAnimation;
	}

	public Animation getWalkRightAnimation() {
		return walkRightAnimation;
	}

	public void setWalkRightAnimation(Animation walkRightAnimation) {
		this.walkRightAnimation = walkRightAnimation;
	}

	public float getJonnyWalkUpAnimationTime() {
		return jonnyWalkUpAnimationTime;
	}

	public void setJonnyWalkUpAnimationTime(float jonnyWalkUpAnimationTime) {
		this.jonnyWalkUpAnimationTime = jonnyWalkUpAnimationTime;
	}

	public float getJonnyWalkDownAnimationTime() {
		return jonnyWalkDownAnimationTime;
	}

	public void setJonnyWalkDownAnimationTime(float jonnyWalkDownAnimationTime) {
		this.jonnyWalkDownAnimationTime = jonnyWalkDownAnimationTime;
	}

	public float getJonnyWalkLeftAnimationTime() {
		return jonnyWalkLeftAnimationTime;
	}

	public void setJonnyWalkLeftAnimationTime(float jonnyWalkLeftAnimationTime) {
		this.jonnyWalkLeftAnimationTime = jonnyWalkLeftAnimationTime;
	}

	public float getJonnyWalkRightAnimationTime() {
		return jonnyWalkRightAnimationTime;
	}

	public void setJonnyWalkRightAnimationTime(float jonnyWalkRightAnimationTime) {
		this.jonnyWalkRightAnimationTime = jonnyWalkRightAnimationTime;
	}

}
