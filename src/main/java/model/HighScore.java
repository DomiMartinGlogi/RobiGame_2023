package model;

import java.io.*;


/**
 * HighScore is a class to manage high scores in the game
 * It also handles the file highScores.json
 */
public class HighScore {
    private String playerName;
    private int score;

    public HighScore(String playerName,int score){
        this.playerName = playerName;
        this.score = score;
    }

    public int getScore() {
        return score;
    }
    public String getPlayerName(){
        return playerName;
    }

    @Override
    public String toString(){
        String returnString = "" + playerName + ":" + score;
        return returnString;
    }
}
