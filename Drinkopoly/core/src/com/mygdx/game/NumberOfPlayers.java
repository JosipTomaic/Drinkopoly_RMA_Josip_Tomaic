package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by tomai on 21.1.2017..
 */

public class NumberOfPlayers implements Screen {


    private Stage stage;
    private Label.LabelStyle mainTextStyle;
    private Label mainText;
    private BitmapFont fontTitle;
    private Image twoPlayers, threePlayers, fourPlayers, fivePlayers;
    private Texture twoPlayersTexture, threePlayersTexture, fourPlayersTexture, fivePlayersTexture;
    private Table scrollPaneTable;
    private ScrollPane playerNumScrollPane;
    private TextButton confirmationButton;
    private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    public int playerNum;

    private static NumberOfPlayers numbr = null;

    public NumberOfPlayers(){}

    public static NumberOfPlayers CreateInstance(){
        if(numbr == null){
            numbr = new NumberOfPlayers();
            return numbr;
        }
        return numbr;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerNum() {

        return playerNum;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        InitializeElements();
        SetUpPlayerChooser();
        SetUpScrollPane();
        SetUpConfirmButton();
        AddElementsToStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
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
        stage.addActor(mainText);
        stage.addActor(playerNumScrollPane);
        stage.addActor(confirmationButton);
        new Dialog("Odabrani broj igraca", skin){
            {
                getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                text("POMAKNITE KLIZAC PREMA DOLJE I DODIRNITE ZELJENI BROJ IGRACA (BROJ FIGURA OZNACAVA BROJ IGRACA)").center();
                button("OK", "");
                getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
            }
        }.show(stage);
    }

    private void SetUpConfirmButton() {
        confirmationButton.getLabel().setFontScale(1.2f);
        confirmationButton.setWidth(Gdx.graphics.getWidth()/8);
        confirmationButton.setHeight(Gdx.graphics.getHeight()/8);
        confirmationButton.setPosition(Gdx.graphics.getWidth()/2 - confirmationButton.getWidth()/2, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - mainText.getHeight() - scrollPaneTable.getHeight()));
        confirmationButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (playerNum == 0){
                    new Dialog("Greska", skin){
                        {
                            getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                            text("MORATE ODABRATI BROJ IGRACA KAKO BI NASTAVILI DALJE SA IGROM!").center();
                            button("OK", "");
                            getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                            getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                        }
                    }.show(stage);
                }
                else ((Game)Gdx.app.getApplicationListener()).setScreen(new Board(playerNum));
            }
        });
    }

    private void SetUpScrollPane() {
        playerNumScrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
        playerNumScrollPane.setPosition(Gdx.graphics.getWidth()/2 - playerNumScrollPane.getWidth()/2,
                Gdx.graphics.getHeight()/2 - playerNumScrollPane.getHeight()/2);
    }

    private void SetUpTable() {
        scrollPaneTable.add(twoPlayers);
        scrollPaneTable.row();
        scrollPaneTable.add(threePlayers);
        scrollPaneTable.row();
        scrollPaneTable.add(fourPlayers);
        scrollPaneTable.row();
        scrollPaneTable.add(fivePlayers);
    }

    private void SetUpPlayerChooser() {
        twoPlayers.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerNum(2);
                ShowDialog(getPlayerNum());
            }
        });

        threePlayers.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerNum(3);
                ShowDialog(getPlayerNum());
            }
        });

        fourPlayers.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerNum(4);
                ShowDialog(getPlayerNum());
            }
        });

        fivePlayers.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerNum(5);
                ShowDialog(getPlayerNum());
            }
        });
    }

    private void InitializeElements() {
        fontTitle = new BitmapFont(Gdx.files.internal("font/textfont.fnt"), false);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        mainTextStyle = new Label.LabelStyle();
        mainTextStyle.font = fontTitle;
        mainText = new Label("Odaberite broj igraca", mainTextStyle);
        mainText.setPosition(Gdx.graphics.getWidth()/2 - mainText.getWidth()/2, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/15);
        twoPlayersTexture = new Texture("twoplayers.PNG");
        threePlayersTexture = new Texture("threeplayers.PNG");
        fourPlayersTexture = new Texture("fourplayers.PNG");
        fivePlayersTexture = new Texture("fiveplayers.PNG");
        twoPlayers = new Image(twoPlayersTexture);
        threePlayers = new Image(threePlayersTexture);
        fourPlayers = new Image(fourPlayersTexture);
        fivePlayers = new Image(fivePlayersTexture);
        scrollPaneTable = new Table();
        SetUpTable();
        playerNumScrollPane = new ScrollPane(scrollPaneTable);
        confirmationButton = new TextButton("Potvrdi",skin);
    }

    private void ShowDialog(final int playernum){
        new Dialog("Odabrani broj igraca", skin){
            {
                getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                text("ODABRANI BROJ IGRACA JE " + playernum).center();
                button("OK", "");
                getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
            }
        }.show(stage);
    }
}
