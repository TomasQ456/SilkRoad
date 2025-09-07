import java.awt.Point;
import java.util.*;


/**
 * SilkRoadView
 * 
 * Solo dibuja la vía continua como un conjunto de rectángulos del paquete shapes.
 */
public class SilkRoadView {
    private final int length;
    private final int canvasSize;
    private final int margin;
    private final Canvas canvas;
    private int dim;
    private double cellSize;
    private Map<Integer, Point> posMap;

    private final List<Rectangle> roadSegments = new ArrayList<>();

    public SilkRoadView(int length, int canvasSize, int margin) {
        this.length = length;
        this.canvasSize = canvasSize;
        this.margin = margin;
        this.canvas = Canvas.getCanvas();
        prepareLayout();
    }

    private void prepareLayout() {
        int cells = length + 1;
        dim = (int) Math.ceil(Math.sqrt(cells));
        int usable = canvasSize - 2 * margin;
        cellSize = (double) usable / dim;
        buildSpiralMap(cells, dim);
    }

    private void buildSpiralMap(int cells, int dim) {
        posMap = new HashMap<>();
        int[][] grid = new int[dim][dim];
        for (int[] row : grid) Arrays.fill(row, -1);

        int top = 0, left = 0, bottom = dim - 1, right = dim - 1;
        int idx = 0;
        while (top <= bottom && left <= right && idx < cells) {
            for (int c = left; c <= right && idx < cells; c++) grid[top][c] = idx++;
            top++;
            for (int r = top; r <= bottom && idx < cells; r++) grid[r][right] = idx++;
            right--;
            for (int c = right; c >= left && idx < cells; c--) grid[bottom][c] = idx++;
            bottom--;
            for (int r = bottom; r >= top && idx < cells; r--) grid[r][left] = idx++;
            left++;
        }

        for (int r = 0; r < dim; r++) {
            for (int c = 0; c < dim; c++) {
                if (grid[r][c] >= 0) posMap.put(grid[r][c], new Point(c, r));
            }
        }
    }


    public void drawRoad() {
        for (int i = 0; i <= length; i++) {
            Point cell = posMap.get(i);
            if (cell == null) continue;

            int x = (int)(margin + cell.x * cellSize);
            int y = (int)(margin + cell.y * cellSize);

            Rectangle road = new Rectangle();
            road.changeSize((int)cellSize, (int)cellSize);
            road.changeColor("black");
            road.moveHorizontal(x - 70);
            road.moveVertical(y - 60);
            road.makeVisible();

            roadSegments.add(road);
        }
    }

    public Map<Integer, Point> getPosMap() {
        return posMap;
    }

    public double getCellSize() {
        return cellSize;
    }
}

