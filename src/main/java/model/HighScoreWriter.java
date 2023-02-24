package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HighScoreWriter {
    private ArrayList<HighScore> list;
    private File file = new File("highScores.txt");

    public HighScoreWriter(ArrayList<HighScore> list){
        this.list = list;
    }

    public void writeHighScores(){
        try{
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("File was not created or unable to exist.");
        }
    }
}
