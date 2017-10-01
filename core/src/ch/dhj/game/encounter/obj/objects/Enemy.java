package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.player.AnimationSet;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public abstract class Enemy extends Figure{
	private int minPlayerLevel;

	public Enemy(Vector2 position, Vector2 scale, String name, AnimationSet animationSet, int minPlayerLevel) {
		super(position, name, animationSet);
		this.minPlayerLevel = minPlayerLevel;
		setSize(scale);
		setTextureRegion(animationSet.encounterIdleAnimation.getKeyFrame(0));
		//getTextureRegion().setSize(-1, 1);
	}

	@Override
	public void died() {
		getEncounterConfig().enemies.removeValue((Enemy) this, false);
	}

	public abstract Action[] getActions();

	public int getMinPlayerLevel() {
		return minPlayerLevel;
	}

	public void setMinPlayerLevel(int minPlayerLevel) {
		this.minPlayerLevel = minPlayerLevel;
	}
}
