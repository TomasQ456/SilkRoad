 

import java.awt.*;
import java.awt.geom.*;
/**
 * Circle drawable for BlueJ shapes, with absolute positioning + getters.
 */
public class Circle {
    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    public Circle(){
        diameter = 30;
        xPosition = 0;
        yPosition = 0;
        color = "blue";
        isVisible = false;
    }

    public void makeVisible(){
        isVisible = true;
        draw();
    }

    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition,
                diameter, diameter));
            canvas.wait(10);
        }
    }

    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    public void slowMoveHorizontal(int distance){
        int delta = distance < 0 ? -1 : 1;
        if(distance < 0) distance = -distance;
        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    public void slowMoveVertical(int distance){
        int delta = distance < 0 ? -1 : 1;
        if(distance < 0) distance = -distance;
        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }

    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    public void setPosition(int x, int y){
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    public int getDiameter(){ 
        return diameter; 
    }
    
    public int getX(){ 
        return xPosition; 
    }
    
    public int getY(){ 
        return yPosition; 
    }
}
