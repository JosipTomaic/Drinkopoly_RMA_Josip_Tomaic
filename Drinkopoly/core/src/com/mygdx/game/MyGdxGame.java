package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;


public class MyGdxGame extends Game{

	private Database db;

	public static final String TABLE_PLAYERS = "Players";
	public static final String PLAYERS_COLUMN_ID = "_id";
	public static final String PLAYERS_COLUMN_PLAYER_NICKNAME = "Nickname";
	public static final String PLAYERS_COLUMN_PLAYER_COLOR = "Color";
	public static final String PLAYERS_COLUMN_PLAYER_SOUND = "Sound";
	public static final String PLAYERS_COLUMN_PLAYER_SCORE = "Score";

	public static final String TABLE_CHALLENGES = "Challenges";
	public static final String CHALLENGES_COLUMN_ID = "_id";
	public static final String CHALLENGES_COLUMN_TEXT = "Text";

	public static final String TABLE_HIGHSCORES = "Highscores";
	public static final String HIGHSCORES_COLUMN_ID = "_id";
	public static final String HIGHSCORES_COLUMN_PLAYER_NICKNAME = "Nickname";
	public static final String HIGHSCORES_COLUMN_PLAYER_SCORE = "Score";

	private static final String DATABASE_NAME = "Drinkopoly.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE_PLAYERS = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYERS +
			"(" + PLAYERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PLAYERS_COLUMN_PLAYER_NICKNAME + " TEXT," +
			PLAYERS_COLUMN_PLAYER_COLOR + " TEXT," + PLAYERS_COLUMN_PLAYER_SOUND + " TEXT," + PLAYERS_COLUMN_PLAYER_SCORE + " INTEGER);";

	private static final String CREATE_TABLE_CHALLENGES = "CREATE TABLE IF NOT EXISTS " + TABLE_CHALLENGES +
			 "(" + CHALLENGES_COLUMN_ID + " INTEGER PRIMARY KEY," + CHALLENGES_COLUMN_TEXT + " TEXT);";

	private static final String CREATE_TABLE_HIGHSCORES = "CREATE TABLE IF NOT EXISTS " + TABLE_HIGHSCORES +
			"(" + HIGHSCORES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + HIGHSCORES_COLUMN_PLAYER_NICKNAME + " TEXT," +
			HIGHSCORES_COLUMN_PLAYER_SCORE + " INTEGER);";
	@Override
	public void create () {
		db = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, CREATE_TABLE_PLAYERS, null);
		db.setupDatabase();
		try{
			db.openOrCreateDatabase();
			db.execSQL(CREATE_TABLE_PLAYERS);
			db.execSQL(CREATE_TABLE_CHALLENGES);
			db.execSQL(CREATE_TABLE_HIGHSCORES);
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('1', 'PIJE PLAVI IGRAC');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('2', 'BIRAS S KIM PIJES');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('3','PIJU SAMO CURE');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('4','CRVENI IGRAC PIJE DUPLO');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('5','BIRAJ TKO PIJE DUPLO');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('6','PIJE CRNI IGRAC');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('7','ZUTI I BIJELI PIJU');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('8','KARTA IZNENADENJA');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('9','PIJE ZELENI IGRAC')");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('10','NE PIJES DVA BACANJA');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('11','BIRTIJA - SVI PIJU NA EKS');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('12','ZUTI IGRAC PIJE');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('13','IGRAC TEBI LIJEVO ODREDUJE KOLIKO PIJES');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('14','PLAVI I BIJELI PIJU');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('15','KOJI SI BROJ DOBIO TOLIKO PUTA PIJES');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('16','KARTA IZNENADENJA');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('17','PIJU SAMO DECKI');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('18','POPIJ SVE STO IMAS U CASI');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('19','CRVENI I PLAVI PIJU');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('20','ZELENI I CRNI PIJU');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('21','KARTA IZNENADENJA');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('22','BIRAS TKO PIJE NA EKS');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('23','PIJE BIJELI IGRAC');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('24','ZELENI IGRAC PIJE DUPLO');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('25','IGRAC TEBI DESNO ODREDUJE KOLIKO PIJES');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('26','CRVENI I BIJELI PIJU');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('27','ODABERI TKO PIJE TRI PUTA');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('28','CRVENI IGRAC PIJE');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('29','KARTA IZNENADENJA');");
			db.execSQL("INSERT INTO Challenges ('_id','Text') VALUES ('30','NOVA RUNDA');");
			db.closeDatabase();
		}catch(SQLiteGdxException e){
			e.printStackTrace();
		}

		setScreen(new MainMenu());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		super.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}