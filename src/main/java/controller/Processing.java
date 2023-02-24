package controller;

import model.*;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;

public class Processing extends PApplet {
    String playerName = "";
    State state;
    String playerState = "";
    int[] blue = {0,0,255};
    int[] red = {255,0,0};
    int[] green = {0,255,0};
    int[] darkPink = {255,100,100};
    int[] lightPink = {255,200,200};
    int[] pink = {255,105,180};
    Robot player = new Robot(400,300,pink,this);
    ArrayList<Gem> gems = new ArrayList<>();
    ArrayList<Robot> robots = new ArrayList<>();
    PFont f;
    int lives = 3;
    int frame = 0;
    int multiplier = 1;
    int scoreGain = 0;
    ArrayList<HighScore> scores;
    public void settings(){
        size(800, 600);
        frameRate = 60;
        state = state.START;
    }

    public void setup(){
        for (int i = 0; i < 5; i++) {
            gems.add(new Gem(50,green,this));
        }
        f = createFont("Arial",32,true);
        HighScoreReader r = new HighScoreReader();
        scores = r.readHighScores();
    }

    public void draw(){
        switch (state){
            case START:
                drawStart();
                break;
            case GAME:
                drawGame();
                break;
            case END,TYPING:
                drawEnd();
                break;
            default:
                exit();
        }
    }

    public void drawStart(){
        background(0);
        fill(255);
        textFont(f);
        text("Press Space to start", (int)width/2, (int) height/2);
        String highScores = "";
        for (HighScore hs: scores) {
            highScores += hs.toString();
            highScores += "\n";
        }
        text(highScores,15,35);
    }

    public void drawGame(){
        //Timing stuff
        frame++;
        if (scoreGain > 1000){
            lives++;
            scoreGain = 0;
        }
        if (frame%180 == 0){
            frame = 0;
            multiplier++;
            if (random(0,1)>0.25){
                robots.add(robotGen());
            }
        }
        // Draws Background
        background(0,0,0);

        //Other preRender functions
        robots.removeIf(robot -> !robot.exists);
        gems.removeIf(g -> !g.exists);
        moveAiRobots();

        // Drawing
        gems.forEach(Gem::drawGem);
        player.drawRobot();
        robots.forEach(Robot::drawRobot);

        // Collision Detection, but bad
        for (int i = 0; i < gems.size(); i++) {
            Gem gem = gems.get(i);
            if (getdistance(player, gem)<=25){
                gems.remove(i);
                player.addScore(gem.value * multiplier);
                scoreGain += gem.value * multiplier;
            }
            for (Robot r: robots){
                if (getdistance(r,gem)<= 25){
                    if (gems.get(i).exists){
                        gems.get(i).exists = false;
                        r.addScore(gem.value);
                        gems.get(i).value = 0;
                    }
                }
            }
        }
        for (int i = 0; i < robots.size(); i++) {
            Robot r1 = robots.get(i);
            if (getdistance(player,r1)<= 30){
                multiplier = 1;
                robots.remove(i);
                player.addScore(r1.getScore()*multiplier);
                lives--;
            }
            for (Robot r2:robots){
                if (r1 == r2){
                    break;
                }
                if (getdistance(r1,r2)<30){
                    r1.addScore(r2.getScore());
                    for (i = 0; i < r1.colour.length;i++){
                        r1.colour[i] = (r1.colour[i]+r2.colour[i])/2;
                    }
                    r2.exists = false;
                }
            }
        }

        // UI Rendering
        fill(255);
        String scoreString = "" + player.getScore();
        String multString = multiplier + "x";
        String liveString = "Lives: " + lives;
        textFont(f);
        text(scoreString,10,40);
        text(multString, 10,80);
        text(liveString,10,120);

        // Creates Gems
        if (random(1) > 0.99f){
            gems.add(new Gem(100,blue,this));
        }

        // Checks End Condition
        if (gems.size() == 0 || lives == 0){
            if (gems.size() == 0){
                playerState = "win";
            }
            if (lives == 0){
                playerState = "lose";
            }
            state = State.END;
        }
    }

    public void drawEnd(){
        background(0);
        fill(255);
        textFont(f);
        int stator;
        if (playerState.equals("win")){
            stator = 1;
        }
        if (playerState.equals("lose")){
            stator = 0;
        }
        else stator = -1;
        switch (stator){
            case 1:
                text("You win!",(int)(0.5 * width), 150);
                break;
            case 0:
                text("You lose!",(int)(0.5 * width), 150);
                break;
            default:
                break;
        }
        var scoreString = "You scored " + player.getScore() + " points";
        text(scoreString,(int)(width * 0.5),250);
        text("Press e to leave \n Press r to play again \n Press y to add your name \n Press ENTER to confirm",(int)(width*0.3),350);
        if (state == State.TYPING){text("Your name: " + playerName, 50, 150);}
    }

    public void keyPressed(){
        switch (state){
            case START:
                switch (key){
                    case ' ':
                        state = State.GAME;
                }
            case GAME:
                switch (key) {
                    case 'w' -> player.moveUp();
                    case 's' -> player.moveDown();
                    case 'a' -> player.moveLeft();
                    case 'd' -> player.moveRight();
                }
                break;
            case END:
                switch (key){
                    case 'e':
                        exit();
                        break;
                    case 'r':
                        lives = 3;
                        player.resetScore();
                        player.resetPos();
                        gems = new ArrayList<Gem>();
                        gemRegen();
                        robots = new ArrayList<Robot>();
                        multiplier = 1;
                        state = State.START;
                        break;
                    case 'y':
                        state = State.TYPING;
                        break;
                }
            case TYPING:
                if (key == '\n'){
                    state = State.END;
                    HighScore hsNew = new HighScore(playerName,player.getScore());
                    scores.add(hsNew);;
                    HighScoreWriter w = new HighScoreWriter(scores);
                    w.writeHighScores();
                    playerName = "";
                    return;
                }
                if (key == BACKSPACE){
                    int last = playerName.length() - 1;
                    char[] chars = playerName.toCharArray();
                    playerName = "";
                    for (int i = 0; i < last - 1; i++) {
                        playerName += chars[i];
                    }
                    break;
                } else {
                    playerName += key;
                    break;
                }
        }
    }

    public void keyReleased(){
        switch (key) {
            case 'w', 's' -> player.yVel = 0;
            case 'a', 'd' -> player.xVel = 0;
        }
    }

    public static void main(String... args){
        PApplet.main("controller.Processing");
    }

    public int getdistance(Robot player, Entity target){
        int xDistance, yDistance, realDistance;
        xDistance = abs(player.x - target.x);
        yDistance = abs(player.y - target.y);
        realDistance = (int) (sqrt((xDistance*xDistance)+(yDistance*yDistance)));
        return realDistance;
    }

    public Robot robotGen(){
        int x,y;
        int[] colour = new int[3];
        x = (int) random(width);
        y = (int) random(height);
        for (int i = 0; i < 3; i++) {
            colour[i] = (int) random(255);
        }
        Robot robot = new Robot(x,y,colour,this);
        return robot;
    }

    /**
     * Moves all AI Robots one step towards the closest instance of Gem
     */
    private void moveAiRobots(){
        for(Robot r: robots){
            // Finds the closest gem
            Gem nearestGem = null;
            int minDist = Integer.MAX_VALUE;
            for (Gem g: gems){
                int currentDist = getdistance(r,g);
                if(currentDist < minDist){
                    nearestGem = g;
                }
            }
            if (nearestGem == null){
                return;
            }
            if (r.x > nearestGem.x){
                r.xVel = 0;
                r.moveLeft();
            }
            if (r.x < nearestGem.x){
                r.xVel = 0;
                r.moveRight();
            }
            if (r.y > nearestGem.y) {
                r.yVel = 0;
                r.moveUp();
            }
            if (r.y < nearestGem.y) {
                r.yVel = 0;
                r.moveDown();
            }
        }
    }

    private void gemRegen(){
        for (int i = 0; i < 5; i++) {
            gems.add(new Gem(50,green,this));
        }
    }
}
