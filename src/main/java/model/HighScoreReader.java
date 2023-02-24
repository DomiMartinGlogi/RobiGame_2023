package model;

import java.io.*;
import java.util.ArrayList;

public class HighScoreReader {
    private File file = new File("highScores.txt");

    public ArrayList<HighScore> readHighScores(){
        ArrayList<HighScore> highScores = new ArrayList<HighScore>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            String line = "";
            while (bReader.ready()){
                line = bReader.readLine();
                HighScore score = toHighScore(line);
                highScores.add(score);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error : File not found.");
        } catch (IOException e) {
            System.out.println("Error : File not read.");
        }
        return highScores;
    }

    private HighScore toHighScore(String line){
        HighScore result;
        String[] vals =  line.split(";");
        result = new HighScore(vals[0], Integer.parseInt(vals[1]));
        return result;
    }
}
