package model;

import processing.core.PApplet;
import processing.core.PConstants;

import static java.lang.Math.random;


public class Gem extends Entity {
    public int value;
    PApplet window;

    public int[] colour;
    public boolean exists = true;

    public Gem(int value, int[] colour, PApplet window){
        x =(int) (random()*(window.width - 100)) + 50;
        y = (int) (random()*(window.height - 100)) + 50;
        this.value = value;
        this.colour = colour;
        this.window = window;
    }

    public void drawGem(){
        star((float) x,(float) y,10,20,5);
    }
    private void star(float x, float y, float radius1, float radius2, int npoints){
        float angle = PConstants.TWO_PI / npoints;
        float halfAngle = angle/(float)2.0;
        window.fill(this.colour[0],this.colour[1],this.colour[2]);
        window.stroke(this.colour[0],this.colour[1],this.colour[2]);
        // shapes in processing:
        // https://processing.org/reference/beginShape_.html
        window.beginShape();
        for (float a = 0; a < PConstants.TWO_PI; a += angle) {
            float sx = x + PApplet.cos(a) * radius2;
            float sy = y + PApplet.sin(a) * radius2;
            window.vertex(sx, sy);
            sx = x + PApplet.cos(a+halfAngle) * radius1;
            sy = y + PApplet.sin(a+halfAngle) * radius1;
            window.vertex(sx, sy);
        }
        window.endShape(PConstants.CLOSE);
    }
}
