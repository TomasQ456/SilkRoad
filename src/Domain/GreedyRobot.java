package Domain;

/**
 * Implementación de un robot especializado en maximizar ganancias a corto plazo,
 * sacrificando eficiencia de movimiento por mayor capacidad de recolección.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Capacidad de extraer más recursos que los disponibles (150%)</li>
 *   <li>Alto costo de movimiento (2 tenges por casilla)</li>
 *   <li>Movilidad bidireccional sin restricciones</li>
 *   <li>Color gris oscuro distintivo que representa su naturaleza ambiciosa</li>
 * </ul>
 * 
 * <p>Este robot implementa una estrategia de alto riesgo/alta recompensa,
 * donde los altos costos de movimiento se compensan con la capacidad de
 * obtener más recursos de cada tienda visitada. Es especialmente efectivo
 * cuando se usa en rutas cortas con tiendas ricas.
 */
public class GreedyRobot extends Robot {
    /** Color distintivo que representa la naturaleza codiciosa del robot */
    private static final String COLOR = "darkGray";

    /**
     * Crea un nuevo robot codicioso en la ubicación especificada.
     * 
     * @param location Posición inicial en el tablero (0-based)
     */
    public GreedyRobot(int location) {
        super(location);
    }

    /**
     * Asigna el color gris oscuro que simboliza la naturaleza
     * ambiciosa y materialista de este robot.
     * 
     * @return "darkGray" como color identificativo
     */
    @Override
    protected String assignColor() {
        return COLOR;
    }

    /**
     * Implementa un costo de movimiento elevado que refleja la
     * ineficiencia del robot debido a su avaricia.
     * 
     * <p>El alto costo representa la "carga" adicional que lleva
     * el robot por su capacidad de extraer más recursos, lo que
     * hace que sus movimientos sean más costosos.
     * 
     * @param distance Número de casillas a moverse
     * @return Costo en tenges (doble de la distancia)
     */
    @Override
    public int getMovementCost(int distance) {
        // ¡Gasta el DOBLE! 2 tenges por casilla
        return distance * 2;
    }

    /**
     * Define la capacidad de movimiento bidireccional del robot.
     * A pesar de su ineficiencia en movimiento, mantiene la
     * flexibilidad de poder moverse en ambas direcciones.
     * 
     * @return true, indicando que puede moverse en ambas direcciones
     */
    @Override
    public boolean canMoveBackward() {
        return true;
    }

    /**
     * Implementa la estrategia codiciosa de recolección, tomando más
     * recursos de los que la tienda originalmente ofrece.
     * 
     * <p>Esta capacidad única representa la habilidad del robot para:
     * <ul>
     *   <li>Extraer recursos ocultos o adicionales</li>
     *   <li>Negociar agresivamente con las tiendas</li>
     *   <li>Obtener un 50% extra sobre lo ofrecido</li>
     * </ul>
     * 
     * @param storeAmount Cantidad que la tienda ofrece
     * @return 150% de la cantidad ofrecida por la tienda
     */
    @Override
    public int collectFromStore(int storeAmount) {
        // ¡Toma el 150%! (1.5 veces lo que la tienda tiene)
        return (storeAmount * 3) / 2;
    }

    /**
     * Identifica este tipo de robot como "Greedy".
     * @return "Greedy" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Greedy";
    }
}