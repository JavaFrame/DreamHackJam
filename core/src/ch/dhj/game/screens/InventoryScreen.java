package ch.dhj.game.screens;

import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;

import java.util.Comparator;

import static ch.dhj.game.utils.WorldConfig.VIEWPORT_HEIGHT;
import static ch.dhj.game.utils.WorldConfig.VIEWPORT_WIDTH;

public class InventoryScreen  implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlasButtons;
    private TextureAtlas atlasBackground;
    protected Skin skin;
    private Sprite background;
    private AssetManager assetManager;
    private Player player;
    private TextureAtlas atlasPlayerImage;
    private float jonnyWaveDuration;
    private Animation jonnyWaveAnimation;
    private float jonnyWaveAnimationTime;
    private Image playerImage;

    public InventoryScreen(AssetManager am, SpriteBatch sb, Player p) {
        assetManager = am;
        batch = sb;
        player = p;

        assetManager.load("textures/defaultSkin.pack", TextureAtlas.class);
        assetManager.load("textures/atlasMainMenu.pack", TextureAtlas.class);
        assetManager.load("textures/jonnySprite.pack", TextureAtlas.class);
        assetManager.finishLoading();

        atlasButtons = assetManager.get("textures/defaultSkin.pack");
        skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);
        atlasBackground = assetManager.get("textures/atlasMainMenu.pack");

        background = new Sprite(atlasBackground.findRegion("Placeholder_titel"));

        atlasPlayerImage = assetManager.get("textures/jonnySprite.pack");

        jonnyWaveDuration = 1.0f / 1.5f;
        Array<TextureAtlas.AtlasRegion> jonnyWaveRegions = new Array<TextureAtlas.AtlasRegion>(atlasPlayerImage.getRegions());
        jonnyWaveRegions.sort(new Comparator<TextureAtlas.AtlasRegion>() {
            @Override
            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        jonnyWaveAnimation = new Animation(jonnyWaveDuration, jonnyWaveRegions, Animation.PlayMode.LOOP);
        jonnyWaveAnimationTime = Gdx.graphics.getDeltaTime();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT,camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, this.batch);
    }

    @Override
    public void show() {
        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table resumeButtonTable = new Table();
        //Set table to fill stage
        resumeButtonTable.setFillParent(true);
        resumeButtonTable.bottom();

        //Create buttons
        TextButton resumeButton = new TextButton("Back To Overworld", skin);

        resumeButton.pad(10,100,10,100);

        //Add listeners to buttons
        resumeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(assetManager, batch, player));
            }
        });

        //Add buttons to table
        resumeButtonTable.add(resumeButton).width(500);
        //Add table to stage
        stage.addActor(resumeButtonTable);

        Table mainInvTable = new Table();
        mainInvTable.setWidth(VIEWPORT_WIDTH/1.5f);
        mainInvTable.setHeight(VIEWPORT_HEIGHT/1.5f);
        mainInvTable.setPosition((VIEWPORT_WIDTH/2)-(mainInvTable.getWidth()/2),(VIEWPORT_HEIGHT/2)-(mainInvTable.getHeight()/2));
        mainInvTable.setDebug(true);

        Pixmap grayBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        grayBackground.setColor(Color.GRAY);
        grayBackground.fill();

        Pixmap lightGreenBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        lightGreenBackground.setColor(Color.LIME);
        lightGreenBackground.fill();

        Pixmap navyBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        navyBackground.setColor(Color.NAVY);
        navyBackground.fill();

        Table playerTable = new Table();
        playerTable.setWidth(mainInvTable.getWidth() * .3f);
        playerTable.setHeight(mainInvTable.getHeight() * .9f);

        Table playerImgTable = new Table();

        playerImage = new Image((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime));
        playerImgTable.add(playerImage).width(150).height(150);

        Label hp = new Label("Hp: ",skin);
        Label level = new Label("Level: ",skin);
        Label exp = new Label("EXP: ",skin);
        Label actions = new Label("Actions: ",skin);
        Label hpPlayer = new Label(player.getLifes() + "/" + player.getMaxLifes(),skin);
        Label levelPlayer = new Label(String.valueOf(player.getLevel()),skin);
        Label expPlayer = new Label(player.getExp() + "/" + player.getTotalExpToNextLevel(),skin);
        Label actionsPlayer = new Label(String .valueOf(player.getMaxActionCount()),skin);

        Table hpTable = new Table();
        hpTable.add(hp, hpPlayer);
        Table levelTable = new Table();
        levelTable.add(level,levelPlayer);
        Table expTable = new Table();
        expTable.add(exp,expPlayer);
        Table actionsTable = new Table();
        actionsTable.add(actions,actionsPlayer);

        Table statsTable = new Table();
        statsTable.add(expTable).width(150);
        statsTable.add(hpTable).width(150);
        statsTable.row();
        statsTable.add(levelTable).width(150);
        statsTable.add(actionsTable).width(150);

        Label equipedLabel = new Label("Equiped:", skin);

        Label meeleEquiped = new Label("Meele:", skin);
        Image meeleEquipedImg = new Image(/*TODO: Hier Bild von Equiped Meele*/);
        Label rangedEquiped = new Label("Ranged:", skin);
        Image rangedEquipedImg = new Image(/*TODO:  Bild von Equiped ranged*/);
        Label spell1Equiped = new Label("Spell 1:", skin);
        Image spell1EquipedImg = new Image(/*TODO: Hier Bild von Equiped Spell 1*/);
        Label spell2Equiped = new Label("Spell 2:", skin);
        Image spell2EquipedImg = new Image(/*TODO: Hier Bild von Equiped Spell 2*/);
        Label spell3Equiped = new Label("Spell 3:", skin);
        Image spell3EquipedImg = new Image(/*TODO: Hier Bild von Equiped Spell 3*/);
        Label spell4Equiped = new Label("Spell 3:", skin);
        Image spell4EquipedImg = new Image(/*TODO: Hier Bild von Equiped Spell 4*/);

        Table equipedItems = new Table();

        equipedItems.add(meeleEquiped).center().padRight(35);
        equipedItems.add(rangedEquiped).center().padLeft(35);
        equipedItems.row();
        equipedItems.add(meeleEquipedImg).center().padRight(35).width(50).height(50);
        equipedItems.add(rangedEquipedImg).center().padLeft(35).width(50).height(50);
        equipedItems.row();
        equipedItems.add(spell1Equiped).center().padRight(35);
        equipedItems.add(spell2Equiped).center().padLeft(35);
        equipedItems.row();
        equipedItems.add(spell1EquipedImg).center().padRight(35).width(50).height(50);
        equipedItems.add(spell2EquipedImg).center().padLeft(35).width(50).height(50);
        equipedItems.row();
        equipedItems.add(spell3Equiped).center().padRight(35);
        equipedItems.add(spell4Equiped).center().padLeft(35);
        equipedItems.row();
        equipedItems.add(spell3EquipedImg).center().padRight(35).width(50).height(50);
        equipedItems.add(spell4EquipedImg).center().padLeft(35).width(50).height(50);

        playerTable.row().padTop(35);
        playerTable.add(playerImgTable);
        playerTable.row().padTop(35);
        playerTable.add(statsTable);
        playerTable.row().padTop(150);
        playerTable.add(equipedLabel);
        playerTable.row();
        playerTable.add(equipedItems);

        Table inventoryTable = new Table();

        playerTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGreenBackground))));
        inventoryTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGreenBackground))));
        mainInvTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(grayBackground))));

        mainInvTable.top().left();
        mainInvTable.add(playerTable).padLeft(20).padTop(20);
        mainInvTable.add(inventoryTable);

        stage.addActor(mainInvTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jonnyWaveAnimationTime += Gdx.graphics.getDeltaTime();
        playerImage.setDrawable(new TextureRegionDrawable((TextureRegion) jonnyWaveAnimation.getKeyFrame(jonnyWaveAnimationTime)));

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.draw(batch);
        batch.end();

        stage.act();
        stage.draw();
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlasBackground.dispose();
        atlasButtons.dispose();
        batch.dispose();
        skin.dispose();
        stage.dispose();
    }}
