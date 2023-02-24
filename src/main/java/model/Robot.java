package model;

import processing.core.PApplet;

/**
 * Class to represent robot within the game
 * @author domimartinglogi
 */
public class Robot extends Entity {

    public int[] colour;
    PApplet window;
    public float xVel = 0f;
    public float yVel = 0f;
    float accel = 1.5f;
    float drag = 0.1f;
    private int score = 0;
    public boolean exists = true;

    /**
     * @param x Initial x coordinate
     * @param y Initial y coordinate
     * @param colour Colour to be set by an int[] where [R,G,B]
     * @param window Window in which it is to be displayed
     */
    public Robot(int x, int y, int[] colour,PApplet window) {
        this.x = x;
        this.y = y;
        this.colour = colour;
        this.window = window;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int i){
        score += i;
    }


    public void drawRobot(){
        window.rectMode(PApplet.CENTER);
        window.fill(colour[0],colour[1],colour[2]);
        window.rect(x,y,100,50);
        window.noStroke();
        window.fill(255);
        window.circle(x + 15,y,40);
        window.circle(x - 15,y,40);
        window.fill(0);
        window.circle(x+15,y,20);
        window.circle(x-15,y,20);
    }

    //All movement
    public void moveDown(){
        yVel += accel - (drag * yVel);
        y += (int)yVel;
        if (y > window.height){
            y = 1;
        }
    }

    public void moveUp(){
        yVel += accel - (drag * yVel);
        y -= (int)yVel;
        if (y < 0) {
            y = window.height - 1;
        }
    }

    public void moveRight(){
        xVel += accel - (drag * xVel);
        x += xVel;
        if (x > window.width){
            x = 1;
        }
    }

    public void moveLeft(){
        xVel += accel - (drag * xVel);
        x -= xVel;
        if (x < 0){
            x = window.width - 1;
        }
    }

}
