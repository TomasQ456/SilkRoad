package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementación de la tienda más básica del sistema, que sigue una estrategia de
 * comercio simple y directa.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Entrega todos sus tenges al primer robot que la visite</li>
 *   <li>No implementa ninguna estrategia especial de negociación</li>
 *   <li>Usa un sistema de colores rotativo para diferenciarse visualmente</li>
 * </ul>
 * 
 * <p>Esta tienda sirve como punto de referencia para comparar el rendimiento
 * de otras estrategias más complejas de comercio. Su comportamiento predecible
 * la hace ideal para pruebas y desarrollo de nuevas estrategias de robots.
 */
public class NormalStore extends Store {
    /** 
     * Paleta de colores disponibles para las tiendas normales.
     * Se mezcla aleatoriamente al inicio para variar la apariencia del juego.
     */
    private static final List<String> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
        "green", "yellow", "magenta", "red", "blue"
    ));
    
    /** Índice para rotar entre los colores disponibles */
    private static int nextColorIndex = 0;
    
    /** Mezcla los colores al cargar la clase para variar la apariencia del juego */
    static { 
        Collections.shuffle(AVAILABLE_COLORS); 
    }

    /**
     * Crea una nueva tienda normal en la ubicación especificada.
     * 
     * @param location Posición en el tablero (0-based)
     * @param tenges Cantidad inicial de tenges
     */
    public NormalStore(int location, int tenges) {
        super(location, tenges);
    }

    /**
     * Asigna un color único a la tienda, rotando entre los colores disponibles.
     * Este sistema asegura una distribución visual equilibrada de las tiendas
     * en el tablero.
     * 
     * @return Color asignado a esta tienda
     */
    @Override
    protected String assignColor() {
        String color = AVAILABLE_COLORS.get(nextColorIndex);
        nextColorIndex = (nextColorIndex + 1) % AVAILABLE_COLORS.size();
        return color;
    }

    /**
     * Implementa la estrategia de comercio más simple: entrega todos sus tenges
     * al robot visitante.
     * 
     * <p>Este comportamiento básico sirve como línea base para comparar con
     * otras estrategias más sofisticadas. La tienda confía en la estrategia
     * de recolección del robot para determinar cuántos tenges realmente se llevan.
     * 
     * @param robot El robot que visita la tienda
     * @return La cantidad de tenges que el robot decide tomar
     */
    @Override
    public int empty(Robot robot) {
        if (tenges > 0) {
            int amount = tenges;
            collected += tenges;
            tenges = 0;
            timesEmptied++;
            updateView();
            return robot.collectFromStore(amount);
        }
        return 0;
    }

    /**
     * Identifica este tipo de tienda como "Normal".
     * @return "Normal" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Normal";
    }
}