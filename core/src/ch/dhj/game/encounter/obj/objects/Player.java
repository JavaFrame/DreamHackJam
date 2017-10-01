package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.Turn;
import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.encounter.actions.MeleeWeaponAction;
import ch.dhj.game.encounter.actions.RangeWeaponAction;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.screens.OverworldScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Random;

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
	private Table rootTable;
	private Table chooseEnemyTable;
	private Table chooseSpellTable;
	private Table turnActionTable;
	private Label actionsL;
	public int objectPosIndex = 1;
	private EnemyManager enemyManager;

	private int actions;

	private Turn currentTurn = new Turn();

	private Weapon selectedWeapon;

	public Player(Vector2 position, String name, AnimationSet animationSet) {
		super(position, name, animationSet);
		objectPosIndex = 1;
	}

	public Player(Sprite texture, Vector2 position, Vector2 scale, String name, AnimationSet animationSet) {
		super(texture, position, scale, name, animationSet);
		objectPosIndex = 1;
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
				rootTable.setVisible(true);
			}
		});
	}

	@Override
	public void createLifeInfos() {
		TextureAtlas atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
		Skin skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		infoTable = new Table(skin);

		nameL = new Label(getName(), skin);
		nameL.setFontScale(2.5f);
		levelL = new Label(getLevel() + " lvl", skin);
		levelL.setFontScale(2.5f);
		lifesL = new Label(getLifes() + "/" + getMaxLifes() + " health", skin);
		lifesL.setFontScale(2.5f);

		infoTable.add(nameL);
		infoTable.row();
		infoTable.add(levelL);
		infoTable.row();
		infoTable.add(lifesL);
		infoTable.pack();
		infoTable.setPosition(1800, 1500);
	}

	@Override
	public void drawLifeInfos(SpriteBatch batch) {
		infoTable.setPosition(1920-infoTable.getWidth()-20, 1700);
		infoTable.draw(batch, 1f);
	}

	/**
	 * Does all the ui initialization
	 */
	private void constructUi() {
		atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
		skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		stage = new Stage(new StretchViewport(1920, 1080));
		Gdx.input.setInputProcessor(stage);

		rootTable = new Table();
		stage.addActor(rootTable);

		turnActionTable = new Table();
		turnActionTable.setFillParent(false);
		rootTable.add(new Container<Table>(turnActionTable)).left();

		chooseEnemyTable = new Table(skin);
		chooseEnemyTable.setVisible(false);

		final List<Enemy> enemies = new List<Enemy>(skin);
		enemies.setItems(getEncounterConfig().enemies.toArray());
		chooseEnemyTable.add(enemies);

		chooseEnemyTable.row();

		TextButton doButton = new TextButton("Do", skin);
		doButton.pad(10);
		doButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(selectedWeapon.isSpell()) { //weapon is a spell
					addActionToTurn(new RangeWeaponAction(selectedWeapon, Player.this, new Enemy[]{enemies.getSelected()}));
				} else if(selectedWeapon.isMelee()) { //weapon is a mele attack
					addActionToTurn(new MeleeWeaponAction(selectedWeapon, Player.this, new Enemy[]{enemies.getSelected()}));
				} else { //weapon is a range weapon
					addActionToTurn(new RangeWeaponAction(selectedWeapon, Player.this, new Enemy[]{enemies.getSelected()}));
				}
				chooseSpellTable.setVisible(false);
			}
		});
		chooseEnemyTable.add(doButton).width(100);

		//choose spell menu
		chooseSpellTable = new Table(skin);
		final List<Weapon> weapons = new List<Weapon>(skin);

		TextButton chooseButton = new TextButton("Choose", skin);
		chooseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Weapon w = weapons.getSelected();
				if(w.isMultipleTargets()) {
					addActionToTurn(new MeleeWeaponAction(getMeleeWeapon(), Player.this, getEncounterConfig().enemies.toArray()));
					return;
				}
				selectedWeapon = w;
				chooseEnemyTable.setVisible(!chooseEnemyTable.isVisible());
				enemies.setItems(getEncounterConfig().enemies);
			}
		});
		chooseSpellTable.add(weapons);
		chooseSpellTable.row();
		chooseSpellTable.add(chooseButton).width(100);
		chooseSpellTable.setVisible(false);

		//actions menu
		actionsL = new Label(String.format("%d/%d Actions", actions, getMaxActionCount()), skin);
		final TextButton attackB = new TextButton("Attack", skin);
		if(getMeleeWeapon() == null) {
			attackB.setTouchable(Touchable.disabled);
			attackB.setText(attackB.getText() + " (unavailbe)");
		}
		attackB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//
				/*Weapon w = Player.this.getMeleeWeapon();
				if(w.isMultipleTargets()) {
					addActionToTurn(new MeleeWeaponAction(getMeleeWeapon(), Player.this, getEncounterConfig().enemies.toArray()));
					return;
				}
				selectedWeapon = w;
				chooseEnemyTable.setVisible(!chooseEnemyTable.isVisible());*/
				weapons.setItems(getMeleeWeapon());
				chooseSpellTable.setVisible(true);

			}
		});
		TextButton spellB = new TextButton("Spell", skin);
		spellB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				chooseSpellTable.setVisible(true);
				weapons.setItems(getSpells());
				chooseEnemyTable.setVisible(false);
			}
		});
		TextButton defendB = new TextButton("Defend", skin);
		defendB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

			}
		});
		TextButton runB = new TextButton("Run", skin);
		runB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				float random = new Random(System.currentTimeMillis()).nextFloat();
				Array<Enemy> enemies = getEncounterConfig().enemies;
				Enemy highestLvlEnemy = enemies.first();
				for(Enemy e : enemies) {
					if(e.getLevel() > highestLvlEnemy.getLevel()) {
						highestLvlEnemy = e;
					}
				}
				int level = highestLvlEnemy.getLevel();
				float chances = 0f;
				if(level > getLevel()) {
					chances = 0.4f;
				} else if(level == getLevel()) {
					chances = 0.3f;
				} else if(level < getLevel()) {
					chances = 0.2f;
				}
				if(random <= chances) {
					Dialog dialog = null;
					dialog = new Dialog("Running successful!", skin);
					dialog.text("You got away with a black eye!");
					TextButton closeB = new TextButton("close", skin);
					final Dialog finalDialog = dialog;
					closeB.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							finalDialog.hide();
							((Game)Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(getEncounterScreen().getAssetManager(), getEncounterScreen().getBatch(), Player.this, enemyManager));
						}
					});
					dialog.button("close", closeB);
					dialog.show(stage);
				} else {
					Dialog dialog = null;
					dialog = new Dialog("Running failed!", skin);
					dialog.text("You couldn't run away! \nYour turn ends!");
					TextButton closeB = new TextButton("close", skin);
					final Dialog finalDialog = dialog;
					closeB.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							finalDialog.hide();
							endTurn();
						}
					});
					dialog.button("close", closeB);
					dialog.show(stage);
				}
			}
		});

		turnActionTable.left();
		turnActionTable.add(actionsL);
		turnActionTable.row();
		turnActionTable.add(attackB).width(200).height(50);
		turnActionTable.row();
		turnActionTable.add(spellB).width(200).height(50);
		turnActionTable.row();
		turnActionTable.add(defendB).width(200).height(50);
		turnActionTable.row();
		turnActionTable.add(runB).width(200).height(50);

		rootTable.add(chooseSpellTable).pad(5);
		rootTable.add(chooseEnemyTable).pad(5);

		rootTable.pack();
		rootTable.setX(1920/2 - (rootTable.getWidth() + 5));
		//rootTable.setY(rootTable.getHeight()/2);
	}

	private void addActionToTurn(Action a) {
		currentTurn.addAction(a);
		actions++;
		actionsL.setText(String.format("%d/%d Actions", actions, getMaxActionCount()));
		chooseEnemyTable.setVisible(false);
		if(actions >= getMaxActionCount()) {
			endTurn();
		}
	}

	private void endTurn() {
		getTurnManager().getTurns().add(currentTurn);
		currentTurn = new Turn();
		getTurnManager().start();

		chooseEnemyTable.setVisible(false);
		chooseSpellTable.setVisible(false);
		rootTable.setVisible(false);
		turnActionTable.setVisible(true);
		actions = 0;
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
	}

	@Override
	public void renderUi(float delta, SpriteBatch batch) {
		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void died() {
		Dialog dialog = new Dialog("You died!",  skin);
		dialog.text("You have no health left!");
		dialog.show(stage);
		System.out.println("palyer died");
	}

	public int getObjectPosIndex() {
		return objectPosIndex;
	}

	public void setObjectPosIndex(int objectPosIndex) {
		this.objectPosIndex = objectPosIndex;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public void setEnemyManager(EnemyManager enemyManager) {
		this.enemyManager = enemyManager;
	}
}
