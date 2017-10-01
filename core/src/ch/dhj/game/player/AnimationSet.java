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

	//encounter
	public Animation<TextureRegion> encounterIdleAnimation;
	public Animation<TextureRegion> encounterWalkAnimation;
	public Animation<TextureRegion> encounterDieAnimation;

	public OrderedMap<Weapon.WeaponType, Animation> weaponMap = new OrderedMap<>();

	public AnimationSet(AnimationSet animationSet) {
		this(animationSet.walkUpAnimation,
				animationSet.walkDownAnimation,
				animationSet.walkLeftAnimation,
				animationSet.walkRightAnimation,
				animationSet.encounterIdleAnimation,
				animationSet.encounterWalkAnimation,
				new Weapon.WeaponType[]{},
				new Animation[]{});
		this.weaponMap = animationSet.weaponMap;
	}

	public AnimationSet(Animation<TextureRegion> walkUpAnimation,
						Animation<TextureRegion> walkDownAnimation,
						Animation<TextureRegion> walkLeftAnimation,
						Animation<TextureRegion> walkRightAnimation,
						Animation<TextureRegion> encounterIdleAnimation,
						Animation<TextureRegion> encounterWalkAnimation,
						Weapon.WeaponType[] weaponTypes,
						Animation<TextureRegion>[] weaponAnimations
	) {
		this.walkUpAnimation = walkUpAnimation;
		this.walkDownAnimation = walkDownAnimation;
		this.walkLeftAnimation = walkLeftAnimation;
		this.walkRightAnimation = walkRightAnimation;
		this.encounterIdleAnimation = encounterIdleAnimation;
		this.encounterWalkAnimation = encounterWalkAnimation;

		if(weaponAnimations.length != weaponTypes.length)
			throw new IllegalArgumentException("Length of weaponTypes array and weaponAnimations array have to match!");

		for (int i = 0; i < weaponAnimations.length; i++) {
			weaponMap.put(weaponTypes[i], weaponAnimations[i]);
		}
	}


}
