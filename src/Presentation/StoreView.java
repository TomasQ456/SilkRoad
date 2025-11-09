package Presentation;
import Shapes.*;
import Domain.*;

/**
 * Componente visual que maneja la representación gráfica de las tiendas en el tablero.
 * Esta clase implementa la visualización de los diferentes tipos de tiendas y sus
 * estados, actuando como puente entre el modelo de dominio y la interfaz gráfica.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Representa cada tienda como un triángulo con color distintivo</li>
 *   <li>Refleja visualmente el estado de los recursos de la tienda</li>
 *   <li>Coordina la posición de la tienda en el tablero</li>
 *   <li>Maneja la visibilidad y el tamaño de la representación</li>
 * </ul>
 */
public class StoreView {
    /** Tienda del dominio que esta vista representa */
    private Store store;
    /** Vista principal del tablero que contiene esta tienda */
    private SilkRoadView roadView;
    /** Forma geométrica que representa visualmente a la tienda */
    private Triangle triangle;
    
    /**
     * Crea una nueva vista para una tienda específica.
     * 
     * <p>Inicializa la representación visual de la tienda usando un triángulo,
     * que permite diferenciarla fácilmente de los robots (círculos) en el tablero.
     * 
     * @param store Tienda del dominio a representar
     * @param roadView Vista principal del tablero
     */
    public StoreView(Store store, SilkRoadView roadView) {
        this.store = store;
        this.roadView = roadView;
        // La forma depende del tipo de tienda (para la extensibilidad C4).
        // Se usa Triangle por defecto.
        this.triangle = new Triangle(); 
    }

    /**
     * Dibuja la representación visual de la tienda en el tablero.
     * 
     * <p>El proceso de dibujado:
     * <ol>
     *   <li>Calcula la posición exacta basada en la ubicación lógica</li>
     *   <li>Ajusta el tamaño del triángulo según las dimensiones de la celda</li>
     *   <li>Aplica el color según el tipo y estado de la tienda</li>
     *   <li>Hace visible la representación</li>
     * </ol>
     * 
     * <p>El color de la tienda cambia a blanco cuando está vacía,
     * proporcionando retroalimentación visual sobre su estado.
     */
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
            color = "white"; // Indica visualmente que la tienda está vacía
        }
        triangle.changeColor(color);
        triangle.makeVisible();
    }
    
    /**
     * Oculta la representación visual de la tienda.
     * Elimina completamente la visualización reduciendo el tamaño
     * a cero y haciéndola invisible.
     */
    public void erase() {
        triangle.makeInvisible();
        triangle.changeSize(0, 0);
    }
}