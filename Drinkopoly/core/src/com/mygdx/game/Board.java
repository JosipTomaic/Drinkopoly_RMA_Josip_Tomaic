package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.util.Random;

public class Board implements Screen{

    private static final String SURPRISE_CARD_TEXT = "KUCA CASTI!";

    private Database db = DatabaseFactory.getNewDatabase("Drinkopoly.db", 1, null, null);
    private DatabaseCursor dc = null;

    private int width, height, diceNumber;
    private SpriteBatch batch;
    private Texture imgTexture, surpriseCardTexture, diceTexture, diceOne, diceTwo, diceThree, diceFour, diceFive, diceSix;
    private Image surpriseCard, dice;
    private CharSequence surpriseText;
    private BitmapFont text;
    private Stage stage;
    private TextButton showChallenge, addPlayerButton, giveUpButton;
    private Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private int playersCounter = 0, radius = 20, stepVertical, stepHorizontal, currentX, currentY;
    private ShapeRenderer playerTag;
    private Table currentPlayers;
    private List<Player> playersPlaying = new List<Player>(skin);
    private boolean hasStarted = false, throwingEnabled = true, positionSet = false;
    private String challengeText;
    private List<String> surprises = new List<String>(skin);
    private Sound playerSound;
    private Dialog dialog;

    public Board(){}

    public Board(int playerNum){
        NumberOfPlayers.CreateInstance().setPlayerNum(playerNum);
    }

    public Board(int playerscounter, List<Player> playersArray){
        this.playersCounter = playerscounter;
        this.playersPlaying = playersArray;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        width = Gdx.graphics.getWidth()/12;
        height = Gdx.graphics.getHeight()/5;
        stepVertical = height;
        stepHorizontal = width;
        batch = new SpriteBatch();
        imgTexture = new Texture("cardbackground.png");
        text = new BitmapFont(Gdx.files.internal("font/textfont.fnt"));
        diceOne = new Texture("one.png");
        diceTwo = new Texture("two.png");
        diceThree = new Texture("three.png");
        diceFour = new Texture("four.png");
        diceFive = new Texture("five.png");
        diceSix = new Texture("six.png");
        playerTag = new ShapeRenderer();

        surpriseText = SURPRISE_CARD_TEXT;
        surpriseCardTexture = new Texture("surprise_card.jpg");
        surpriseCard = new Image(surpriseCardTexture);
        surpriseCard.setHeight(Gdx.graphics.getHeight()/6);
        surpriseCard.setWidth(Gdx.graphics.getWidth()/5);
        surpriseCard.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - width),
                Gdx.graphics.getHeight() - (height + Gdx.graphics.getHeight()/6));
        surpriseCard.setTouchable(Touchable.disabled);
        surpriseCard.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Dialog("IZNENADENJE!", skin){
                    {
                        GetChallengesForSurpriseCard();
                        getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                        text(surprises.getItems().get(new Random().nextInt(surprises.getItems().size)));
                        button("OK", "").addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                if((playersPlaying.getSelectedIndex() + 1) == playersPlaying.getItems().size){
                                    playersPlaying.setSelected(playersPlaying.getItems().get(0));
                                    diceNumber = 0;
                                    throwingEnabled = true;
                                    positionSet = false;
                                    surpriseCard.setTouchable(Touchable.disabled);
                                    CheckAndPlayPlayerSound();
                                }
                                else{
                                    playersPlaying.setSelected(playersPlaying.getItems().get(playersPlaying.getSelectedIndex()+1));
                                    diceNumber = 0;
                                    throwingEnabled = true;
                                    positionSet = false;
                                    surpriseCard.setTouchable(Touchable.disabled);
                                    CheckAndPlayPlayerSound();
                                }
                                new Dialog("", skin){
                                    {
                                        getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                                        text("Na redu je igrac " + playersPlaying.getSelected().getPlayerNickname() + " :)");
                                        button("OK", "");
                                        getBackground().setMinWidth(Gdx.graphics.getWidth()/2);
                                        getBackground().setMinHeight(Gdx.graphics.getHeight()/2);
                                    }
                                }.show(stage);
                            }
                        });
                        getBackground().setMinWidth(Gdx.graphics.getWidth()/2);
                        getBackground().setMinHeight(Gdx.graphics.getHeight()/2);
                    }
                }.show(stage);
            }
        });

        diceTexture = new Texture("cardbackground.png");
        dice = new Image(diceTexture);
        dice.setHeight(Gdx.graphics.getHeight()/5);
        dice.setWidth(Gdx.graphics.getWidth()/6);
        dice.setPosition(Gdx.graphics.getWidth() - (width + dice.getWidth()),
                Gdx.graphics.getHeight() - (height + dice.getHeight()));

        showChallenge = new TextButton("POKAZI IZAZOV", skin);
        showChallenge.setTransform(true);
        showChallenge.setScale(1.2f);
        showChallenge.setDisabled(true);
        showChallenge.getLabel().setFontScale(0.8f);
        showChallenge.setWidth(Gdx.graphics.getWidth()/8);
        showChallenge.setHeight(Gdx.graphics.getHeight()/8);
        showChallenge.setPosition(Gdx.graphics.getWidth()/2 - showChallenge.getWidth()/2,
                Gdx.graphics.getHeight() - height - surpriseCard.getHeight());
        showChallenge.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new Dialog("Izazov!", skin){
                    {
                        GetChallengesFromDatabase();
                        getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                        text(challengeText);
                        button("OK", "").addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                if (playersPlaying.getSelected().getPlayerCurrentField() == 8 ||
                                        playersPlaying.getSelected().getPlayerCurrentField() == 16 ||
                                        playersPlaying.getSelected().getPlayerCurrentField() == 21 ||
                                        playersPlaying.getSelected().getPlayerCurrentField() == 29)
                                {
                                    new Dialog("Izazov!", skin){
                                        {
                                            getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                                            text("Otvori kartu sa iznenadenjem (slika lijevo) uz mnogo srece!");
                                            button("OK", "").addListener(new ChangeListener() {
                                                @Override
                                                public void changed(ChangeEvent event, Actor actor) {
                                                    surpriseCard.setTouchable(Touchable.enabled);
                                                }
                                            });
                                            getBackground().setMinWidth(Gdx.graphics.getWidth()/2);
                                            getBackground().setMinHeight(Gdx.graphics.getHeight()/2);
                                        }
                                    }.show(stage);
                                }
                                else if((playersPlaying.getSelectedIndex() + 1) == playersPlaying.getItems().size){
                                    playersPlaying.setSelected(playersPlaying.getItems().get(0));
                                    diceNumber = 0;
                                    throwingEnabled = true;
                                    positionSet = false;
                                    CheckAndPlayPlayerSound();
                                    new Dialog("", skin){
                                        {
                                            getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                                            //getContentTable().defaults().width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/5);
                                            text("Na redu je igrac " + playersPlaying.getSelected().getPlayerNickname() + " :)");
                                            button("OK", "");
                                            getBackground().setMinWidth(Gdx.graphics.getWidth()/2);
                                            getBackground().setMinHeight(Gdx.graphics.getHeight()/2);
                                        }
                                    }.show(stage);
                                }
                                else{
                                    playersPlaying.setSelected(playersPlaying.getItems().get(playersPlaying.getSelectedIndex()+1));
                                    diceNumber = 0;
                                    throwingEnabled = true;
                                    positionSet = false;
                                    CheckAndPlayPlayerSound();
                                    new Dialog("", skin){
                                        {
                                            getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                                            text("Na redu je igrac " + playersPlaying.getSelected().getPlayerNickname() + " :)");
                                            button("OK", "");
                                            getBackground().setMinWidth(Gdx.graphics.getWidth()/2);
                                            getBackground().setMinHeight(Gdx.graphics.getHeight()/2);
                                        }
                                    }.show(stage);
                                }
                            }
                        });
                        getBackground().setMinWidth(Gdx.graphics.getWidth()/2);
                        getBackground().setMinHeight(Gdx.graphics.getHeight()/2);
                    }
                }.show(stage);
            }
        });

        addPlayerButton = new TextButton("DODAJ IGRACA", skin);
        addPlayerButton.getLabel().setFontScale(1.0f);
        addPlayerButton.setWidth(Gdx.graphics.getWidth()/8);
        addPlayerButton.setHeight(Gdx.graphics.getHeight()/8);
        addPlayerButton.setPosition(Gdx.graphics.getWidth() - (width + (dice.getWidth()/2 + addPlayerButton.getWidth()/2)),
                Gdx.graphics.getHeight() - (height + addPlayerButton.getHeight() + dice.getHeight()));
        if(playersCounter == NumberOfPlayers.CreateInstance().getPlayerNum()){
            addPlayerButton.setDisabled(true);
        }
        addPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playersCounter++;
                ((Game)Gdx.app.getApplicationListener()).setScreen(new PlayerSettings(playersCounter, playersPlaying));
            }
        });

        giveUpButton = new TextButton("ODUSTANI", skin);
        giveUpButton.getLabel().setFontScale(1.2f);
        giveUpButton.setWidth(Gdx.graphics.getWidth()/8);
        giveUpButton.setHeight(Gdx.graphics.getHeight()/8);
        giveUpButton.setPosition(Gdx.graphics.getWidth() - (width + (dice.getWidth()/2 + giveUpButton.getWidth()/2)),
                Gdx.graphics.getHeight() - (height + giveUpButton.getHeight() + addPlayerButton.getHeight() + dice.getHeight()));
        giveUpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int selectedIndex = playersPlaying.getSelectedIndex();
                playersPlaying.getItems().removeIndex(selectedIndex);
                playersPlaying.getItems().removeValue(playersPlaying.getSelected(), true);
                db.setupDatabase();
                try {
                    db.openOrCreateDatabase();
                } catch (SQLiteGdxException e) {
                    e.printStackTrace();
                }
                try {
                    db.execSQL("INSERT INTO Highscores('Nickname','Score') VALUES ('" + playersPlaying.getSelected().getPlayerNickname()
                            + "','" + playersPlaying.getSelected().getPlayerCounter() + "');");
                    db.execSQL("DELETE FROM Players WHERE Nickname='" + playersPlaying.getSelected().getPlayerNickname() + "'");
                } catch (SQLiteGdxException e) {
                    e.printStackTrace();
                }
                try {
                    db.closeDatabase();
                } catch (SQLiteGdxException e) {
                    e.printStackTrace();
                }
                if (playersPlaying.getItems().size == 1){
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new Winner());
                }
                else if (selectedIndex == playersPlaying.getItems().size){
                    diceNumber = 0;
                    throwingEnabled = true;
                    positionSet = false;
                    playersPlaying.setSelected(playersPlaying.getItems().get(0));
                    CheckAndPlayPlayerSound();
                    new Dialog("", skin) {
                        {
                            getButtonTable().defaults().width(Gdx.graphics.getWidth() / 10).height(Gdx.graphics.getWidth() / 20);
                            text("Na redu je igrac " + playersPlaying.getSelected().getPlayerNickname() + " :)");
                            button("OK", "");
                            getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                            getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                        }
                    }.show(stage);
                }
                else{
                    diceNumber = 0;
                    throwingEnabled = true;
                    positionSet = false;
                    playersPlaying.setSelected(playersPlaying.getItems().get(selectedIndex));
                    CheckAndPlayPlayerSound();
                    new Dialog("", skin) {
                        {
                            getButtonTable().defaults().width(Gdx.graphics.getWidth() / 10).height(Gdx.graphics.getWidth() / 20);
                            text("Na redu je igrac " + playersPlaying.getSelected().getPlayerNickname() + " :)");
                            button("OK", "");
                            getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                            getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                        }
                    }.show(stage);
                }
            }
        });

        CheckButtonAvailability();

        currentPlayers = new Table(skin);
        currentPlayers.setPosition(Gdx.graphics.getWidth()/2 - currentPlayers.getWidth()/2,
                Gdx.graphics.getHeight()/2 - currentPlayers.getHeight()/2);

        dialog = new Dialog("Quit", skin){
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    currentPlayers.clear();
                    db.setupDatabase();
                    try {
                        db.openOrCreateDatabase();
                    } catch (SQLiteGdxException e) {
                        e.printStackTrace();
                    }
                    try {
                        db.execSQL("DELETE FROM Players");
                        playersPlaying.clear();
                    } catch (SQLiteGdxException e) {
                        e.printStackTrace();
                    }
                    try {
                        db.closeDatabase();
                    } catch (SQLiteGdxException e) {
                        e.printStackTrace();
                    }
                    Gdx.gl.glClearColor(1, 1, 1, 1);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                }
            }
        };
        dialog.text("ZELITE IZACI?");
        dialog.getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
        dialog.button("DA", true).pad(Gdx.graphics.getWidth()/50).setSize(Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/10);
        dialog.button("NE", false).pad(Gdx.graphics.getWidth()/50);
        dialog.getBackground().setMinHeight(Gdx.graphics.getWidth()/5);
        dialog.getBackground().setMinWidth(Gdx.graphics.getWidth()/2);

        stage.addActor(dice);
        stage.addActor(surpriseCard);
        stage.addActor(showChallenge);
        stage.addActor(addPlayerButton);
        stage.addActor(currentPlayers);
        stage.addActor(giveUpButton);

        if (playersPlaying.getItems().size == 0){
            new Dialog("Uputa", skin){
                {
                    getButtonTable().defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/20);
                    text("PRVO DODAJTE BROJ IGRACA KOLIKO STE ODABRALI NA PRETHODNOM EKRANU").center();
                    button("OK", "");
                    getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                    getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                }
            }.show(stage);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(204,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        int locationX = 0;
        int locationY = 0;
        batch.draw(imgTexture, locationX, locationY, width, height);
        for(int i=0; i<12; i++){
            locationX += width;
            batch.draw(imgTexture, locationX, locationY, width, height);
        }
        for(int i=0; i<5; i++){
            locationX = Gdx.graphics.getWidth() - width;
            locationY += height;
            batch.draw(imgTexture, locationX, locationY, width, height);
        }
        for(int i=0; i<11; i++){
            locationX -= width;
            locationY = Gdx.graphics.getHeight() - height;
            if(i == 10){
                batch.draw(imgTexture, 0, locationY, Gdx.graphics.getWidth() - width*11, height);
            }
            else {
                batch.draw(imgTexture, locationX, locationY, width, height);
            }
        }
        for(int i=0; i<5; i++){
            locationY -= height;
            locationX = 0;
            batch.draw(imgTexture, locationX, locationY, width, height);
        }
        text.draw(batch, surpriseText, Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - width),
                Gdx.graphics.getHeight() - (height + Gdx.graphics.getHeight()/6));
        batch.end();

        stage.act(delta);
        stage.draw();

        GetCurrentPlayersFromDatabase();

        if (playersPlaying.getItems().size == NumberOfPlayers.CreateInstance().getPlayerNum() || (playersPlaying.getItems().size != 1 && playersPlaying.getItems().size != 0)) {
            if (hasStarted == false && playersPlaying.getItems().size == NumberOfPlayers.CreateInstance().getPlayerNum()) {
                hasStarted = true;
                playersPlaying.setSelected(playersPlaying.getItems().get(0));
                for (int n = 0; n < playersPlaying.getItems().size; n++) {
                    SetUpPlayers(playersPlaying.getItems().get(n));
                }
                CheckAndPlayPlayerSound();
                new Dialog("", skin) {
                    {
                        getButtonTable().defaults().width(Gdx.graphics.getWidth() / 10).height(Gdx.graphics.getWidth() / 20);
                        text("Na redu je igrac " + playersPlaying.getSelected().getPlayerNickname() + " :)");
                        button("OK", "");
                        getBackground().setMinWidth(Gdx.graphics.getWidth() / 2);
                        getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
                    }
                }.show(stage);
            } else if (hasStarted == true){
                RollTheDice();
                CalculatePosition(playersPlaying.getSelected());
                for (int t = 0; t < playersPlaying.getItems().size; t++) {
                    DrawPlayerTag(playersPlaying.getItems().get(t));
                }
                if (playersPlaying.getSelected().getPlayerCurrentField() == 0 || throwingEnabled == true) {
                    showChallenge.setDisabled(true);
                } else showChallenge.setDisabled(false);
            }
        }

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
        batch.dispose();
        stage.dispose();
        imgTexture.dispose();
        surpriseCardTexture.dispose();
        diceTexture.dispose();
        diceOne.dispose();
        diceTwo.dispose();
        diceThree.dispose();
        diceFour.dispose();
        diceFive.dispose();
        diceSix.dispose();
        text.dispose();
    }

    private void RollTheDice(){
        float accX = Gdx.input.getAccelerometerX();
        float accY = Gdx.input.getAccelerometerY();
        float accZ = Gdx.input.getAccelerometerZ();
        double acc = Math.sqrt(accX * accX + accY * accY + accZ * accZ);
        if(acc > 15){
            if (throwingEnabled == true) {
                throwingEnabled = false;
                Gdx.input.vibrate(1000);
                diceNumber = MathUtils.random(1, 6);
                switch (diceNumber) {
                    case 1:
                        dice.setDrawable(new SpriteDrawable(new Sprite(diceOne)));
                        break;
                    case 2:
                        dice.setDrawable(new SpriteDrawable(new Sprite(diceTwo)));
                        break;
                    case 3:
                        dice.setDrawable(new SpriteDrawable(new Sprite(diceThree)));
                        break;
                    case 4:
                        dice.setDrawable(new SpriteDrawable(new Sprite(diceFour)));
                        break;
                    case 5:
                        dice.setDrawable(new SpriteDrawable(new Sprite(diceFive)));
                        break;
                    case 6:
                        dice.setDrawable(new SpriteDrawable(new Sprite(diceSix)));
                        break;
                }
            }
        }
    }

    private void SetUpPlayers(Player player) {
        player.setPlayerXPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() - width/2));
        player.setPlayerYPosition(Gdx.graphics.getHeight() - height/2);
        player.setPlayerCounter(0);
        player.setPlayerCurrentField(0);
        playerTag.begin(ShapeRenderer.ShapeType.Filled);
        playerTag.setColor(GetPlayerColor(player.getPlayerColor()));
        playerTag.circle(player.getPlayerXPosition(), player.getPlayerYPosition(), radius);
        playerTag.end();
    }

    private void CalculatePosition(Player selected) {
        if (positionSet == false && throwingEnabled == false) {
            currentX = 0;
            currentY = 0;
            positionSet = true;
            for (int z = 1; z <= diceNumber; z++) {
                selected.setPlayerCounter(selected.getPlayerCounter()+1);
                selected.setPlayerCurrentField(selected.getPlayerCurrentField()+1);
                if(selected.getPlayerCurrentField() >= 31){
                    selected.setPlayerCurrentField(1);
                }
                if (selected.getPlayerCurrentField() <= 11) {
                    currentX = selected.getPlayerXPosition() + stepHorizontal;
                    currentY = selected.getPlayerYPosition();
                } else if (selected.getPlayerCurrentField() >= 12 && selected.getPlayerCurrentField() <= 15) {
                    currentX = selected.getPlayerXPosition();
                    currentY = selected.getPlayerYPosition() - stepVertical;
                } else if (selected.getPlayerCurrentField() >= 16 && selected.getPlayerCurrentField() <= 26) {
                    currentX = selected.getPlayerXPosition() - stepHorizontal;
                    currentY = selected.getPlayerYPosition();
                } else {
                    currentX = selected.getPlayerXPosition();
                    currentY = selected.getPlayerYPosition() + stepVertical;
                }
                selected.setPlayerXPosition(currentX);
                selected.setPlayerYPosition(currentY);
            }
            RefreshPlayerCounter(selected);
        }
    }

    private void RefreshPlayerCounter(Player selected) {
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL("UPDATE Players SET Score=" + selected.getPlayerCounter() + " WHERE Nickname='" +
                    selected.getPlayerNickname() + "';");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    private void GetCurrentPlayersFromDatabase(){
        currentPlayers.clearChildren();
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        try {
            dc = db.rawQuery("SELECT * FROM Players ORDER BY Score DESC");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }while(dc.next()){
            if(dc.getInt(0) != 0){
                currentPlayers.add(dc.getString(1)).spaceRight(20);
                currentPlayers.add(dc.getString(2)).spaceRight(20);
                currentPlayers.add(dc.getString(3)).spaceRight(20);
                currentPlayers.add(""+dc.getInt(4));
                currentPlayers.row();
            }
            else{
                Gdx.app.log("prazna tablica", "TABLICA Players JE PRAZNA, KREIRAJ IGRACE...");
            }
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    private void GetChallengesFromDatabase(){
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            dc = db.rawQuery("SELECT Text FROM Challenges WHERE _id=" + playersPlaying.getSelected().getPlayerCurrentField());
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while(dc.next()){
            challengeText = String.valueOf(dc.getString(0));
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    private void GetChallengesForSurpriseCard(){
        db.setupDatabase();
        try {
            db.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        try {
            dc = db.rawQuery("SELECT Text FROM Challenges WHERE Text!='KARTA IZNENADENJA'");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (dc.next()){
            surprises.getItems().add(String.valueOf(dc.getString(0)));
        }
        try {
            db.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    private Color GetPlayerColor(String playerColor){
        if (playerColor.equals("Crna")){
            return Color.BLACK;
        }
        else if (playerColor.equals("Plava")){
            return Color.BLUE;
        }
        else if (playerColor.equals("Zelena")){
            return Color.GREEN;
        }
        else if (playerColor.equals("Zuta")){
            return Color.YELLOW;
        }
        else if (playerColor.equals("Bijela")){
            return Color.WHITE;
        }
        else return Color.RED;
    }

    private void DrawPlayerTag(Player player) {
        playerTag.begin(ShapeRenderer.ShapeType.Filled);
        playerTag.setColor(GetPlayerColor(player.getPlayerColor()));
        playerTag.circle(player.getPlayerXPosition(), player.getPlayerYPosition(), radius);
        playerTag.end();
    }

    private void CheckAndPlayPlayerSound(){
        if (playersPlaying.getSelected().getPlayerSound() == "Donkey"){
            playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/donkey.wav"));
        }
        else if (playersPlaying.getSelected().getPlayerSound() == "Fart"){
            playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/fart.mp3"));
        }
        else if (playersPlaying.getSelected().getPlayerSound() == "Monkey"){
            playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/monkey.mp3"));
        }
        else if (playersPlaying.getSelected().getPlayerSound() == "Cow"){
            playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/cow.wav"));
        }
        else if (playersPlaying.getSelected().getPlayerSound() == "Pikachu"){
            playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pikachu.wav"));
        }
        else if (playersPlaying.getSelected().getPlayerSound() == "Burp"){
            playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/burp.mp3"));
        }
        else playerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/kitten.wav"));

        playerSound.play(1.0f);
    }

    private void CheckButtonAvailability(){
        if (playersPlaying.getItems().size < NumberOfPlayers.CreateInstance().getPlayerNum()){
            showChallenge.setDisabled(true);
            giveUpButton.setDisabled(true);
        }
        else{
            showChallenge.setDisabled(false);
            giveUpButton.setDisabled(false);
        }
    }
}