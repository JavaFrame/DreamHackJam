package ch.dhj.game.screens;

import ch.dhj.game.EnemyManager;
import ch.dhj.game.encounter.obj.objects.Enemy;
import ch.dhj.game.encounter.obj.objects.Player;
import ch.dhj.game.player.Weapon;
import ch.dhj.game.player.spells.TestHealSpell;
import ch.dhj.game.player.spells.TestSpell;
import ch.dhj.game.player.weapons.TestWeapon;
import ch.dhj.game.utils.WorldConfig;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

import javax.swing.*;
import javax.swing.text.View;

import java.util.ArrayList;
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
    private final boolean[] isMeele = {false};
    private final boolean[] isRanged = {false};
    private final boolean[] isSpell = {false};
    private final int[] spellSlot = {0};
    private EnemyManager enemyManager;
    private Music invScreenMusic;

    public InventoryScreen(AssetManager am, SpriteBatch sb, Player p, EnemyManager em) {
        assetManager = am;
        batch = sb;
        player = p;
        enemyManager = em;

        assetManager.load("textures/defaultSkin.pack", TextureAtlas.class);
        assetManager.load("textures/atlasMainMenu.pack", TextureAtlas.class);
        assetManager.load("textures/jonnySprite.pack", TextureAtlas.class);
        assetManager.finishLoading();

        invScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Funny Song - Bensound (Royalty Free Music).mp3"));
        invScreenMusic.setLooping(true);
        invScreenMusic.setVolume(.3f);

        atlasButtons = assetManager.get("textures/defaultSkin.pack");
        skin = new Skin(Gdx.files.internal("textures/defaultSkin.json"), atlasButtons);
        atlasBackground = assetManager.get("textures/atlasMainMenu.pack");

        background = new Sprite(atlasBackground.findRegion("Title"));
        background.setSize(WorldConfig.VIEWPORT_WIDTH, WorldConfig.VIEWPORT_HEIGHT);

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

        invScreenMusic.play();
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
                invScreenMusic.stop();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new OverworldScreen(assetManager, batch, player, enemyManager, true));
            }
        });

        //Add buttons to table
        resumeButtonTable.add(resumeButton).width(500);
        //Add table to stage
        stage.addActor(resumeButtonTable);

        final Table mainInvTable = new Table();
        mainInvTable.setWidth(VIEWPORT_WIDTH/1.5f);
        mainInvTable.setHeight(VIEWPORT_HEIGHT/1.5f);
        mainInvTable.setPosition((VIEWPORT_WIDTH/2)-(mainInvTable.getWidth()/2),(VIEWPORT_HEIGHT/2)-(mainInvTable.getHeight()/2));

        Pixmap grayBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        grayBackground.setColor(Color.GRAY);
        grayBackground.fill();

        Pixmap lightGreenBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        lightGreenBackground.setColor(Color.rgba8888(.7f,.7f,.7f,1));
        lightGreenBackground.fill();

        Pixmap lightGrayBackground = new Pixmap(1,1, Pixmap.Format.RGB565);
        lightGrayBackground.setColor(Color.rgba8888(.65f,.65f,.65f,1));
        lightGrayBackground.fill();

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
        Image meeleEquipedImg = new Image();
        if(player.getMeleeWeapon() != null && player.getMeleeWeapon().getIcon() != null){
            meeleEquipedImg.setDrawable(new TextureRegionDrawable(new TextureRegion(player.getMeleeWeapon().getIcon())));
        }
        Label rangedEquiped = new Label("Ranged:", skin);
        Image rangedEquipedImg = new Image();
        if(player.getRangeWeapon() != null && player.getRangeWeapon().getIcon() != null){
            rangedEquipedImg.setDrawable(new TextureRegionDrawable(new TextureRegion(player.getRangeWeapon().getIcon())));
        }

        Table equipedItems = new Table();

        equipedItems.add(meeleEquiped).center().padRight(35);
        equipedItems.add(rangedEquiped).center().padLeft(35);
        equipedItems.row().padTop(45);
        equipedItems.add(meeleEquipedImg).center().padRight(35).width(50).height(50);
        equipedItems.add(rangedEquipedImg).center().padLeft(35).width(50).height(50);

        playerTable.row().padTop(35);
        playerTable.add(playerImgTable);
        playerTable.row().padTop(50);
        playerTable.add(statsTable);
        playerTable.row().padTop(200);
        playerTable.add(equipedLabel);
        playerTable.row().padTop(35);
        playerTable.add(equipedItems);

        Table inventoryTable = new Table();
        inventoryTable.left().padLeft(30);

        Array<Weapon> meeleWeapons = new Array<Weapon>();
        Array<Weapon> rangedWeapons = new Array<Weapon>();
        Array<Weapon> allWeapons = new Array<Weapon>();

        for(Weapon w : player.getWeapons()){
            if(!w.isSpell()){
                allWeapons.add(w);
            }
            if(w.isMelee()&& !w.isSpell()){
                meeleWeapons.add(w);
            }
            if(!w.isMelee() && !w.isSpell()){
                rangedWeapons.add(w);
            }
        }


        final Table weaponFilters = new Table();
        weaponFilters.top().left();

        TextButton meeleButton = new TextButton("Meele", skin);
        meeleButton.pad(10,20,10,20);
        meeleButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMeele[0] = true;
                isRanged[0] = false;
                hide();
            }
        });

        TextButton rangedButton = new TextButton("Ranged", skin);
        rangedButton.pad(10,20,10,20);
        rangedButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMeele[0] = false;
                isRanged[0] = true;
                hide();
            }
        });


        weaponFilters.add(meeleButton).pad(10,151,10,151);
        weaponFilters.add(rangedButton).pad(10,151,10,151);

        Table weaponList = new Table();

        if(isMeele[0]){
            for(Weapon w : meeleWeapons){
                Table item = createWeaponListItem(w);
                item.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGrayBackground))));
                weaponList.add(item).top().left();
                weaponList.row();
            }
        } else if (isRanged[0]){
            for(Weapon w : rangedWeapons){
                Table item = createWeaponListItem(w);
                item.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGrayBackground))));
                weaponList.add(item).top().left();
                weaponList.row();
            }
        } else {
            for(Weapon w : allWeapons){
                Table item = createWeaponListItem(w);
                item.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGrayBackground))));
                weaponList.add(item).top().left();
                weaponList.row();
            }
        }



        inventoryTable.pad(20,20,20,20).top().left();
        inventoryTable.add(weaponFilters).top().left();
        inventoryTable.row().padTop(10);
        inventoryTable.add(weaponList).top().left();

        weaponFilters.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGrayBackground))));
        playerTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGreenBackground))));
        inventoryTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(lightGreenBackground))));
        mainInvTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(grayBackground))));

        mainInvTable.top().left();
        mainInvTable.add(playerTable).padLeft(45).padTop(20).padBottom(30);
        mainInvTable.add(inventoryTable).top().padLeft(85).padTop(20).padBottom(30).padRight(40);

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
        show();
    }

    @Override
    public void dispose() {
        atlasBackground.dispose();
        atlasButtons.dispose();
        batch.dispose();
        skin.dispose();
        stage.dispose();
        invScreenMusic.dispose();
    }

    public Table createWeaponListItem(final Weapon w){
        Table item = new Table();
        Image icon = new Image();
        Label name = new Label("Test", skin);
        TextButton equipButton = new TextButton("Equip",skin);
        equipButton.pad(10,50,10,50);

        //Add listeners to buttons
        equipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(w != null) {
                    if (w.isMelee()) {
                        player.setRangeWeapon(player.getRangeWeapon());
                        player.setMeleeWeapon(new Weapon(w.getType()).setIcon(w.getIcon()));
                        hide();
                    } else {
                        player.setMeleeWeapon(player.getMeleeWeapon());
                        player.setRangeWeapon(new Weapon(w.getType()).setIcon(w.getIcon()));
                        hide();
                    }
                }
            }
        });

        if(w != null) {
            if (w.getName() != null && !w.getName().equals("")) {
                name.setText(w.getName());
            }

            if (w.getIcon() != null) {
                icon.setDrawable(new TextureRegionDrawable(new TextureRegion(w.getIcon())));
            }
        }

        item.add(icon).pad(10,20,10,50).width(100).height(100);
        item.add(name).pad(10,10,10,20).width(400);
        item.add(equipButton).pad(10,20,10,25);

        return item;
    }
}
