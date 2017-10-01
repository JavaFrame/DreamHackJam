package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.player.AnimationSet;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Enemy extends Figure{
	private int minPlayerLevel;
	private int expToDrop;

	public Enemy(Enemy e) {
		super(e.getPosition().cpy(), e.getName(), e.getAnimationSet());
		setMaxLifes(e.getMaxLifes());
		setLifes(e.getMaxLifes());
		setLevel(e.getLevel());
		setTextureRegion(e.getTextureRegion());
		setExpToDrop(e.getExpToDrop());
		setMinPlayerLevel(e.getMinPlayerLevel());
		setMaxActionCount(e.getMaxActionCount());
		setMeleeWeapon(e.getMeleeWeapon());
		setRangeWeapon(e.getRangeWeapon());
		setMaxActionCount(e.getMaxActionCount());
		setTotalExpToNextLevel(e.getTotalExpToNextLevel());
		setAnimation(e.getAnimation());
		setParent(getParent());
		setSize(e.getSize());
		setStateTime(e.getStateTime());
	}

	public Enemy(Vector2 position, Vector2 scale, String name, AnimationSet animationSet, int minPlayerLevel, int expToDrop) {
		super(position, name, animationSet);
		this.minPlayerLevel = minPlayerLevel;
		setSize(scale);
		setTextureRegion(animationSet.encounterIdleAnimation.getKeyFrame(0));
		//getTextureRegion().setSize(-1, 1);
		this.expToDrop = expToDrop;
	}

	@Override
	public void died() {
		getEncounterConfig().enemies.removeValue((Enemy) this, false);
	}

	public Action[] getActions(){
		return new Action[]{};
	}

	public int getMinPlayerLevel() {
		return minPlayerLevel;
	}

	public void setMinPlayerLevel(int minPlayerLevel) {
		this.minPlayerLevel = minPlayerLevel;
	}

	public int getExpToDrop() {
		return expToDrop;
	}

	public void setExpToDrop(int expToDrop) {
		this.expToDrop = expToDrop;
	}
}
