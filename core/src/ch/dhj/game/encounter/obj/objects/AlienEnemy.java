package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.player.AnimationSet;
import com.badlogic.gdx.math.Vector2;

public class AlienEnemy extends Enemy {
    public AlienEnemy(Vector2 position, Vector2 scale, String name, AnimationSet animationSet) {
        super(position, scale, name, animationSet);
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }
}
