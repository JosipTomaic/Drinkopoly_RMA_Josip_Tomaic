package com.mygdx.game;

import java.lang.*;


public class Player {

    public String playerColor;
    public String playerNickname;
    public String playerSound;
    public int playerXPosition;
    public int playerYPosition;
    public int playerCounter;
    public int playerCurrentField;

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public Player(){}

    public Player(String playernickname,String playercolor, String playersound, String playercounter){
        this.playerNickname = playernickname;
        this.playerColor = playercolor;
        this.playerSound = playersound;
        this.playerCounter = Integer.parseInt(playercounter);
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void setPlayerSound(String playerSound) {
        this.playerSound = playerSound;
    }

    public void setPlayerXPosition(int playerXPosition) {

        this.playerXPosition = playerXPosition;
    }

    public void setPlayerYPosition(int playerYPosition) {
        this.playerYPosition = playerYPosition;
    }

    public void setPlayerCounter(int playerCounter) {

        this.playerCounter = playerCounter;
    }

    public String getPlayerColor() {

        return playerColor;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public String getPlayerSound() {
        return playerSound;
    }

    public int getPlayerXPosition() {
        return playerXPosition;
    }

    public int getPlayerYPosition() {
        return playerYPosition;
    }

    public int getPlayerCounter() {
        return playerCounter;
    }

    public int getPlayerCurrentField() {
        return playerCurrentField;
    }

    public void setPlayerCurrentField(int playerCurrentField) {
        this.playerCurrentField = playerCurrentField;
    }
}