 

import java.awt.*;
/**
 * Triangle drawable with absolute positioning + getters.
 */
public class Triangle {
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    public Triangle(){
        height = 30;
        width = 40;
        xPosition = 0;
        yPosition = 0;
        color = "green";
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
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
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

    public void setPosition(int apexX, int apexY){
        erase();
        xPosition = apexX;
        yPosition = apexY;
        draw();
    }

    public int getWidth(){ 
        return width; 
    }
    
    public int getHeight(){ 
        return height; 
    }
    
    public int getApexX(){ 
        return xPosition; 
    }
    
    public int getApexY(){ 
        return yPosition; 
    }
}
