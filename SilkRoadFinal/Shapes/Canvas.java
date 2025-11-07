package Shapes;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Canvas {
    private static Canvas canvasSingleton;

    /**
     * Factory method to create the canvas singleton object with a specific size.
     * THIS MUST BE CALLED ONCE by the main view before any shapes are drawn.
     * @param title The title for the window.
     * @param width The desired width for the canvas.
     * @param height The desired height for the canvas.
     */
    public static void createCanvas(String title, int width, int height) {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas(title, width, height, Color.white);
        } else {
            // Optional: Handle if someone tries to create it again.
            System.out.println("Canvas already exists. Ignoring new size settings.");
        }
    }

    /**
     * Factory method to get the canvas singleton object.
     * Throws an exception if the canvas has not been created yet.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            // This fallback creates a default canvas if createCanvas was never called.
            createCanvas("BlueJ Shapes Demo", 300, 300);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    // ----- instance part -----
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /**
     * Private constructor for the singleton pattern.
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    // ... (El resto de los métodos de Canvas no cambian)
    public void setVisible(boolean visible){
        if(graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    public void draw(Object referenceObject, String color, Shape shape){
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
 
    public void erase(Object referenceObject){
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    public void setForegroundColor(String colorString){
        if(colorString.equals("red"))
            graphic.setColor(Color.red);
        else if(colorString.equals("black"))
            graphic.setColor(Color.black);
        else if(colorString.equals("blue"))
            graphic.setColor(Color.blue);
        else if(colorString.equals("yellow"))
            graphic.setColor(Color.yellow);
        else if(colorString.equals("green"))
            graphic.setColor(Color.green);
        else if(colorString.equals("magenta"))
            graphic.setColor(Color.magenta);
        else if(colorString.equals("white"))
            graphic.setColor(Color.white);
        else
            graphic.setColor(Color.black);
    }

    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
            // ignoring exception at the moment
        }
    }

    private void redraw(){
        erase();
        for(Iterator i=objects.iterator(); i.hasNext(); ) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
    
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    private class ShapeDescription {
        private Shape shape;
        private String colorString;
        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }
        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.fill(shape);
        }
    }
    // Add this method to the Canvas.java file

    public JFrame getFrame() {
        return this.frame;
    }
}