import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Gestiona la representación gráfica de un objeto Robot.
 * Esta clase se encarga de dibujar el robot como un círculo en el tablero y de
 * manejar animaciones, como el parpadeo.
 */
public class RobotView {
    private Robot robot;
    private SilkRoadView roadView;
    private Circle circle;
    private Thread blinkingThread;

    /**
     * Constructor para la vista de un Robot.
     * @param robot La instancia del modelo Robot que esta vista representará.
     * @param roadView La vista principal del tablero (SilkRoadView) para obtener
     * información sobre coordenadas y tamaño.
     */
    public RobotView(Robot robot, SilkRoadView roadView) {
        this.robot = robot;
        this.roadView = roadView;
        this.circle = new Circle();
    }

    /**
     * Dibuja el robot en su ubicación actual en el lienzo.
     * Calcula la posición y el tamaño del círculo basándose en la celda del tablero
     * y lo hace visible con el color correspondiente del robot.
     */
    public void draw() {
        int[] pos = roadView.coordsForLocation(robot.getLocation());
        int cellSize = roadView.getCellSize();
        int margin = roadView.getMargin();
        int baseX = margin + pos[1] * (cellSize + margin);
        int baseY = margin + pos[0] * (cellSize + margin);
        int circleSize = cellSize - 10;
        circle.changeSize(circleSize);
        int offset = (cellSize - circleSize) / 2;
        circle.setPosition(baseX + offset, baseY + offset);
        circle.changeColor(robot.getColor());
        circle.makeVisible();
    }

    /**
     * Borra el robot del lienzo haciéndolo invisible.
     */
    public void erase() {
        if (circle != null) {
            circle.makeInvisible();
        }
    }

    /**
     * Inicia una animación de parpadeo para el círculo del robot.
     * Crea y ejecuta un nuevo hilo que alterna la visibilidad del círculo
     * durante un período de tiempo determinado (10 segundos). Si ya existe una
     * animación de parpadeo en curso, se interrumpe antes de iniciar la nueva.
     */
    public void startBlinking() {
        // Interrumpe cualquier hilo de parpadeo anterior para evitar animaciones conflictivas.
        if (blinkingThread != null && blinkingThread.isAlive()) {
            blinkingThread.interrupt();
        }

        blinkingThread = new Thread(() -> {
            try {
                long blinkDuration = 10000; // Duración total del parpadeo en milisegundos
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < blinkDuration) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    circle.makeInvisible();
                    Thread.sleep(250); // Tiempo invisible

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    circle.makeVisible();
                    Thread.sleep(250); // Tiempo visible
                }
            } catch (InterruptedException e) {
            } finally {
                // Asegura que el círculo sea visible al finalizar la animación o al ser interrumpida.
                circle.makeVisible();
            }
        });
        blinkingThread.start();
    }

    /**
     * Detiene la animación de parpadeo del robot de forma anticipada.
     * Interrumpe el hilo de animación, lo que hace que el bloque 'finally'
     * asegure que el círculo quede en un estado visible.
     */
    public void stopBlinking() {
        if (blinkingThread != null && blinkingThread.isAlive()) {
            blinkingThread.interrupt(); 
        }
    }
}