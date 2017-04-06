package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.Array;

import javax.xml.crypto.Data;

import static com.badlogic.gdx.scenes.scene2d.Touchable.disabled;


public class PlayerSettings implements Screen {

    private Database db = DatabaseFactory.getNewDatabase("Drinkopoly.db",1, null, null);
    private DatabaseCursor dcNickname = null;
    private DatabaseCursor dcColor = null;
    private DatabaseCursor dcSound = null;

    private Stage stage;
    private Label nickname;
    private Label color;
    private Label sound;
    private TextArea nicknameInput;
    private String[] colorsList;
    private String[] soundsList;
    private SelectBox<String> colorsDropdown;
    private SelectBox<String> soundsDropdown;
    private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private BitmapFont labelsFont;
    private Label.LabelStyle labelsStyle;
    private Sound fart, donkey, monkey, cow, pikachu, burp, kitten;
    private TextButton okButton;
    private int j;
    private Player player = new Player();
    private List<Player> playersPlaying = new List<Player>(skin);
    private boolean exists = false;


    public PlayerSettings(int counter, List<Player> currentPlayers){
        this.j = counter;
        this.playersPlaying = currentPlayers;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        InitializeElements();
        setUpColors();
        setUpSounds();
        setUpElements();
        AddElementsToStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            ((Game) Gdx.app.getApplicationListener()).setScreen(new Board());
        }
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }

    private void AddElementsToStage() {
        stage.addActor(colorsDropdown);
        stage.addActor(soundsDropdown);
        stage.addActor(color);
        stage.addActor(nickname);
        stage.addActor(nicknameInput);
        stage.addActor(sound);
        stage.addActor(okButton);
    }

    private void setUpElements() {
        color.setPosition((Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()- (color.getWidth()*2))), Gdx.graphics.getHeight() - (colorsDropdown.getHeight()+50));
        nickname.setPosition((Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - (color.getWidth()*2))), Gdx.graphics.getHeight() - (colorsDropdown.getHeight()+80+nickname.getHeight()));
        nicknameInput.setPosition(Gdx.graphics.getWidth()/2 - colorsDropdown.getWidth()/2, Gdx.graphics.getHeight() - (colorsDropdown.getHeight()+80+nickname.getHeight()));
        nicknameInput.setWidth(Gdx.graphics.getWidth()/4);
        nicknameInput.setMaxLength(10);
        nicknameInput.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nicknameInput.setText("");
            }
        });

        sound.setPosition((Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - (color.getWidth()*2))), Gdx.graphics.getHeight() - (colorsDropdown.getHeight()+150+sound.getHeight()));
        okButton.setWidth(Gdx.graphics.getWidth()/8);
        okButton.setHeight(Gdx.graphics.getHeight()/8);
        okButton.setPosition(Gdx.graphics.getWidth()/2 - okButton.getWidth()/2, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - okButton.getHeight()));
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckEverythingAndInsert();
            }
        });
    }

    private void InitializeElements() {
        donkey = Gdx.audio.newSound(Gdx.files.internal("sounds/donkey.wav"));
        fart = Gdx.audio.newSound(Gdx.files.internal("sounds/fart.mp3"));
        monkey = Gdx.audio.newSound(Gdx.files.internal("sounds/monkey.mp3"));
        cow = Gdx.audio.newSound(Gdx.files.internal("sounds/cow.wav"));
        burp = Gdx.audio.newSound(Gdx.files.internal("sounds/burp.mp3"));
        pikachu = Gdx.audio.newSound(Gdx.files.internal("sounds/pikachu.wav"));
        kitten = Gdx.audio.newSound(Gdx.files.internal("sounds/kitten.wav"));
        labelsFont = new BitmapFont(Gdx.files.internal("font/textfont.fnt"), false);
        labelsStyle = new Label.LabelStyle();
        labelsStyle.font = labelsFont;
        color = new Label("Boja", labelsStyle);
        nickname = new Label("Nadimak", labelsStyle);
        nicknameInput = new TextArea("Ovdje upisite nadimak", skin);
        sound = new Label("Zvuk", labelsStyle);
        okButton = new TextButton("OK", skin);
    }

    private void setUpColors(){
        colorsList = new String[6];
        colorsList[0] = "Crna";
        colorsList[1] = "Plava";
        colorsList[2] = "Zelena";
        colorsList[3] = "Zuta";
        colorsList[4] = "Bijela";
        colorsList[5] = "Crvena";

        colorsDropdown = new SelectBox<String>(skin);
        colorsDropdown.setItems(colorsList);
        colorsDropdown.setSelected(colorsList[0]);
        colorsDropdown.setWidth(Gdx.graphics.getWidth()/4);
        colorsDropdown.setHeight(Gdx.graphics.getHeight()/7);
        colorsDropdown.setX(Gdx.graphics.getWidth()/2 - colorsDropdown.getWidth()/2);
        colorsDropdown.setY(Gdx.graphics.getHeight() - (colorsDropdown.getHeight()+50));

        colorsDropdown.addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                colorsDropdown.setSelected(colorsDropdown.getSelected());
                return true;
            }
        });
    }

    private void setUpSounds(){
        soundsList = new String[7];
        soundsList[0] = "Donkey";
        soundsList[1] = "Fart";
        soundsList[2] = "Monkey";
        soundsList[3] = "Cow";
        soundsList[4] = "Pikachu";
        soundsList[5] = "Burp";
        soundsList[6] = "Kitten";

        soundsDropdown = new SelectBox<String>(skin);
        soundsDropdown.setItems(soundsList);
        soundsDropdown.setSelected(soundsList[0]);
        soundsDropdown.setWidth(Gdx.graphics.getWidth()/4);
        soundsDropdown.setHeight(Gdx.graphics.getHeight()/7);
        soundsDropdown.setX(Gdx.graphics.getWidth()/2 - colorsDropdown.getWidth()/2);
        soundsDropdown.setY(Gdx.graphics.getHeight() - (colorsDropdown.getHeight()+150+soundsDropdown.getHeight()));

        soundsDropdown.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkSound();
            }
        });
    }

    private void checkSound(){
        if(soundsDropdown.getSelected() == soundsList[0]){
            donkey.play();
        }
        else if(soundsDropdown.getSelected() == soundsList[1]){
            fart.play();
        }
        else if(soundsDropdown.getSelected() == soundsList[2]){
            monkey.play();
        }
        else if(soundsDropdown.getSelected() == soundsList[3]){
            cow.play();
        }
        else if(soundsDropdown.getSelected() == soundsList[4]){
            pikachu.play();
        }
        else if(soundsDropdown.getSelected() == soundsList[5]){
            burp.play();
        }
        else{
            kitten.play();
        }
    }

    private void CheckEverythingAndInsert(){
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        if (playersPlaying.getItems().size != 0) {
            //PROVJERA BOJE, ZVUKA I NADIMKA
            try {
                dcColor = db.rawQuery("SELECT Color FROM Players WHERE Color='" + colorsDropdown.getSelected() +"';");
                dcSound = db.rawQuery("SELECT Sound FROM Players WHERE Sound='" + soundsDropdown.getSelected() +"';");
                dcNickname = db.rawQuery("SELECT Nickname FROM Players WHERE Nickname='" + nicknameInput.getText() +"';");
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
            if (dcColor.getCount() != 0 || dcSound.getCount() != 0 || dcNickname.getCount() != 0) {
                exists = true;
            }
            else exists = false;
        }
        if (nicknameInput.getText().toString().length() == 0){
            try {
                db.closeDatabase();
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
            new Dialog("Greska", skin){
                {
                    getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                    text("POLJE ZA UNOS NADIMKA NE SMIJE OSTATI PRAZNO!").center();
                    button("OK", "");
                    getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                    getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                }
            }.show(stage);
        }
        else if (exists){
            try {
                db.closeDatabase();
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
            new Dialog("Greska", skin){
                {
                    getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                    text("PODATKE KOJE STE UNIJELI I ODABRALI VEC KORISTI DRUGI IGRAC!").center();
                    button("OK", "");
                    getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                    getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                }
            }.show(stage);
        }
        else {
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            try {
                player.setPlayerNickname(nicknameInput.getText());
                player.setPlayerColor(colorsDropdown.getSelected());
                player.setPlayerSound(soundsDropdown.getSelected());
                player.setPlayerCounter(0);
                playersPlaying.getItems().add(player);
                db.execSQL("INSERT INTO Players('Nickname','Color','Sound','Score') VALUES (" + "'" + nicknameInput.getText() + "'" +
                        "," + "'" + colorsDropdown.getSelected() + "'" + "," + "'" + soundsDropdown.getSelected() + "','"
                        + player.getPlayerCounter() + "');");
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
            try {
                db.closeDatabase();
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
            ((Game) Gdx.app.getApplicationListener()).setScreen(new Board(j, playersPlaying));
        }
    }
}