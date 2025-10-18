/**
 * Gestiona la representación gráfica de un objeto Store.
 * Esta clase se encarga de dibujar la tienda como un triángulo en el tablero
 * y de actualizar su apariencia según el estado del modelo (Store).
 */
public class StoreView {
    private Store store;
    private SilkRoadView roadView;
    private Triangle triangle; 
    
    /**
     * Constructor para la vista de una Tienda.
     * @param store La instancia del modelo Store que esta vista representará.
     * @param roadView La vista principal del tablero (SilkRoadView) para obtener
     * información sobre coordenadas y tamaño de celda.
     */
    public StoreView(Store store, SilkRoadView roadView) {
        this.store = store;
        this.roadView = roadView;
        this.triangle = new Triangle(); 
    }

    /**
     * Dibuja la tienda en su ubicación actual en el lienzo.
     * Calcula la posición y el tamaño del triángulo basándose en la celda del tablero
     * y lo hace visible con el color correspondiente de la tienda.
     */
    public void draw() {
        int[] pos = roadView.coordsForLocation(store.getLocation());
        int cellSize = roadView.getCellSize();
        int margin = roadView.getMargin();
        int baseX = margin + pos[1] * (cellSize + margin);
        int baseY = margin + pos[0] * (cellSize + margin);
        
        triangle.changeSize(cellSize - 6, cellSize - 6); // Ajusta el tamaño del triángulo
        // Posiciona el triángulo centrado en la celda
        triangle.setPosition(baseX + cellSize / 2, baseY + 5); 
        triangle.changeColor(store.getColor()); // El color depende de si la tienda tiene tenges
        triangle.makeVisible();
    }

    /**
     * Borra la tienda del lienzo haciéndola invisible.
     * Este método se llama antes de redibujar para reflejar un cambio de estado,
     * como cuando una tienda es vaciada o reabastecida.
     */
    public void erase() {
        if (triangle != null) {
            triangle.makeInvisible();
        }
    }
}