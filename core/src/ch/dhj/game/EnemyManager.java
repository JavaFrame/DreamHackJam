package ch.dhj.game;

import ch.dhj.game.encounter.obj.objects.Enemy;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

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
				return new Enemy(e);
		return null;
	}

	public Enemy modifyEnemy(Enemy enemy, int playerLevel) {
		int level = playerLevel + (new Random().nextInt(3)-2);
		if(level < enemy.getMinPlayerLevel())
			level = enemy.getMinPlayerLevel();

		enemy.setLevel(level);
		enemy.setMaxLifes(enemy.getMaxLifes()*level);
		enemy.setLifes(enemy.getMaxLifes());
		int actionCount = enemy.getMaxActionCount() * level;
		enemy.setMaxActionCount((actionCount<5?actionCount:4));
		return enemy;
	}
}
