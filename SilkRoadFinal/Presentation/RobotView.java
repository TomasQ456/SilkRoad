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
import javax.swing.Timer;

public class RobotView {
    private Robot robot;
    private SilkRoadView roadView;
    private Circle circle;
    private Timer blinkingTimer; 

    public RobotView(Robot robot, SilkRoadView roadView) {
        this.robot = robot;
        this.roadView = roadView;
        this.circle = new Circle();
        this.blinkingTimer = new Timer(500, e -> {
            if (circle.isVisible()) {
                circle.makeInvisible();
            } else {
                circle.makeVisible();
            }
        });
    }

    public Robot getRobot() {
        return robot;
    }

    public void draw() {
        int[] pos = roadView.coordsForLocation(robot.getLocation());
        int cellSize = roadView.getCellSize();
        int margin = roadView.getMargin();
        int baseX = margin + pos[1] * (cellSize + margin);
        int baseY = margin + pos[0] * (cellSize + margin);
        
        circle.changeSize(cellSize - 10);
        circle.setPosition(baseX + cellSize / 2, baseY + cellSize / 2);
        circle.changeColor(robot.getColor()); // Se asume que Robot tiene getColor()
        circle.makeVisible();
    }
    
    public void erase() {
        stopBlinking();
        circle.makeInvisible();
        circle.changeSize(0);
    }
    
    public void startBlinking() {
        if (!blinkingTimer.isRunning()) {
            blinkingTimer.start();
        }
    }

    public void stopBlinking() {
        if (blinkingTimer != null && blinkingTimer.isRunning()) {
            blinkingTimer.stop();
        }
        if (circle != null) {
            circle.makeVisible();
        }
    }
}
