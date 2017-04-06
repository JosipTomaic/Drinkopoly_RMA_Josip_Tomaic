package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by tomai on 19.12.2016..
 */

public class MainMenu implements Screen {

    private Texture img;
    private TextureAtlas play_button_atlas;
    private TextureAtlas rules_button_atlas;
    private TextureAtlas highscores_button_atlas;
    private Skin play_button_skin;
    private Skin rules_button_skin;
    private Skin highscores_button_skin;
    private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private Stage stage;
    private TextButton button_play, button_rules, button_highscore;
    private BitmapFont white;
    private Image image;
    private TextButtonStyle buttonPlay, buttonRules, buttonHigscores;
    private Dialog dialog;

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        white = new BitmapFont(Gdx.files.internal("font/whiteFont.fnt"), false);
        img = new Texture("drinkopoly_logo.png");
        image = new Image(img);
        play_button_atlas = new TextureAtlas("drinkopoly.pack");
        play_button_skin = new Skin(play_button_atlas);
        rules_button_atlas = new TextureAtlas("rules.pack");
        rules_button_skin = new Skin(rules_button_atlas);
        highscores_button_atlas = new TextureAtlas("highscores.pack");
        highscores_button_skin = new Skin(highscores_button_atlas);

        image.setPosition((Gdx.graphics.getWidth()/2 - img.getWidth()/2), (Gdx.graphics.getHeight()/2) - img.getHeight()/2);

        buttonPlay = new TextButtonStyle();
        buttonPlay.up = play_button_skin.getDrawable("play_normal");
        buttonPlay.down = play_button_skin.getDrawable("play_pressed");
        buttonPlay.pressedOffsetX = 1;
        buttonPlay.pressedOffsetY = -1;
        buttonPlay.font = white;

        buttonRules = new TextButtonStyle();
        buttonRules.up = rules_button_skin.getDrawable("settings_normal");
        buttonRules.down = rules_button_skin.getDrawable("settings_pressed");
        buttonRules.pressedOffsetX = 1;
        buttonRules.pressedOffsetY = -1;
        buttonRules.font = white;

        buttonHigscores = new TextButtonStyle();
        buttonHigscores.up = highscores_button_skin.getDrawable("highscores_normal");
        buttonHigscores.down = highscores_button_skin.getDrawable("highscores_pressed");
        buttonHigscores.pressedOffsetX = 1;
        buttonHigscores.pressedOffsetY = -1;
        buttonHigscores.font = white;

        button_play = new TextButton("", buttonPlay);
        button_play.pad(20);
        button_play.setPosition(Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/10);
        button_play.setSize(Gdx.graphics.getWidth()*4/10, Gdx.graphics.getHeight()*3/10);
        button_play.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NumberOfPlayers());
            }
        });

        button_rules = new TextButton("", buttonRules);
        button_rules.pad(20);
        button_rules.setSize(Gdx.graphics.getWidth()*4/10, Gdx.graphics.getHeight()*3/10);
        button_rules.setPosition(Gdx.graphics.getWidth()/2 - button_rules.getWidth()/2, Gdx.graphics.getHeight()/10);
        button_rules.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameRules());
            }
        });

        button_highscore = new TextButton("", buttonHigscores);
        button_highscore.pad(20);
        button_highscore.setSize(Gdx.graphics.getWidth()*4/10, Gdx.graphics.getHeight()*3/10);
        button_highscore.setPosition(Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/30 + button_highscore.getWidth()), Gdx.graphics.getHeight()/10);
        button_highscore.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Highscores());
            }
        });

        dialog = new Dialog("Quit", skin){
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    Gdx.app.exit();
                }
            }
        };
        dialog.text("STVARNO ZELITE IZACI?");
        dialog.getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
        dialog.getContentTable().defaults().width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/5);
        dialog.button("DA", true).pad(Gdx.graphics.getWidth()/50);
        dialog.button("NE", false).pad(Gdx.graphics.getWidth()/50);
        dialog.getBackground().setMinHeight(Gdx.graphics.getWidth()/5);
        dialog.getBackground().setMinWidth(Gdx.graphics.getWidth()/2);

        stage.addActor(image);
        stage.addActor(button_play);
        stage.addActor(button_rules);
        stage.addActor(button_highscore);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dialog.show(stage);
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
        img.dispose();
        stage.dispose();
    }
}
