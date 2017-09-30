package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.player.AnimationSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Player extends Figure{
	public static final float EXP_FACTOR = 1.5f;
	public static final float LIFE_FACTOR = 1.5f;
	public static final float ACTIONS_FACTOR = 2f;
	public static final int MAX_ACTIONS = 10;

	private StringBuffer levelChangeReport;

	private Skin skin;
	private TextureAtlas atlasButtons;
	private Stage stage;
	private Table table;
	private Label actionsL;

	public Player(Vector2 position, String name, AnimationSet animationSet) {
		super(position, name, animationSet);
	}

	/**
	 * Does all the ui initialization
	 */
	private void constructUi() {
		atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
		skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		stage = new Stage(new StretchViewport(1920/2, 1080/2));
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		//table.setDebug(true);

		//actionsL = new Label(String.format("%d/%d Actions", currentActionCount, player.getMaxActionCount()), skin);
		final TextButton attackB = new TextButton("Attack", skin);
		attackB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//	addPlayerAction(new WeaponAction(player.getCurrentWeapon()));
			}
		});
		TextButton spellB = new TextButton("Spell", skin);
		TextButton defendB = new TextButton("Defend", skin);
		TextButton fleeB = new TextButton("Flee", skin);

		table.left();
		table.add(actionsL);
		table.row();
		table.add(attackB).width(100);
		table.row();
		table.add(spellB).width(100);
		table.row();
		table.add(defendB).width(100);
		table.row();
		table.add(fleeB).width(100);


	}

	public void addExp(int exp) {
		setExp(getExp() + exp);
		if(getExp() >= getTotalExpToNextLevel()) {
			levelChangeReport = new StringBuffer();
			setExp(0);
			setTotalExpToNextLevel((int) (getTotalExpToNextLevel() * EXP_FACTOR));
			setLevel(getLevel() + 1);
			levelChangeReport.append(String.format("%i lvl -> %i lvl", getLevel()-1, getLevel()));

			int oldMaxLife = getMaxLifes();
			setMaxLifes((int) (getMaxLifes() * LIFE_FACTOR));
			setLifes(getMaxLifes());
			levelChangeReport.append(String.format("%i lifes -> %i lifes", oldMaxLife, getMaxLifes()));

			int oldActionCount = getMaxActionCount();
			setMaxActionCount((int) (getMaxActionCount() * ACTIONS_FACTOR));
			if(getMaxActionCount() > MAX_ACTIONS)
				setMaxActionCount(MAX_ACTIONS);
			levelChangeReport.append(String.format("%i ap -> %i ap", oldActionCount, getMaxActionCount()));
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
