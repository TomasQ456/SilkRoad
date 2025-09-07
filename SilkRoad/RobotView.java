import java.awt.*;


/**
 * RobotView
 * Representa un robot como un triángulo (shapes.Triangle).
 */
public class RobotView {
    private final Triangle tri;

    public RobotView(Point cell, double cellSize, int margin, int tenges) {
        int x = (int)(margin + cell.x * cellSize);
        int y = (int)(margin + cell.y * cellSize);

        tri = new Triangle();
        tri.changeSize((int)(cellSize * 0.6), (int)(cellSize * 0.6));
        tri.moveHorizontal(x - 60 + (int)(cellSize * 0.2));
        tri.moveVertical(y - 50 + (int)(cellSize * 0.2));
        tri.changeColor(tenges > 0 ? "blue" : "green");
        tri.makeVisible();
    }
}

