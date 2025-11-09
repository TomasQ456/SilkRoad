package Domain;

/**
 * Implementación de una tienda que simula un sistema de comercio basado en el poder
 * económico, donde solo los robots más ricos pueden acceder a sus recursos.
 * 
 * <p>Características principales:
 * <ul>
 *   <li>Exige que los robots demuestren su riqueza para comerciar</li>
 *   <li>Solo permite transacciones con robots que tienen más tenges que ella</li>
 *   <li>Protege sus recursos de robots con pocos recursos</li>
 *   <li>Usa un color naranja distintivo para indicar su naturaleza competitiva</li>
 * </ul>
 * 
 * <p>Esta tienda implementa una mecánica que fomenta la acumulación estratégica
 * de recursos por parte de los robots, creando una dinámica donde la riqueza
 * genera más oportunidades de riqueza.
 */
public class FighterStore extends Store {
    /** Color distintivo que representa la naturaleza competitiva de la tienda */
    private static final String COLOR = "orange";

    /**
     * Crea una nueva tienda luchadora en la ubicación especificada.
     * 
     * @param location Posición en el tablero (0-based)
     * @param tenges Cantidad inicial de tenges
     */
    public FighterStore(int location, int tenges) {
        super(location, tenges);
    }

    /**
     * Asigna el color naranja que simboliza la naturaleza competitiva
     * y agresiva de esta tienda.
     * 
     * @return "orange" como color identificativo
     */
    @Override
    protected String assignColor() {
        return COLOR;
    }

    /**
     * Implementa la mecánica de comercio basada en poder económico.
     * 
     * <p>La tienda evalúa la riqueza del robot visitante y:
     * <ul>
     *   <li>Si el robot tiene más tenges que la tienda: permite el comercio</li>
     *   <li>Si el robot tiene menos o igual tenges: rechaza la transacción</li>
     * </ul>
     * 
     * <p>Esta mecánica crea una barrera de entrada que fuerza a los robots
     * a planificar cuidadosamente su ruta de comercio, acumulando suficientes
     * recursos antes de intentar negociar con tiendas luchadoras.
     * 
     * @param robot El robot que intenta comerciar
     * @return Cantidad de tenges obtenida (0 si el robot es rechazado)
     */
    @Override
    public int empty(Robot robot) {
        if (tenges > 0) {
            // Solo permite el vaciado si el robot tiene MÁS tenges que la tienda
            if (robot.getTenges() > tenges) {
                int amount = tenges;
                collected += tenges;
                tenges = 0;
                timesEmptied++;
                updateView();
                System.out.println("FighterStore en " + (location + 1) + 
                                 ": Robot con " + robot.getTenges() + 
                                 " tenges venció y tomó " + amount + " tenges.");
                return amount;
            } else {
                System.out.println("FighterStore en " + (location + 1) + 
                                 ": Robot con " + robot.getTenges() + 
                                 " tenges no pudo vencer (necesita > " + tenges + ").");
                return 0;
            }
        }
        return 0;
    }

    /**
     * Identifica este tipo de tienda como "Fighter".
     * @return "Fighter" como identificador del tipo
     */
    @Override
    public String getType() {
        return "Fighter";
    }
}