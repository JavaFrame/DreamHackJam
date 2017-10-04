package ch.dhj.game.encounter.obj.objects;

import ch.dhj.game.encounter.obj.GObject;
import ch.dhj.game.player.AnimationSet;
import ch.dhj.game.player.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sebastian on 30.09.2017.
 */
public class Figure extends GObject {
	private String name;
	private int level;
	private int exp;
	private int totalExpToNextLevel;
	private int maxActionCount;
	private int lifes;
	private int maxLifes;
	private Array<Weapon> weapons = new Array<>();
	private Weapon meleeWeapon;
	private Weapon rangeWeapon;
	private Array<Weapon> spells = new Array<>(3);
	private AnimationSet animationSet;

	protected Label nameL;
	protected Label levelL;
	protected Label lifesL;
	protected Table infoTable;

	public Figure(Vector2 position, String name, AnimationSet animationSet) {
		super(position);
		this.name = name;
		this.animationSet = animationSet;
		//setTextureRegion(new Sprite(new Texture(Gdx.files.internal("textures/Johhny.png"))));
	}

	public Figure(Sprite texture, Vector2 position, Vector2 scale, String name, AnimationSet animationSet) {
		super(texture, position, scale);
		this.name = name;
		this.animationSet = animationSet;
	}

	@Override
	public void init() {
		createLifeInfos();
	}


	public void createLifeInfos() {
		TextureAtlas atlasButtons = new TextureAtlas("textures/defaultSkin.pack");
		Skin skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);

		infoTable = new Table(skin);

		nameL = new Label(getName(), skin);
		nameL.setFontScale(2);
		levelL = new Label(getLevel() + " lvl", skin);
		levelL.setFontScale(2);
		lifesL = new Label(getLifes() + "/" + getMaxLifes() + " health", skin);
		lifesL.setFontScale(2);

		infoTable.add(nameL);
		infoTable.row();
		infoTable.add(levelL);
		infoTable.row();
		infoTable.add(lifesL);
		infoTable.setPosition(getPosition().x+(getSize().x/4), getPosition().y+getSize().y + infoTable.getHeight());
	}

	public void drawLifeInfos(SpriteBatch batch) {
		/*levelL.setPosition(nameL.getX() + nameL.getGlyphLayout().width + 5, getPosition().y+getSize().y+80);
		nameL.draw(batch, 1f);
		levelL.draw(batch, 1f);
		lifesL.draw(batch, 1f);*/
		infoTable.setPosition(getPosition().x+(getSize().x/4), getPosition().y+getSize().y + infoTable.getHeight());
		infoTable.draw(batch, 1f);
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		if(isDead()) {
			if(!isAnnimationFinished()) {
				super.render(delta, batch);
			} else {
				died();
				getParent().remove(this);
			}
			return;
		}
		super.render(delta, batch);
		drawLifeInfos(batch);
	}

	@Override
	public void dispose() {
		
	}

	public void died() {

	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public int getTotalExpToNextLevel() {
		return totalExpToNextLevel;
	}

	public int getMaxActionCount() {
		return maxActionCount;
	}

	public int getLifes() {
		return lifes;
	}

	public boolean isDead() {
		return getLifes() <= 0;
	}

	public int getMaxLifes() {
		return maxLifes;
	}

	public Array<Weapon> getWeapons() {
		return weapons;
	}

	public Weapon getMeleeWeapon() {
		return meleeWeapon;
	}

	public Array<Weapon> getSpells() {
		return spells;
	}

	public AnimationSet getAnimationSet() {
		return animationSet;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setTotalExpToNextLevel(int totalExpToNextLevel) {
		this.totalExpToNextLevel = totalExpToNextLevel;
	}

	public void setMaxActionCount(int maxActionCount) {
		this.maxActionCount = maxActionCount;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}

	public void setMaxLifes(int maxLifes) {
		this.maxLifes = maxLifes;
	}

	public void setMeleeWeapon(Weapon meleeWeapon) {
		this.meleeWeapon = meleeWeapon;
	}

	public void applyDamage(int damage, Figure you) {
		setLifes(getLifes()-damage);
		lifesL.setText(getLifes() + "/" + getMaxLifes() + " health");
		if(damage > 0)
			setAnimation(getAnimationSet().encounterDamagedAnimation);
		if(getLifes() <= 0) {
			setAnimation(getAnimationSet().encounterDieAnimation);
			if(you instanceof Player) {
				if(this instanceof Enemy) {
					Enemy thisE = (Enemy) this;
					((Player) you).addExp(thisE.getExpToDrop());
				}
			}
		}
		if(getLifes() > getMaxLifes()) {
			setLifes(getMaxLifes());
		}
	}

	@Override
	public String toString() {
		return getName() + "(" + getLevel() + " lvl)";
	}

	public Weapon getRangeWeapon() {
		return rangeWeapon;
	}

	public void setRangeWeapon(Weapon rangeWeapon) {
		this.rangeWeapon = rangeWeapon;
	}
}
