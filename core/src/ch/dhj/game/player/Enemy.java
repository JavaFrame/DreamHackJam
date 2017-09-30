package ch.dhj.game.player;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.actions.IdleAction;
import com.badlogic.gdx.maps.MapRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Enemy extends Player {

	public Enemy(String name, int level, int actionCount, int lifes, int maxLifes, Weapon currentWeapon, Weapon[] spells, AnimationSet animationSet) {
		super(name, level, 0, 0, actionCount, lifes, maxLifes, new ArrayList<Weapon>(), animationSet);
		setCurrentWeapon(currentWeapon);
		getSpells().addAll(Arrays.asList(spells));
	}

	public Action[] getActions() {
		return new Action[]{new IdleAction()};
	}
}
