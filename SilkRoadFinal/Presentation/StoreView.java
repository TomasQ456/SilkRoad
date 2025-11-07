package Presentation;
import Shapes.*;
import Domain.*;

public class StoreView {
    private Store store;
    private SilkRoadView roadView;
    private Triangle triangle;
    
    public StoreView(Store store, SilkRoadView roadView) {
        this.store = store;
        this.roadView = roadView;
        // La forma depende del tipo de tienda (para la extensibilidad C4).
        // Se usa Triangle por defecto.
        this.triangle = new Triangle(); 
    }

    public void draw() {
        int[] pos = roadView.coordsForLocation(store.getLocation());
        int cellSize = roadView.getCellSize();
        int margin = roadView.getMargin();
        int baseX = margin + pos[1] * (cellSize + margin);
        int baseY = margin + pos[0] * (cellSize + margin);
        
        triangle.changeSize(cellSize - 6, cellSize - 6);
        triangle.setPosition(baseX + cellSize / 2, baseY + 5);
        String color = store.getColor();
        if (store.getTenges() == 0) {
            color = "white"; 
        }
        triangle.changeColor(color);
        triangle.makeVisible();
    }
    
    public void erase() {
        triangle.makeInvisible();
        triangle.changeSize(0, 0);
    }
}
