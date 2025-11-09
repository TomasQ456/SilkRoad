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

/**
 * Componente visual que maneja la representación gráfica de los robots en el tablero.
 * Esta clase actúa como un puente entre el modelo de dominio (Robot) y su
 * visualización en la interfaz de usuario.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Representa cada robot como un círculo con color distintivo</li>
 *   <li>Implementa efectos visuales como parpadeo para robots destacados</li>
 *   <li>Maneja la actualización dinámica de la posición del robot</li>
 *   <li>Coordina la visualización con la cuadrícula del tablero</li>
 * </ul>
 */
public class RobotView {
    /** Robot del dominio que esta vista representa */
    private Robot robot;
    /** Vista principal del tablero que contiene este robot */
    private SilkRoadView roadView;
    /** Forma geométrica que representa visualmente al robot */
    private Circle circle;
    /** Temporizador para el efecto de parpadeo */
    private Timer blinkingTimer; 

    /**
     * Crea una nueva vista para un robot específico.
     * 
     * <p>Inicializa la representación visual del robot y configura
     * el temporizador para el efecto de parpadeo que se usa para
     * destacar robots especiales (por ejemplo, el más rico).
     * 
     * @param robot Robot del dominio a representar
     * @param roadView Vista principal del tablero
     */
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

    /**
     * Obtiene el robot del dominio asociado a esta vista.
     * @return Robot representado por esta vista
     */
    public Robot getRobot() {
        return robot;
    }

    /**
     * Dibuja la representación visual del robot en el tablero.
     * 
     * <p>El proceso de dibujado:
     * <ol>
     *   <li>Calcula la posición exacta en píxeles basada en la ubicación lógica</li>
     *   <li>Ajusta el tamaño del círculo según las dimensiones de la celda</li>
     *   <li>Aplica el color correspondiente al tipo de robot</li>
     *   <li>Hace visible la representación</li>
     * </ol>
     */
    public void draw() {
        int[] pos = roadView.coordsForLocation(robot.getLocation());
        int cellSize = roadView.getCellSize();
        int margin = roadView.getMargin();
        int baseX = margin + pos[1] * (cellSize + margin);
        int baseY = margin + pos[0] * (cellSize + margin);
        
        circle.changeSize(cellSize - 10);
        circle.setPosition(baseX + cellSize / 2, baseY + cellSize / 2);
        circle.changeColor(robot.getColor());
        circle.makeVisible();
    }
    
    /**
     * Oculta la representación visual del robot.
     * Detiene cualquier efecto de parpadeo activo antes de
     * hacer invisible al robot.
     */
    public void erase() {
        stopBlinking();
        circle.makeInvisible();
    }

    /**
     * Inicia el efecto de parpadeo para destacar al robot.
     * Utilizado típicamente para resaltar el robot más rico
     * o en situaciones especiales que requieren atención.
     */
    public void startBlinking() {
        circle.makeVisible();
        blinkingTimer.start();
    }

    /**
     * Detiene el efecto de parpadeo y restaura la visibilidad
     * normal del robot.
     */
    public void stopBlinking() {
        blinkingTimer.stop();
        circle.makeVisible();
    }
}