package ch.dhj.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class AnimationSet {
	//overworld
	public Animation walkUpAnimation;
	public Animation walkDownAnimation;
	public Animation walkLeftAnimation;
	public Animation walkRightAnimation;

	//encounter
	public Animation<Texture> encounterWalkAnimation = new Animation(1, new Texture(Gdx.files.internal("textures/Johhny.png")));

	public OrderedMap<String, Animation> weaponMap = new OrderedMap<>();
}
