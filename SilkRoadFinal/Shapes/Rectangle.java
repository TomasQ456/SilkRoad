package Shapes; 

import java.awt.*;
/**
 * Rectangle drawable with absolute positioning + getters.
 */
public class Rectangle {
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    public Rectangle(){
        height = 30;
        width = 40;
        xPosition = 0;
        yPosition = 0;
        color = "magenta";
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

    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition,
                                       width, height));
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

    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
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

    public int getWidth(){ 
        return width; 
    }
    
    public int getHeight(){ 
        return height; 
    }
    
    public int getX(){ 
        return xPosition; 
    }
    
    public int getY(){ 
        return yPosition; 
    }
}
