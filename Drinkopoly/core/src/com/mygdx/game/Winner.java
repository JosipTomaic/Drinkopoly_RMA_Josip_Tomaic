package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

/**
 * Created by tomai on 15.3.2017..
 */

public class Winner implements Screen {

    private Database db = DatabaseFactory.getNewDatabase("Drinkopoly.db", 1, null, null);
    private DatabaseCursor dc = null;

    private Stage stage;
    private Label winnerNickname;
    private BitmapFont font;
    private Label.LabelStyle textStyle;
    private String winner;
    private TextButton newGameButton;
    private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

    public Winner() {}

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont(Gdx.files.internal("font/textfont.fnt"), false);
        textStyle = new Label.LabelStyle();
        textStyle.font = font;
        winnerNickname = new Label("Winner is " + FetchTheWinner(), textStyle);
        winnerNickname.setPosition(Gdx.graphics.getWidth()/2 - winnerNickname.getWidth()/2, Gdx.graphics.getHeight()/2 - winnerNickname.getHeight()/2);
        newGameButton = new TextButton("Igraj ponovo", skin);
        newGameButton.getLabel().setFontScale(1.2f);
        newGameButton.setWidth(Gdx.graphics.getWidth()/8);
        newGameButton.setHeight(Gdx.graphics.getWidth()/8);
        newGameButton.setPosition(Gdx.graphics.getWidth()/2 - newGameButton.getWidth()/2, Gdx.graphics.getHeight()/2
                - newGameButton.getHeight() - winnerNickname.getHeight());
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveWinnerToHighscores();
                DeleteWinnerFromPlayers();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        stage.addActor(winnerNickname);
        stage.addActor(newGameButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(204,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        font.dispose();
    }

    private String FetchTheWinner() {
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            dc = db.rawQuery("SELECT Nickname FROM Players");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (dc.next()) {
            winner = String.valueOf(dc.getString(0));
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        return winner;
    }

    private void DeleteWinnerFromPlayers(){
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL("DELETE FROM Players");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    private void SaveWinnerToHighscores(){
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            dc = db.rawQuery("SELECT * FROM Players");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while(dc.next()){
            try {
                db.execSQL("INSERT INTO Highscores('Nickname','Score') VALUES ('" + dc.getString(1) +  "','" + dc.getInt(2) + "');");
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
        }
    }

}
