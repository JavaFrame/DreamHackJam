package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.Turn;
import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.encounter.actions.MeleeWeaponAction;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
	private Table turnActionTable;
	private Label actionsL;

	private int actions;

	private Turn currentTurn = new Turn();

	private Weapon selectedWeapon;

	public Player(Vector2 position, String name, AnimationSet animationSet) {
		super(position, name, animationSet);

	}

	public Player(Texture texture, Vector2 position, String name, AnimationSet animationSet) {
		super(texture, position, name, animationSet);
	}

	@Override
	public void init() {
		super.init();
		constructUi();

		getTurnManager().addRoundDoneListener(new TurnManager.TurnManagerListener() {
			@Override
			public void triggered(TurnManager manager) {
				currentTurn = new Turn();
				actions = 0;
				turnActionTable.setVisible(true);
			}
		});
	}

	/**
	 * Does all the ui initialization
	 */
	private void constructUi() {
		atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
		skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		stage = new Stage(new StretchViewport(1920/2, 1080/2));
		Gdx.input.setInputProcessor(stage);

		final Table tablePane = new Table();
		stage.addActor(tablePane);

		turnActionTable = new Table();
		turnActionTable.setFillParent(false);
		tablePane.add(new Container<Table>(turnActionTable)).left();

		final Table chooseEnemyTable = new Table(skin);
		chooseEnemyTable.setVisible(false);

		final List<Enemy> enemies = new List<Enemy>(skin);
		enemies.setItems(getEncounterConfig().enemies.toArray(new Enemy[]{}));
		chooseEnemyTable.add(enemies);

		chooseEnemyTable.row();

		TextButton doButton = new TextButton("Do", skin);
		doButton.pad(10);
		doButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(selectedWeapon.isSpell()) { //weapon is a spell

				} else if(selectedWeapon.isMelee()) { //weapon is a mele attack
					addActionToTurn(new MeleeWeaponAction(selectedWeapon, Player.this, new Enemy[]{enemies.getSelected()}));
				} else { //weapon is a range weapon

				}
			}
		});
		chooseEnemyTable.add(doButton);

		actionsL = new Label(String.format("%d/%d Actions", actions, getMaxActionCount()), skin);

		final TextButton attackB = new TextButton("Attack", skin);
		if(getCurrentWeapon() == null) {
			attackB.setTouchable(Touchable.disabled);
			attackB.setText(attackB.getText() + " (unavailbe)");
		}
		attackB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//
				Weapon w = Player.this.getCurrentWeapon();
				if(w.isMultipleTargets()) {
					addActionToTurn(new MeleeWeaponAction(getCurrentWeapon(), Player.this, getEncounterConfig().enemies.toArray(new Enemy[]{})));
					return;
				}
				selectedWeapon = w;
				chooseEnemyTable.setVisible(true);
			}
		});
		TextButton spellB = new TextButton("Spell", skin);
		TextButton defendB = new TextButton("Defend", skin);
		TextButton fleeB = new TextButton("Flee", skin);

		turnActionTable.left();
		turnActionTable.add(actionsL);
		turnActionTable.row();
		turnActionTable.add(attackB).width(200);
		turnActionTable.row();
		turnActionTable.add(spellB).width(200);
		turnActionTable.row();
		turnActionTable.add(defendB).width(200);
		turnActionTable.row();
		turnActionTable.add(fleeB).width(200);

		tablePane.add(chooseEnemyTable).pad(5);

		tablePane.pack();
		tablePane.setX(tablePane.getWidth()/2);
		tablePane.setY(tablePane.getHeight()/2);
	}

	private void addActionToTurn(Action a) {
		currentTurn.addAction(a);
		actions++;
		if(actions >= getMaxActionCount()) {
			getTurnManager().getTurns().add(currentTurn);
			currentTurn = new Turn();
			getTurnManager().start();

			turnActionTable.setVisible(false);
		}
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
	public void render(float delta, SpriteBatch batch) {
		super.render(delta, batch);
		stage.act();
		batch.end();
		stage.draw();
		batch.begin();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
