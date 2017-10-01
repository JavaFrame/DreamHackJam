package ch.dhj.game;

import ch.dhj.game.encounter.obj.objects.Enemy;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sebastian on 01.10.2017.
 */
public class EnemyManager {
	private Array<Enemy> enemies = new Array<>();

	public EnemyManager() {
	}

	public void addEnemy(Enemy e) {
		enemies.add(e);
	}


	public Array<Enemy> getEnemies() {
		return enemies;
	}

	public Enemy getEnemyByName(String name) {
		for(Enemy e : enemies)
			if(e.getName().equals(name))
				return e;
		return null;
	}
}
