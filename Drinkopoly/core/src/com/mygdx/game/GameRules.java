package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class GameRules implements Screen {

    private Stage stage;
    private BitmapFont rulesFont;
    private Label rulesText;
    private Label.LabelStyle rulesTextStyle;
    private Table table;

    @Override
    public void show() {
        stage = new Stage();
        rulesFont = new BitmapFont(Gdx.files.internal("font/rulesfont.fnt"), false);
        rulesTextStyle = new Label.LabelStyle();
        rulesTextStyle.font = rulesFont;
        rulesText = new Label("Na pocetku igre potrebno je dodirnuti tipku play.\n" +
                "Nakon toga potrebno je odabrati zeljeni broj igraca prema uputama koje se prikazu na ekranu i dodirnuti tipku Confirm.\n" +
                "Nakon sto se otvori ploca prvo je potrebno dodati onaj broj igraca koji je odabran dodirom tipke Dodaj igraca.\n" +
                "U izborniku za odabir karakteristika igraca moze se odabrati boja, upisati nadimak i odabrati zvuk za igraca.\n" +
                "Nakon sto su svi igraci dodani igra moze zapoceti.\n" +
                "Kockica se baca koristenjem akcelerometra i nakon toga se igrac pomice na odgovarajuce polje.\n" +
                "Nakon sto se pomakao mora dodirnuti tipku Pokazi izazov i obaviti trazeni izazov ukoliko se odnosi na njega i pritisnuti tipku OK.\n" +
                "Igrac koji je trenutno na potezu moze odustati u bilo kojem trenutku, a igra se odvija sve dok igraju vise od jednog igraca.\n" +
                "Igrac koji ostane zadnji je pobjednik.\n" +
                "Sretno i zabavite se! :)", rulesTextStyle);
        rulesText.setPosition(Gdx.graphics.getWidth() - rulesText.getWidth() + Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight() - rulesText.getHeight());
        rulesText.setWrap(true);

        table = new Table();
        table.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - table.getHeight() - rulesText.getHeight());
        table.add(rulesText).width(Gdx.graphics.getWidth());

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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

    }
}