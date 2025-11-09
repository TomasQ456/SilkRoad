package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementación del robot básico que sirve como punto de referencia para
 * comparar el rendimiento de otras estrategias más especializadas.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Movilidad bidireccional sin restricciones</li>
 *   <li>Costo de movimiento simple y predecible (1 tenge por casilla)</li>
 *   <li>Estrategia de recolección simple (toma todo lo disponible)</li>
 *   <li>Sistema de colores rotativo para diferenciación visual</li>
 * </ul>
 * 
 * <p>Este robot implementa la estrategia más básica y directa posible,
 * sirviendo como línea base para el desarrollo y evaluación de robots
 * más sofisticados.
 */
public class NormalRobot extends Robot {
    /** 
     * Paleta de colores disponibles para los robots normales.
     * Se mezcla aleatoriamente al inicio para variar la apariencia del juego.
     */
    private static final List<String> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
        "red", "blue", "green", "yellow", "magenta", "black"
    ));
    
    /** Índice para rotar entre los colores disponibles */
    private static int nextColorIndex = 0;
    
    /** Mezcla los colores al cargar la clase para variar la apariencia del juego */
    static { 
        Collections.shuffle(AVAILABLE_COLORS); 
    }

    /**
     * Crea un nuevo robot normal en la ubicación especificada.
     * 
     * @param location Posición inicial en el tablero (0-based)
     */
    public NormalRobot(int location) {
        super(location);
    }

    /**
     * Asigna un color único al robot, rotando entre los colores disponibles.
     * Este sistema asegura una distribución visual equilibrada de los robots
     * en el tablero.
     * 
     * @return Color asignado a este robot
     */
    @Override
    protected String assignColor() {
        if (AVAILABLE_COLORS.isEmpty()) { return "black"; }
        String color = AVAILABLE_COLORS.get(nextColorIndex);
        nextColorIndex = (nextColorIndex + 1) % AVAILABLE_COLORS.size();
        return color;
    }

    /**
     * Implementa el costo de movimiento más simple posible.
     * Cada casilla de movimiento cuesta exactamente un tenge,
     * lo que hace que los costos sean predecibles y fáciles de calcular.
     * 
     * @param distance Número de casillas a moverse
     * @return Costo exacto en tenges (igual a la distancia)
     */
    @Override
    public int getMovementCost(int distance) {
        // Costo estándar: 1 tenge por casilla
        return distance;
    }

    /**
     * Define la capacidad de movimiento bidireccional del robot.
     * Los robots normales pueden moverse tanto hacia adelante como hacia atrás,
     * proporcionando máxima flexibilidad en la planificación de rutas.
     * 
     * @return true, indicando que puede moverse en ambas direcciones
     */
    @Override
    public boolean canMoveBackward() {
        return true;
    }

    /**
     * Implementa la estrategia más simple de recolección: tomar todo.
     * Esta estrategia maximiza la ganancia inmediata sin considerar
     * factores estratégicos adicionales.
     * 
     * @param storeAmount Cantidad que la tienda ofrece
     * @return La misma cantidad ofrecida por la tienda
     */
    @Override
    public int collectFromStore(int storeAmount) {
        // Toma todo lo que la tienda ofrece
        return storeAmount;
    }

    /**
     * Identifica este tipo de robot como "Normal".
     * @return "Normal" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Normal";
    }
}