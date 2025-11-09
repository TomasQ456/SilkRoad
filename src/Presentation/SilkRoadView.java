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

/**
 * Componente visual principal que gestiona la representación gráfica completa
 * del juego Silk Road, incluyendo el tablero, tiendas, robots y barra de progreso.
 * 
 * <p>Esta clase actúa como el coordinador central de la interfaz gráfica,
 * manejando:
 * <ul>
 *   <li>La disposición y visualización del tablero en espiral</li>
 *   <li>La creación y posicionamiento de tiendas y robots</li>
 *   <li>La barra de progreso que muestra las ganancias totales</li>
 *   <li>La coordinación entre el modelo de dominio y sus representaciones visuales</li>
 * </ul>
 */
public class SilkRoadView {
    /** Referencia al modelo de dominio principal */
    private SilkRoad road;
    /** Tamaño del tablero (número de celdas por lado) */
    private int size;
    /** Matriz que define la secuencia en espiral */
    private int[][] spiral;
    /** Tamaño de cada celda en píxeles */
    private int cellSize = 40;
    /** Margen entre celdas en píxeles */
    private int margin = 5;

    /** Fondo de la barra de progreso */
    private Rectangle profitBarBackground;
    /** Indicador de progreso actual */
    private Rectangle profitBarCurrent;

    /**
     * Crea una nueva vista principal del juego.
     * 
     * <p>Inicializa todos los componentes visuales necesarios:
     * <ul>
     *   <li>Configura el canvas con las dimensiones apropiadas</li>
     *   <li>Crea la barra de progreso con fondo e indicador</li>
     *   <li>Establece las posiciones relativas de todos los elementos</li>
     * </ul>
     * 
     * @param road Modelo de dominio del juego
     * @param size Tamaño del tablero
     * @param spiral Matriz que define el patrón en espiral
     */
    public SilkRoadView(SilkRoad road, int size, int[][] spiral) {
        this.road = road;
        this.size = size;
        this.spiral = spiral;
        
        // Cálculo de dimensiones y posicionamiento
        int boardWidth = size * (cellSize + margin) + margin;
        int lastRowY = margin + (size - 1) * (cellSize + margin);
        int boardBottom = lastRowY + cellSize;
        int barYPosition = boardBottom + 10;
        int barHeight = 20;
        int bottomPadding = 20;
        int windowHeight = barYPosition + barHeight + bottomPadding;

        // Inicialización del canvas principal
        Canvas.createCanvas("Silk Road", boardWidth, windowHeight);

        // Configuración de la barra de progreso
        profitBarBackground = new Rectangle();
        profitBarCurrent = new Rectangle();
        
        int barWidth = size * (cellSize + margin);
        profitBarBackground.changeSize(barHeight, barWidth);
        profitBarBackground.setPosition(margin, barYPosition);
        profitBarBackground.changeColor("lightgray");

        profitBarCurrent.changeSize(barHeight, 0);
        profitBarCurrent.setPosition(margin, barYPosition);
        profitBarCurrent.changeColor("green");
    }
    
    /**
     * Hace visible la barra de progreso que muestra las ganancias totales.
     * Esta barra proporciona retroalimentación visual sobre el rendimiento
     * de los robots en el juego.
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
     * Dibuja el tablero completo con todas sus celdas.
     * Crea una cuadrícula visual donde cada celda tiene un borde negro
     * y un relleno blanco, formando el espacio de juego.
     */
    public void drawBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                int x = margin + j * (cellSize + margin);
                int y = margin + i * (cellSize + margin);

                // Borde de la celda
                Rectangle border = new Rectangle();
                border.changeSize(cellSize, cellSize);
                border.setPosition(x, y);
                border.changeColor("black");
                border.makeVisible();

                // Relleno de la celda
                Rectangle fill = new Rectangle();
                fill.changeSize(cellSize - 2, cellSize - 2); 
                fill.setPosition(x + 1, y + 1);
                fill.changeColor("white");
                fill.makeVisible();
            }
        }
    }

    /**
     * Crea y dibuja la representación visual de una tienda.
     * 
     * @param store Tienda del dominio a representar
     */
    public void drawStore(Store store) {
        StoreView sv = new StoreView(store, this);
        store.setView(sv);
        sv.draw();
    }

    /**
     * Crea y dibuja la representación visual de un robot.
     * 
     * @param robot Robot del dominio a representar
     */
    public void drawRobot(Robot robot) {
        RobotView rv = new RobotView(robot, this);
        robot.setView(rv);
        rv.draw();
    }

    /**
     * Actualiza la visualización de un robot específico.
     * 
     * @param robot Robot cuya representación debe actualizarse
     */
    public void updateRobot(Robot robot) {
        if (robot.getView() != null) {
            robot.getView().draw();
        }
    }

    /**
     * Elimina la representación visual de una tienda.
     * 
     * @param store Tienda cuya representación debe eliminarse
     */
    public void eraseStore(Store store) {
        if (store.getView() != null) {
            store.getView().erase();
        }
    }

    /**
     * Elimina la representación visual de un robot.
     * 
     * @param robot Robot cuya representación debe eliminarse
     */
    public void eraseRobot(Robot robot) {
        if (robot.getView() != null) {
            robot.getView().erase();
        }
    }

    /**
     * Actualiza la barra de progreso según las ganancias actuales.
     * 
     * @param current Ganancias actuales
     * @param maximum Ganancias máximas posibles
     */
    public void updateProfitBar(int current, int maximum) {
        if (maximum <= 0) return;
        
        int barWidth = size * (cellSize + margin);
        int newWidth = (int)((double)current * barWidth / maximum);
        newWidth = Math.max(0, Math.min(newWidth, barWidth));
        
        profitBarCurrent.makeInvisible();
        profitBarCurrent.changeSize(20, newWidth);
        profitBarCurrent.makeVisible();
    }

    /**
     * Convierte una posición lógica del juego en coordenadas de matriz.
     * 
     * @param location Posición lógica (0-based)
     * @return Array con las coordenadas [fila, columna]
     */
    public int[] coordsForLocation(int location) {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(spiral[i][j] == location) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }

    /**
     * Obtiene el tamaño de celda actual.
     * @return Tamaño de celda en píxeles
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Obtiene el margen actual entre celdas.
     * @return Margen en píxeles
     */
    public int getMargin() {
        return margin;
    }

    /**
     * Obtiene el frame principal del juego.
     * @return Frame de la ventana principal
     */
    public JFrame getFrame() {
        // Canvas exposes getFrame(); previous call used a non-existent
        // getWindowFrame() which caused a NoSuchMethodError at runtime.
        return Canvas.getCanvas().getFrame();
    }
}