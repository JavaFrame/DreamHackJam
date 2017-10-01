package ch.dhj.game.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Sebastian on 01.10.2017.
 */
public class ProjectileBluePrint {
	private Animation<TextureRegion> animation;
	private float fireTime;
	private float velocity;
	private int damge;
	private int fireCount;
	private int repeatedFireTime;

	public ProjectileBluePrint(Animation<TextureRegion> animation, float fireTime, float velocity, int damge, int fireCount, int repeatedFireTime) {
		this.animation = animation;
		this.fireTime = fireTime;
		this.velocity = velocity;
		this.damge = damge;
		this.fireCount = fireCount;
		this.repeatedFireTime = repeatedFireTime;
	}

	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	public float getVelocity() {
		return velocity;
	}

	public int getDamge() {
		return damge;
	}

	public float getFireTime() {
		return fireTime;
	}

	public int getFireCount() {
		return fireCount;
	}

	public int getRepeatedFireTime() {
		return repeatedFireTime;
	}
}
