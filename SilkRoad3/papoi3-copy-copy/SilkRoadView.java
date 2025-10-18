import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Gestiona la totalidad de la interfaz gráfica de usuario (GUI) para la simulación de Silk Road.
 * Esta clase es responsable de crear la ventana, dibujar el tablero, y coordinar la
 * visualización de los robots, las tiendas y la barra de progreso de ganancias.
 */
public class SilkRoadView {
    private SilkRoad road;
    private int size;
    private int[][] spiral;
    private int cellSize = 40;
    private int margin = 5;

    private Rectangle profitBarBackground;
    private Rectangle profitBarCurrent;

    /**
     * Constructor para la vista de Silk Road.
     * Inicializa y configura la ventana principal (Canvas) y la barra de ganancias.
     * @param road La instancia principal de la simulación SilkRoad (el modelo).
     * @param size La dimensión del tablero (para un tablero de n x n).
     * @param spiral La matriz 2D que define el camino en espiral.
     */
    public SilkRoadView(SilkRoad road, int size, int[][] spiral) {
        this.road = road;
        this.size = size;
        this.spiral = spiral;
        // Calcular las dimensiones de la ventana para que se ajuste al tablero y la barra de ganancias
        int boardWidth = size * (cellSize + margin) + margin;
        int lastRowY = margin + (size - 1) * (cellSize + margin);
        int boardBottom = lastRowY + cellSize;
        int barYPosition = boardBottom + 10; 
        int barHeight = 20;
        int bottomPadding = 20; 
        int windowHeight = barYPosition + barHeight + bottomPadding;
        
        // Crear el lienzo principal para dibujar
        Canvas.createCanvas("Silk Road", boardWidth, windowHeight);
        
        // Inicializar la barra de ganancias (fondo y primer plano)
        profitBarBackground = new Rectangle();
        profitBarCurrent = new Rectangle();
        
        int barWidth = size * (cellSize + margin);
        profitBarBackground.changeSize(barHeight, barWidth);
        profitBarBackground.setPosition(margin, barYPosition);
        profitBarBackground.changeColor("lightgray");

        profitBarCurrent.changeSize(barHeight, 0); // Comienza con ancho 0
        profitBarCurrent.setPosition(margin, barYPosition); 
        profitBarCurrent.changeColor("green");
    }
    
    /**
     * Hace visible la barra de ganancias en el lienzo.
     */
    public void showProfitBar() {
        if (profitBarBackground != null) {
            profitBarBackground.makeVisible();
        }
        if (profitBarCurrent != null) {
            profitBarCurrent.makeVisible();
        }
    }
    
    /**
     * Dibuja la cuadrícula del tablero en el lienzo.
     * Cada celda se representa como un cuadrado blanco con un borde negro.
     */
    public void drawBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                int x = margin + j * (cellSize + margin);
                int y = margin + i * (cellSize + margin);

                // Dibuja el borde negro
                Rectangle border = new Rectangle();
                border.changeSize(cellSize, cellSize);
                border.setPosition(x, y);
                border.changeColor("black");
                border.makeVisible();

                // Dibuja el relleno blanco
                Rectangle fill = new Rectangle();
                fill.changeSize(cellSize - 2, cellSize - 2); 
                fill.setPosition(x + 1, y + 1);
                fill.changeColor("white");
                fill.makeVisible();
            }
        }
    }

    /**
     * Crea una representación gráfica para una tienda y la dibuja en el tablero.
     * @param store El objeto Store que se va a dibujar.
     */
    public void drawStore(Store store) {
        StoreView sv = new StoreView(store, this);
        store.setView(sv);
        sv.draw();
    }

    /**
     * Crea una representación gráfica para un robot y lo dibuja en el tablero.
     * @param robot El objeto Robot que se va a dibujar.
     */
    public void drawRobot(Robot robot) {
        RobotView rv = new RobotView(robot, this);
        robot.setView(rv);
        rv.draw();
    }

    /**
     * Borra la representación gráfica de una tienda del lienzo.
     * @param store La tienda que se va a borrar.
     */
    public void eraseStore(Store store) {
        if(store.getView() != null) store.getView().erase();
    }

    /**
     * Borra la representación gráfica de un robot del lienzo.
     * @param robot El robot que se va a borrar.
     */
    public void eraseRobot(Robot robot) {
        if(robot.getView() != null) robot.getView().erase();
    }

    /**
     * Actualiza la visualización de una tienda, borrándola y volviéndola a dibujar.
     * @param store La tienda a actualizar.
     */
    public void updateStore(Store store) {
        if(store.getView() != null) {
            store.getView().erase();
            store.getView().draw();
        }
    }

    /**
     * Actualiza la visualización de un robot, borrándolo y volviéndolo a dibujar.
     * @param robot El robot a actualizar.
     */
    public void updateRobot(Robot robot) {
        if(robot.getView() != null) {
            robot.getView().erase();
            robot.getView().draw();
        }
    }

    /**
     * Convierte una ubicación 1D de la espiral en coordenadas 2D [fila, columna] del tablero.
     * @param loc La ubicación en la espiral (0-indexada).
     * @return Un array de enteros [fila, columna] o null si no se encuentra la ubicación.
     */
    public int[] coordsForLocation(int loc) {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(spiral[i][j] == loc) return new int[]{i, j};
            }
        }
        return null;
    }

    /**
     * Obtiene el tamaño de cada celda en píxeles.
     * @return El tamaño de la celda.
     */
    public int getCellSize(){ 
        return cellSize; 
    }
    
    /**
     * Obtiene el tamaño del margen entre celdas en píxeles.
     * @return El tamaño del margen.
     */
    public int getMargin(){ 
        return margin; 
    }

    /**
     * Actualiza el ancho de la barra de ganancias para reflejar la ganancia actual.
     * @param currentProfit La ganancia total acumulada por los robots.
     * @param maxProfit La ganancia máxima posible (suma de los tenges iniciales de todas las tiendas).
     */
    public void updateProfitBar(int currentProfit, int maxProfit) {
        if (profitBarCurrent == null) return;
        int barWidth = size * (cellSize + margin);
        int current = Math.max(0, currentProfit);
        int newWidth = maxProfit > 0 ? (int)(((double)current / maxProfit) * barWidth) : 0;
        
        newWidth = Math.min(newWidth, barWidth); // Asegura que la barra no exceda el máximo
        
        profitBarCurrent.changeSize(profitBarCurrent.getHeight(), newWidth);
    }

    /**
     * Obtiene el JFrame principal de la aplicación desde la clase Canvas.
     * Esto es útil para operaciones como cerrar la ventana.
     * @return El JFrame de la aplicación.
     */
    public JFrame getFrame() {
        return Canvas.getCanvas().getFrame();
    }
}