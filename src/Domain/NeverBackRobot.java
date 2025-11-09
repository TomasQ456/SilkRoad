package Domain;

/**
 * Implementación de un robot con una restricción de movimiento única que lo
 * fuerza a seguir siempre hacia adelante, simulando un comportamiento determinado
 * y persistente.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Movimiento unidireccional (solo hacia adelante)</li>
 *   <li>Costo de movimiento eficiente (1 tenge por casilla)</li>
 *   <li>Estrategia de recolección simple (toma todo lo disponible)</li>
 *   <li>Color púrpura distintivo que simboliza su determinación</li>
 * </ul>
 * 
 * <p>Este robot está diseñado para simular estrategias de "avance continuo",
 * donde la imposibilidad de retroceder fuerza una planificación más cuidadosa
 * de las rutas y un aprovechamiento máximo de cada movimiento hacia adelante.
 */
public class NeverBackRobot extends Robot {
    /** Color distintivo que representa la determinación del robot */
    private static final String COLOR = "purple";

    /**
     * Crea un nuevo robot que nunca retrocede en la ubicación especificada.
     * 
     * @param location Posición inicial en el tablero (0-based)
     */
    public NeverBackRobot(int location) {
        super(location);
    }

    /**
     * Asigna el color púrpura que simboliza la determinación y
     * el avance constante de este robot.
     * 
     * @return "purple" como color identificativo
     */
    @Override
    protected String assignColor() {
        return COLOR;
    }

    /**
     * Implementa un costo de movimiento eficiente que compensa
     * la restricción de movimiento unidireccional.
     * 
     * <p>A pesar de su limitación de movimiento, mantiene una
     * eficiencia energética alta, gastando solo un tenge por
     * casilla recorrida.
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
     * Define la restricción fundamental de este robot:
     * la imposibilidad de retroceder.
     * 
     * <p>Esta limitación fuerza al robot a:
     * <ul>
     *   <li>Planificar cuidadosamente sus movimientos</li>
     *   <li>Aprovechar al máximo cada avance</li>
     *   <li>Considerar el tablero como un ciclo completo</li>
     * </ul>
     * 
     * @return false, indicando que no puede moverse hacia atrás
     */
    @Override
    public boolean canMoveBackward() {
        // ¡Nunca retrocede!
        return false;
    }

    /**
     * Implementa una estrategia de recolección simple que maximiza
     * la ganancia en cada oportunidad, crucial dado que no puede
     * volver a visitar tiendas moviéndose hacia atrás.
     * 
     * @param storeAmount Cantidad que la tienda ofrece
     * @return La cantidad completa ofrecida por la tienda
     */
    @Override
    public int collectFromStore(int storeAmount) {
        // Toma todo lo que la tienda ofrece
        return storeAmount;
    }

    /**
     * Identifica este tipo de robot como "NeverBack".
     * @return "NeverBack" como identificador del tipo
     */
    @Override
    public String getType() {
        return "NeverBack";
    }
}