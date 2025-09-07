import java.awt.Point;

public class StoreView {
    private final Rectangle rect;

    public StoreView(Point cell, double cellSize, int margin, int tenges) {
        int x = (int)(margin + cell.x * cellSize);
        int y = (int)(margin + cell.y * cellSize);

        rect = new Rectangle();
        rect.changeSize((int)(cellSize * 0.5), (int)(cellSize * 0.5));
        rect.moveHorizontal(x - 60 + (int)(cellSize * 0.25));
        rect.moveVertical(y - 50);
        rect.changeColor(tenges > 0 ? "magenta" : "black");
        rect.makeVisible();
    }

    public void setEmpty() {
        rect.changeColor("black");
    }
}
