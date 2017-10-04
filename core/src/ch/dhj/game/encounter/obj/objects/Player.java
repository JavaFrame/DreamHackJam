package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.Action;
import ch.dhj.game.encounter.Turn;
import ch.dhj.game.encounter.TurnManager;
import ch.dhj.game.encounter.actions.MeleeWeaponAction;
import ch.dhj.game.encounter.actions.RangeWeaponAction;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.screens.EncounterScreen;
import ch.dhj.game.screens.MainMenu;
import ch.dhj.game.screens.OverworldScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

	private StringBuffer levelChangeReport = new StringBuffer();

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

	private boolean showEncounterWonDialog = false;

	private int oldExp;
	private int oldLvl;
	private int gottenExp;

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
		oldLvl = getLevel();
		oldExp = getExp();

		getTurnManager().addRoundDoneListener(new TurnManager.TurnManagerListener() {
			@Override
			public void triggered(TurnManager manager) {

				if(isDead()) {
					Dialog dialog = new Dialog("You died!",  skin);
					dialog.text("You have no health left!");
					TextButton restartGameButton = new TextButton("Back to Main Menu", skin);
					final Dialog finalDialog = dialog;
					restartGameButton.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							finalDialog.hide();
							Player.this.setMaxLifes(10);
							Player.this.setLifes(10);
							Player.this.setMaxActionCount(3);
							Player.this.setLevel(1);
							setExp(0);
							Player.this.setTotalExpToNextLevel(10);
							Player.this.getWeapons().clear();
							Player.this.setMeleeWeapon(new Weapon(Weapon.WeaponType.Stab));
							Player.this.setRangeWeapon(null);
							setObjectPosIndex(1);
							getWeapons().add(new Weapon(Weapon.WeaponType.Stab));
                            EncounterScreen.encounterMusic.stop();
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu(getEncounterScreen().getAssetManager(), getEncounterScreen().getBatch(), Player.this , enemyManager));
						}
					});
					dialog.button(restartGameButton);
					dialog.show(stage);
					return;
				}
				currentTurn = new Turn();
				actions = 0;
				turnActionTable.setVisible(true);
				rootTable.setVisible(true);
			}
		});
	}

	private void encounterWon() {
		final Dialog newWeaponDialg = new Dialog("You got a new Weapon!", skin);
		TextButton newWeaponCloseB = new TextButton("Go to Overworld!", skin);
		newWeaponCloseB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				newWeaponDialg.hide();
				EncounterScreen.encounterMusic.stop();
				((Game) Gdx.app.getApplicationListener()).
						setScreen(new OverworldScreen(getEncounterScreen().getAssetManager(), getEncounterScreen().getBatch(), Player.this, enemyManager, false));
			}
		});
		newWeaponDialg.button(newWeaponCloseB);

		Dialog levlUpDialog = null;
		levlUpDialog = new Dialog("Level up!", skin);
		levlUpDialog.getContentTable().add(new Label(levelChangeReport, skin));

		final TextButton closeLvlUpDialog = new TextButton("close", skin);
		final Dialog finalLevelUpDialog = levlUpDialog;
		closeLvlUpDialog.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				finalLevelUpDialog.hide();
				int level = getLevel();
				Weapon newWeapon = addWeapon(level);

				if (newWeapon != null) {
					if (!newWeapon.isSpell()) {
						newWeaponDialg.add(new Image(newWeapon.getIcon()));
						newWeaponDialg.text("You got a " + newWeapon.getName() + "!");
					} else {
						newWeaponDialg.text("You got a " + newWeapon.getName() + " Spell!");
					}
					newWeaponDialg.show(stage);
				} else {
					EncounterScreen.encounterMusic.stop();
					((Game) Gdx.app.getApplicationListener()).
							setScreen(new OverworldScreen(getEncounterScreen().getAssetManager(), getEncounterScreen().getBatch(), Player.this, enemyManager, false));
				}
				}
			});


			Dialog wonDialog = null;
			wonDialog = new Dialog("You won!", skin);
			wonDialog.text(String.format("You got %d exp!", gottenExp));
			gottenExp = 0;
			TextButton closeB = new TextButton("close", skin);
			final Dialog finalWonDialog = wonDialog;
			closeB.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					finalWonDialog.hide();
					if (oldLvl != getLevel()) {
						finalLevelUpDialog.button(closeLvlUpDialog);
						finalLevelUpDialog.show(stage);
					} else {
						EncounterScreen.encounterMusic.stop();
						((Game) Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(getEncounterScreen().getAssetManager(), getEncounterScreen().getBatch(), Player.this, enemyManager, false));
					}
				}
			});
			wonDialog.button(closeB);
			wonDialog.show(stage);
			oldExp = getExp();
	}

	private Weapon addWeapon(int level) {
		Weapon newWeapon = null;
		switch (level) {
			case 3:
			case 4:
				if (!Player.this.getSpells().contains(new Weapon(Weapon.WeaponType.Heal), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Heal);
					getSpells().add(newWeapon);
				}
				break;
			case 5:
			case 6:
				if (!Player.this.getSpells().contains(new Weapon(Weapon.WeaponType.Heal), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Heal);
					getSpells().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Gun), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Gun);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Gun.png")));
					getWeapons().add(newWeapon);
				}
				break;
			case 7:
			case 8:
				if (!Player.this.getSpells().contains(new Weapon(Weapon.WeaponType.Heal), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Heal);
					getSpells().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Gun), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Gun);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Gun.png")));
					getWeapons().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Flamethrower), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Flamethrower);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Flamethrower_gun_mit_orange.png")));
					getWeapons().add(newWeapon);
				}
				break;
			case 9:
			case 10:
			case 11:
				if (!Player.this.getSpells().contains(new Weapon(Weapon.WeaponType.Heal), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Heal);
					getSpells().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Gun), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Gun);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Gun.png")));
					getWeapons().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Flamethrower), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Flamethrower);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Flamethrower_gun_mit_orange.png")));
					getWeapons().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Shotgun), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Shotgun);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/shotguunGun.png")));
					getWeapons().add(newWeapon);
				}
				break;
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
				if (!Player.this.getSpells().contains(new Weapon(Weapon.WeaponType.Heal), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Heal);
					getSpells().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Gun), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Gun);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Gun.png")));
					getWeapons().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Flamethrower), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Flamethrower);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/Flamethrower_gun_mit_orange.png")));
					getWeapons().add(newWeapon);
				}
				if (!Player.this.getWeapons().contains(new Weapon(Weapon.WeaponType.Shotgun), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Shotgun);
					newWeapon.setIcon(new Texture(Gdx.files.internal("textures/shotguunGun.png")));
					getWeapons().add(newWeapon);
				}
				if (!Player.this.getSpells().contains(new Weapon(Weapon.WeaponType.Fireball), true)) {
					newWeapon = new Weapon(Weapon.WeaponType.Fireball);
					getSpells().add(newWeapon);
				}
				break;
		}
		return newWeapon;
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

		final List<Figure> enemies = new List<>(skin);
		Array<Figure> choosableEnemies = new Array<>();
		choosableEnemies.addAll(getEncounterConfig().enemies);
		choosableEnemies.add(this);
		enemies.setItems(choosableEnemies);
		chooseEnemyTable.add(enemies);

		chooseEnemyTable.row();

		TextButton doButton = new TextButton("Do", skin);
		doButton.pad(10);
		doButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(selectedWeapon.isMelee()) { //weapon is a mele attack
					addActionToTurn(new MeleeWeaponAction(selectedWeapon, Player.this, new Figure[]{enemies.getSelected()}), selectedWeapon.getActionCost());
				} else { //weapon is a range weapon or Spell
					addActionToTurn(new RangeWeaponAction(selectedWeapon, Player.this, new Figure[]{enemies.getSelected()}), selectedWeapon.getActionCost());
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
				selectedWeapon = w;
				chooseEnemyTable.setVisible(!chooseEnemyTable.isVisible());
				//enemies.setItems(getEncounterConfig().enemies);
				Array<Figure> choosableEnemies = new Array<>();
				choosableEnemies.addAll(getEncounterConfig().enemies);
				choosableEnemies.add(Player.this);
				enemies.setItems(choosableEnemies);
			}
		});
		chooseSpellTable.add(weapons);
		chooseSpellTable.row();
		chooseSpellTable.add(chooseButton).width(100);
		chooseSpellTable.setVisible(false);

		//actions menu
		actionsL = new Label(String.format("%d/%d Actions", actions, getMaxActionCount()), skin);
		final TextButton attackB = new TextButton("Attack", skin);
		if(getMeleeWeapon() == null && getRangeWeapon() == null) {
			attackB.setTouchable(Touchable.disabled);
			attackB.setText(attackB.getText() + " (unavailbe)");
		}
		attackB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Array<Weapon> actionWeapons = new Array<>();

				if(getMeleeWeapon() != null)
				    actionWeapons.add(getMeleeWeapon());
				if(getRangeWeapon() != null)
                    actionWeapons.add(getRangeWeapon());
				weapons.setItems(actionWeapons);
				chooseSpellTable.setVisible(true);
			}
		});
		TextButton spellB = new TextButton("Spell", skin);
		if(getSpells().size == 0) {
			spellB.setText("Spell (unavaible)");
			spellB.setTouchable(Touchable.disabled);
		}
		spellB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				chooseSpellTable.setVisible(true);
				chooseEnemyTable.setVisible(false);
				weapons.setItems(getSpells());
			}
		});
		TextButton doNothingB = new TextButton("Do nothing", skin);
		doNothingB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				actions++;
				actionsL.setText(String.format("%d/%d Actions", actions, getMaxActionCount()));
				if(actions >= getMaxActionCount()) {
					endTurn();
				}
				chooseSpellTable.setVisible(false);
				chooseEnemyTable.setVisible(false);
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
					chances = 0.3f;
				} else if(level == getLevel()) {
					chances = 0.1f;
				} else if(level < getLevel()) {
					chances = 0.1f;
				}
				Dialog dialog = null;
				if(random <= chances) {
					dialog = new Dialog("Running successful!", skin);
					dialog.text("You got away with a black eye!");
					TextButton closeB = new TextButton("close", skin);
					final Dialog finalDialog = dialog;
					closeB.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							finalDialog.hide();
							EncounterScreen.encounterMusic.stop();
							((Game)Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(getEncounterScreen().getAssetManager(), getEncounterScreen().getBatch(), Player.this, enemyManager, false));
						}
					});
					dialog.button(closeB);
				} else {
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
					dialog.button(closeB);
				}
				if(dialog != null) {
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
		turnActionTable.add(doNothingB).width(200).height(50);
		turnActionTable.row();
		turnActionTable.add(runB).width(200).height(50);

		rootTable.add(chooseSpellTable).pad(5);
		rootTable.add(chooseEnemyTable).pad(5);

		rootTable.pack();
		rootTable.setX(5);
		rootTable.setY(1080-rootTable.getHeight() - 5);
		//rootTable.setY(rootTable.getHeight()/2);
	}

	private void addActionToTurn(Action a, int neededActions) {
		currentTurn.addAction(a);
		if(actions + neededActions > getMaxActionCount()) {
			Dialog toManyActions = new Dialog("Not enough ActionPoints!", skin);
			toManyActions.text("You already used " + actions + " ap of " + getMaxActionCount() + "!");
			toManyActions.text("The action you wanted to use needes " + neededActions + " ap!");
			toManyActions.button("Close");
			toManyActions.show(stage);
			chooseSpellTable.setVisible(false);
			chooseEnemyTable.setVisible(false);
			return;
		}
		actions += neededActions;
		actionsL.setText(String.format("%d/%d Actions", actions, getMaxActionCount()));
		chooseEnemyTable.setVisible(false);
		if(actions >= getMaxActionCount()) {
			endTurn();
		}
	}

	private void endTurn() {
		getTurnManager().getTurns().add(currentTurn);
		currentTurn = new Turn();

		for(Enemy e : getEncounterConfig().enemies) {
			Turn eTurn = new Turn();
			for(Action a : e.getActions()) {
				eTurn.addAction(a);
			}
			getTurnManager().getTurns().add(eTurn);
		}

		getTurnManager().start();

		chooseEnemyTable.setVisible(false);
		chooseSpellTable.setVisible(false);
		rootTable.setVisible(false);
		turnActionTable.setVisible(true);
		actions = 0;
	}

	public void addExp(int exp) {
		gottenExp += exp;
		setExp(getExp() + getTotalExpToNextLevel());
		if(getExp() >= getTotalExpToNextLevel()) {
			levelChangeReport = new StringBuffer();
			setExp(0);
			setTotalExpToNextLevel((int) (getTotalExpToNextLevel() * EXP_FACTOR));
			setLevel(getLevel() + 1);
			levelChangeReport.append(String.format("%d lvl -> %d lvl\n", getLevel()-1, getLevel()));

			int oldMaxLife = getMaxLifes();
			setMaxLifes((int) (getMaxLifes() * LIFE_FACTOR));
			setLifes(getMaxLifes());
			levelChangeReport.append(String.format("%d hp -> %d hp\n", oldMaxLife, getMaxLifes()));

			int oldActionCount = getMaxActionCount();
			setMaxActionCount((int) (getMaxActionCount() * ACTIONS_FACTOR));
			if(getMaxActionCount() > MAX_ACTIONS)
				setMaxActionCount(MAX_ACTIONS);
			levelChangeReport.append(String.format("%d ap -> %d ap\n", oldActionCount, getMaxActionCount()));
		}
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		super.render(delta, batch);
		if(getEncounterConfig().enemies.size == 0 && !showEncounterWonDialog) {
			showEncounterWonDialog = true;
			encounterWon();
		}
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
