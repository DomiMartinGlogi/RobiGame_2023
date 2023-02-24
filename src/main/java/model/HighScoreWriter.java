package model;

import java.io.File;
import java.io.FileWriter;
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
            list.sort(HighScore::compareTo);
            if (!file.exists()){
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            for (HighScore hs:list){
                String line = hs.getPlayerName() + ";" + hs.getScore() + "\n";
                fw.write(line);
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("File was not created or unable to exist.");
        }
    }
}
