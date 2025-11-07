
package Presentation;
import Shapes.*;
import Shapes.Rectangle;
import Shapes.Canvas;
import Domain.*;
import Domain.Robot;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class SilkRoadView {
    private SilkRoad road;
    private int size;
    private int[][] spiral;
    private int cellSize = 40;
    private int margin = 5;

    private Rectangle profitBarBackground;
    private Rectangle profitBarCurrent;

    public SilkRoadView(SilkRoad road, int size, int[][] spiral) {
        this.road = road;
        this.size = size;
        this.spiral = spiral;
        
        // --- INICIO DE LA CORRECCIÓN ---

        // 1. Calcular las dimensiones del tablero y la posición de la barra.
        int boardWidth = size * (cellSize + margin) + margin;
        int lastRowY = margin + (size - 1) * (cellSize + margin);
        int boardBottom = lastRowY + cellSize;

        int barYPosition = boardBottom + 10; // Posicionar la barra 10px debajo del tablero
        
        // 2. Calcular la altura total necesaria para la ventana.
        int barHeight = 20;
        int bottomPadding = 20; // Dejar 20px de espacio debajo de la barra
        int windowHeight = barYPosition + barHeight + bottomPadding;

        // 3. Crear el canvas con las dimensiones calculadas.
        Canvas.createCanvas("Silk Road", boardWidth, windowHeight);

        // --- FIN DE LA CORRECCIÓN ---

        profitBarBackground = new Rectangle();
        profitBarCurrent = new Rectangle();
        
        int barWidth = size * (cellSize + margin);
        profitBarBackground.changeSize(barHeight, barWidth);
        profitBarBackground.setPosition(margin, barYPosition); // Usar la posición Y calculada
        profitBarBackground.changeColor("lightgray");

        profitBarCurrent.changeSize(barHeight, 0);
        profitBarCurrent.setPosition(margin, barYPosition); // Usar la posición Y calculada
        profitBarCurrent.changeColor("green");
    }
    
    public void showProfitBar() {
        if (profitBarBackground != null) {
            profitBarBackground.makeVisible();
        }
        if (profitBarCurrent != null) {
            profitBarCurrent.makeVisible();
        }
    }
    
    public void drawBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                int x = margin + j * (cellSize + margin);
                int y = margin + i * (cellSize + margin);

                Rectangle border = new Rectangle();
                border.changeSize(cellSize, cellSize);
                border.setPosition(x, y);
                border.changeColor("black");
                border.makeVisible();

                Rectangle fill = new Rectangle();
                fill.changeSize(cellSize - 2, cellSize - 2); 
                fill.setPosition(x + 1, y + 1);
                fill.changeColor("white");
                fill.makeVisible();
            }
        }
    }

    public void drawStore(Store store) {
        StoreView sv = new StoreView(store, this);
        store.setView(sv);
        sv.draw();
    }

    public void drawRobot(Robot robot) {
        RobotView rv = new RobotView(robot, this);
        robot.setView(rv);
        rv.draw();
    }

    public void eraseStore(Store store) {
        if(store.getView() != null) store.getView().erase();
    }

    public void eraseRobot(Robot robot) {
        if(robot.getView() != null) robot.getView().erase();
    }

    public void updateStore(Store store) {
        if(store.getView() != null) {
            store.getView().erase();
            store.getView().draw();
        }
    }

    public void updateRobot(Robot robot) {
        if(robot.getView() != null) {
            robot.getView().erase();
            robot.getView().draw();
        }
    }

    public int[] coordsForLocation(int loc) {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(spiral[i][j] == loc) return new int[]{i, j};
            }
        }
        return null;
    }

    public int getCellSize(){ 
        return cellSize; 
    }
    
    public int getMargin(){ 
        return margin; 
    }

    public void updateProfitBar(int currentProfit, int maxProfit) {
        if (profitBarCurrent == null) return;
        int barWidth = size * (cellSize + margin);
        int current = Math.max(0, currentProfit);
        int newWidth = maxProfit > 0 ? (int)(((double)current / maxProfit) * barWidth) : 0;
        
        newWidth = Math.min(newWidth, barWidth);
        
        profitBarCurrent.changeSize(profitBarCurrent.getHeight(), newWidth);
    }
    // This code goes inside SilkRoadView.java
    public JFrame getFrame() {
        return Canvas.getCanvas().getFrame();
    }
}

