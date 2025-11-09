package Domain;

import java.util.Random;

/**
 * Implementación de una tienda que introduce elementos de azar en el juego,
 * simulando un casino donde los robots pueden doblar sus ganancias o perderlo todo.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Ofrece una mecánica de alto riesgo/alta recompensa</li>
 *   <li>Tiene 50% de probabilidad de duplicar la ganancia potencial</li>
 *   <li>Tiene 50% de probabilidad de hacer que el robot pierda todos sus tenges</li>
 *   <li>Usa un color distintivo (rosa) para advertir de su naturaleza arriesgada</li>
 * </ul>
 * 
 * <p>Esta tienda añade un elemento estratégico adicional al juego, ya que los robots
 * deben decidir si vale la pena arriesgar sus ganancias acumuladas por la posibilidad
 * de obtener una gran recompensa.
 */
public class CasinoStore extends Store {
    /** Color distintivo para las tiendas casino */
    private static final String COLOR = "pink";
    /** Generador de números aleatorios para las probabilidades */
    private static Random random = new Random();

    /**
     * Crea una nueva tienda casino en la ubicación especificada.
     * 
     * @param location Posición en el tablero (0-based)
     * @param tenges Cantidad inicial de tenges
     */
    public CasinoStore(int location, int tenges) {
        super(location, tenges);
    }

    /**
     * Asigna el color rosa distintivo que advierte de la naturaleza
     * arriesgada de esta tienda.
     * 
     * @return "pink" como color identificativo
     */
    @Override
    protected String assignColor() {
        return COLOR;
    }

    /**
     * Implementa la mecánica de juego de azar del casino.
     * 
     * <p>Cuando un robot visita la tienda:
     * <ul>
     *   <li>Si gana (50% probabilidad): recibe el doble de los tenges disponibles</li>
     *   <li>Si pierde (50% probabilidad): pierde todos sus tenges actuales</li>
     * </ul>
     * 
     * <p>Esta mecánica crea situaciones interesantes donde los robots deben
     * evaluar si el riesgo de perderlo todo vale la recompensa potencial.
     * 
     * @param robot El robot que se arriesga en el casino
     * @return Cantidad ganada (positiva) o perdida (negativa)
     */
    @Override
    public int empty(Robot robot) {
        if (tenges > 0) {
            int storeAmount = tenges;
            
            // 50% de probabilidad de ganar o perder
            boolean wins = random.nextBoolean();
            
            if (wins) {
                // ¡GANA! Duplica las ganancias
                int winAmount = storeAmount * 2;
                collected += storeAmount;
                tenges = 0;
                timesEmptied++;
                updateView();
                return winAmount;
            } else {
                // ¡PIERDE! El robot pierde todo lo que traía
                int lostAmount = robot.getTenges();
                collected += storeAmount;
                tenges = 0;
                timesEmptied++;
                updateView();
                return -lostAmount;
            }
        }
        return 0;
    }

    /**
     * Identifica este tipo de tienda como "Casino".
     * @return "Casino" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Casino";
    }
}