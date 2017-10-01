package ch.dhj.game.customcomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Sebastian on 01.10.2017.
 */
public class LoadingBar extends Image {
	private Animation<TextureRegion> animation;
	private TextureRegionDrawable drawable;

	private float max = 1;
	private float value;

	public LoadingBar(float max, float value) {
		Texture t = new Texture(Gdx.files.internal("textures/loading_bar.png"));
		TextureRegion[][] aniTextures = TextureRegion.split(t, 16, 4);
		this.animation = new Animation<TextureRegion>(1, aniTextures[0]);
		this.max = max;
		this.value = value;
		drawable = new TextureRegionDrawable(aniTextures[0][0]);
		setDrawable(drawable);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float stateTime = value * animation.getAnimationDuration() / max;
		TextureRegion reg = animation.getKeyFrame(stateTime);
		//batch.draw(reg, getX(), getY(), getWidth(), getHeight());
		drawable.setRegion(reg);
		super.draw(batch, parentAlpha);
	}

	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
}
