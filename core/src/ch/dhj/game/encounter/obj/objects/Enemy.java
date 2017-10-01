package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.actions.MeleeWeaponAction;
import ch.dhj.game.encounter.actions.RangeWeaponAction;
import ch.dhj.game.player.AnimationSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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

	public Array<Action> getActions(){
		Array<Action> actions = new Array<>();
		int ap = 0;
		while(ap < getMaxActionCount()) {
			if(getLifes() < 3) {
				//TODO: heal
			}
			if(getMeleeWeapon() != null) {
				actions.add(new MeleeWeaponAction(getMeleeWeapon(), this, new Figure[]{getEncounterScreen().getPlayer()}));
				ap += getMeleeWeapon().getActionCost();
			} else if(getRangeWeapon() != null) {
				actions.add(new RangeWeaponAction(getRangeWeapon(), this, new Figure[]{getEncounterScreen().getPlayer()}));
				ap += getMeleeWeapon().getActionCost();
			}
		}
		return actions;
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
