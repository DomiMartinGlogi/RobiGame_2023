package model;


/**
 * HighScore is a class to manage high scores in the game
 * It also handles the file highScores.json
 */
public class HighScore implements Comparable<HighScore> {
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

    @Override
    public int compareTo(HighScore hs){
        int r = 0;
        if (this.equals(hs)){
            r = 0;
        }
        if (this.score > hs.score){
            r =  1;
        }
        if (this.score == hs.score){
            r = this.playerName.compareTo(hs.playerName);
        }
        if (this.score < hs.score){
            r = -1;
        }
        return r;
    }
}
