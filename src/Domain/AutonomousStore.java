package Domain;

import java.util.Random;

/**
 * Implementación de una tienda con capacidad de decisión sobre su ubicación,
 * añadiendo un elemento de incertidumbre a la planificación de rutas de los robots.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Decide autónomamente su ubicación en el tablero</li>
 *   <li>Intenta primero usar la ubicación sugerida</li>
 *   <li>Si la ubicación sugerida no está disponible, busca una aleatoria</li>
 *   <li>Usa un color cian distintivo para indicar su naturaleza impredecible</li>
 * </ul>
 * 
 * <p>Esta tienda añade un elemento de adaptabilidad al juego, forzando a los robots
 * a ser flexibles en sus estrategias de ruta, ya que no pueden confiar en encontrar
 * la tienda en la ubicación originalmente planeada.
 */
public class AutonomousStore extends Store {
    /** Color distintivo que representa la naturaleza independiente de la tienda */
    private static final String COLOR = "cyan";
    /** Generador de números aleatorios para la selección de ubicación */
    private static Random random = new Random();

    /**
     * Crea una nueva tienda autónoma que selecciona su propia ubicación.
     * 
     * @param suggestedLocation Ubicación sugerida (podría ser ignorada)
     * @param tenges Cantidad inicial de tenges
     * @param boardSize Tamaño total del tablero
     * @param isAvailable Predicado que determina si una ubicación está disponible
     */
    public AutonomousStore(int suggestedLocation, int tenges, int boardSize, java.util.function.Predicate<Integer> isAvailable) {
        super(findAvailableLocation(suggestedLocation, boardSize, isAvailable), tenges);
    }

    /**
     * Implementa la lógica de búsqueda de ubicación para la tienda.
     * 
     * <p>El proceso de búsqueda sigue el siguiente algoritmo:
     * <ol>
     *   <li>Intenta usar la ubicación sugerida si está disponible</li>
     *   <li>Si no está disponible, busca aleatoriamente una nueva ubicación</li>
     *   <li>Limita la búsqueda a un número razonable de intentos</li>
     *   <li>Si no encuentra ubicación, usa la sugerida (causará error en SilkRoad)</li>
     * </ol>
     * 
     * @param suggested Ubicación inicialmente sugerida
     * @param boardSize Tamaño total del tablero
     * @param isAvailable Función que verifica si una ubicación está libre
     * @return Ubicación seleccionada para la tienda
     */
    private static int findAvailableLocation(int suggested, int boardSize, java.util.function.Predicate<Integer> isAvailable) {
        // Intentar con la ubicación sugerida primero
        if (isAvailable.test(suggested)) {
            return suggested;
        }
        
        // Buscar una ubicación aleatoria disponible
        int attempts = 0;
        int maxAttempts = boardSize * 2;
        
        while (attempts < maxAttempts) {
            int randomLoc = random.nextInt(boardSize);
            if (isAvailable.test(randomLoc)) {
                System.out.println("AutonomousStore: Ubicación " + (suggested + 1) + 
                                 " no disponible. Eligiendo ubicación " + (randomLoc + 1));
                return randomLoc;
            }
            attempts++;
        }
        
        // Si no encuentra ninguna, usar la sugerida (causará error en SilkRoad)
        return suggested;
    }

    /**
     * Asigna el color cian que simboliza la naturaleza independiente
     * y adaptable de esta tienda.
     * 
     * @return "cyan" como color identificativo
     */
    @Override
    protected String assignColor() {
        return COLOR;
    }

    /**
     * Implementa la mecánica estándar de comercio.
     * A pesar de su ubicación autónoma, esta tienda mantiene
     * un comportamiento de comercio similar al de una tienda normal.
     * 
     * @param robot El robot que intenta comerciar
     * @return Cantidad de tenges que el robot obtiene
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
     * Identifica este tipo de tienda como "Autonomous".
     * @return "Autonomous" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Autonomous";
    }
}