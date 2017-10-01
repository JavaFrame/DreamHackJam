package ch.dhj.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class AnimationSet {
	//overworld
	public Animation<TextureRegion> walkUpAnimation;
	public Animation<TextureRegion> walkDownAnimation;
	public Animation<TextureRegion> walkLeftAnimation;
	public Animation<TextureRegion> walkRightAnimation;

	private float jonnyWalkUpAnimationTime;
	private float jonnyWalkDownAnimationTime;
	private float jonnyWalkLeftAnimationTime;
	private float jonnyWalkRightAnimationTime;

	//encounter
	public Animation<TextureRegion> encounterIdleAnimation;
	public Animation<TextureRegion> encounterWalkAnimation;
	public Animation<TextureRegion> encounterDieAnimation;
	public Animation<TextureRegion> encounterDamagedAnimation;

	public OrderedMap<Weapon.WeaponType, Animation> weaponMap = new OrderedMap<>();

	public AnimationSet(AnimationSet animationSet) {
		/*this(animationSet.walkUpAnimation,
				animationSet.walkDownAnimation,
				animationSet.walkLeftAnimation,
				animationSet.walkRightAnimation,
				animationSet.encounterIdleAnimation,
				animationSet.encounterWalkAnimation,
				new Weapon.WeaponType[]{},
				new Animation[]{});*/
		setWalkUpAnimation(animationSet.getWalkUpAnimation());
		setWalkDownAnimation(animationSet.getWalkDownAnimation());
		setWalkLeftAnimation(animationSet.getWalkLeftAnimation());
		setWalkRightAnimation(animationSet.getWalkRightAnimation());
		setJonnyWalkUpAnimationTime(animationSet.getJonnyWalkUpAnimationTime());
		setJonnyWalkDownAnimationTime(animationSet.getJonnyWalkDownAnimationTime());
		setJonnyWalkLeftAnimationTime(animationSet.getJonnyWalkLeftAnimationTime());
		setJonnyWalkRightAnimationTime(animationSet.getJonnyWalkRightAnimationTime());
		setEncounterWalkAnimation(animationSet.getEncounterWalkAnimation());
		setEncounterDieAnimation(animationSet.getEncounterDieAnimation());
		setEncounterIdleAnimation(animationSet.getEncounterIdleAnimation());
		this.weaponMap = animationSet.weaponMap;
	}

	public AnimationSet() {
		this.walkUpAnimation = walkUpAnimation;
		this.walkDownAnimation = walkDownAnimation;
		this.walkLeftAnimation = walkLeftAnimation;
		this.walkRightAnimation = walkRightAnimation;
		this.encounterIdleAnimation = encounterIdleAnimation;
		this.encounterWalkAnimation = encounterWalkAnimation;

		/*if(weaponAnimations.length != weaponTypes.length)
			throw new IllegalArgumentException("Length of weaponTypes array and weaponAnimations array have to match!");

		for (int i = 0; i < weaponAnimations.length; i++) {
			weaponMap.put(weaponTypes[i], weaponAnimations[i]);
		}*/
	}


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

	public Animation<TextureRegion> getEncounterIdleAnimation() {
		return encounterIdleAnimation;
	}

	public void setEncounterIdleAnimation(Animation<TextureRegion> encounterIdleAnimation) {
		this.encounterIdleAnimation = encounterIdleAnimation;
	}

	public Animation<TextureRegion> getEncounterWalkAnimation() {
		return encounterWalkAnimation;
	}

	public void setEncounterWalkAnimation(Animation<TextureRegion> encounterWalkAnimation) {
		this.encounterWalkAnimation = encounterWalkAnimation;
	}

	public Animation<TextureRegion> getEncounterDieAnimation() {
		return encounterDieAnimation;
	}

	public void setEncounterDieAnimation(Animation<TextureRegion> encounterDieAnimation) {
		this.encounterDieAnimation = encounterDieAnimation;
	}

	public Animation<TextureRegion> getEncounterDamagedAnimation() {
		return encounterDamagedAnimation;
	}

	public void setEncounterDamagedAnimation(Animation<TextureRegion> encounterDamagedAnimation) {
		this.encounterDamagedAnimation = encounterDamagedAnimation;
	}

	public OrderedMap<Weapon.WeaponType, Animation> getWeaponMap() {
		return weaponMap;
	}

	public void setWeaponMap(OrderedMap<Weapon.WeaponType, Animation> weaponMap) {
		this.weaponMap = weaponMap;
	}
}
