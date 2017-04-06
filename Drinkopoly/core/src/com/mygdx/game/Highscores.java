package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

/**
 * Created by tomai on 21.1.2017..
 */

public class Highscores implements Screen {
    private Database db = DatabaseFactory.getNewDatabase("Drinkopoly.db", 1, null, null);
    private DatabaseCursor dc = null;

    private Stage stage;
    private Table highscoresTable;
    private BitmapFont highscoreTitleFont;
    private Label highscoreTitleLabel;
    private Label.LabelStyle highscoreTitleLabelStyle;
    private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setCatchBackKey(true);
        highscoreTitleFont = new BitmapFont(Gdx.files.internal("font/textfont.fnt"), false);
        highscoreTitleLabelStyle = new Label.LabelStyle();
        highscoreTitleLabelStyle.font = highscoreTitleFont;
        highscoreTitleLabel = new Label("Highscores", highscoreTitleLabelStyle);
        highscoreTitleLabel.setPosition(Gdx.graphics.getWidth()/2 - highscoreTitleLabel.getWidth()/2, Gdx.graphics.getHeight()
                - highscoreTitleLabel.getHeight());
        highscoresTable = new Table(skin);
        highscoresTable.setPosition(Gdx.graphics.getWidth()/2,
                Gdx.graphics.getHeight()/2 - highscoresTable.getHeight()/2);
        FetchPlayers();

        stage.addActor(highscoreTitleLabel);
        stage.addActor(highscoresTable);
    }

    private void FetchPlayers() {
        highscoresTable.clearChildren();
        highscoresTable.add("Nadimak").spaceRight(20).width(Gdx.graphics.getWidth()/4).getActor().setFontScale(1.2f);
        highscoresTable.add("Rezultat").width(Gdx.graphics.getWidth()/4).getActor().setFontScale(1.2f);
        highscoresTable.row();
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
           dc = db.rawQuery("SELECT * FROM HIGHSCORES ORDER BY Score DESC LIMIT 10");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while(dc.next()){
            if (dc.getInt(0) != 0){
                highscoresTable.add(dc.getString(1)).spaceRight(20).width(Gdx.graphics.getWidth()/4).getActor().setFontScale(1.2f);
                highscoresTable.add(""+dc.getInt(2)).width(Gdx.graphics.getWidth()/4).getActor().setFontScale(1.2f);
                highscoresTable.row();
            }
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(204,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
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
        highscoreTitleFont.dispose();
        skin.dispose();
    }
}
