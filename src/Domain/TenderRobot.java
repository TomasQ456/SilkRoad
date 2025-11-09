package Domain;

/**
 * Implementación de un robot que prioriza la sostenibilidad y el comercio justo,
 * tomando solo una parte de los recursos disponibles para permitir que las tiendas
 * mantengan su actividad comercial.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Estrategia de recolección conservadora (solo toma 50%)</li>
 *   <li>Costo de movimiento eficiente (1 tenge por casilla)</li>
 *   <li>Movilidad bidireccional sin restricciones</li>
 *   <li>Color gris claro que simboliza su naturaleza considerada</li>
 * </ul>
 * 
 * <p>Este robot implementa una estrategia de comercio sostenible que,
 * aunque puede parecer menos eficiente a corto plazo, permite mantener
 * un flujo constante de recursos al no agotar completamente las tiendas.
 */
public class TenderRobot extends Robot {
    /** Color distintivo que representa la naturaleza considerada del robot */
    private static final String COLOR = "lightGray";

    /**
     * Crea un nuevo robot considerado en la ubicación especificada.
     * 
     * @param location Posición inicial en el tablero (0-based)
     */
    public TenderRobot(int location) {
        super(location);
    }

    /**
     * Asigna el color gris claro que simboliza la naturaleza
     * suave y considerada de este robot.
     * 
     * @return "lightGray" como color identificativo
     */
    @Override
    protected String assignColor() {
        return COLOR;
    }

    /**
     * Implementa un costo de movimiento eficiente que refleja
     * su naturaleza cuidadosa y planificada.
     * 
     * <p>Su eficiencia en el movimiento compensa parcialmente
     * su estrategia conservadora de recolección.
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
     * Esta flexibilidad le permite regresar a tiendas previamente
     * visitadas que aún conservan recursos gracias a su estrategia
     * conservadora.
     * 
     * @return true, indicando que puede moverse en ambas direcciones
     */
    @Override
    public boolean canMoveBackward() {
        return true;
    }

    /**
     * Implementa la estrategia conservadora de recolección, tomando
     * solo la mitad de los recursos disponibles.
     * 
     * <p>Esta estrategia tiene varios beneficios:
     * <ul>
     *   <li>Permite que las tiendas mantengan actividad comercial</li>
     *   <li>Posibilita visitas posteriores provechosas</li>
     *   <li>Crea oportunidades para otros robots</li>
     * </ul>
     * 
     * @param storeAmount Cantidad que la tienda ofrece
     * @return 50% de la cantidad ofrecida por la tienda
     */
    @Override
    public int collectFromStore(int storeAmount) {
        // Solo toma la MITAD de lo que la tienda ofrece
        // La tienda mantiene la otra mitad
        return storeAmount / 2;
    }

    /**
     * Identifica este tipo de robot como "Tender".
     * @return "Tender" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Tender";
    }
}