package Domain;
import Presentation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la base de la inteligencia artificial para los robots que operan en la Ruta de la Seda.
 * Esta clase abstracta define el comportamiento base y las características que todos los robots deben tener
 * para poder participar en el sistema comercial.
 * 
 * <p>Los robots son entidades autónomas que:
 * <ul>
 *   <li>Se mueven por el tablero buscando oportunidades de comercio</li>
 *   <li>Interactúan con las tiendas para obtener tenges</li>
 *   <li>Mantienen un registro de sus ganancias y pérdidas</li>
 *   <li>Tienen diferentes estrategias de movimiento y recolección</li>
 * </ul>
 * 
 * <p>Esta implementación permite una fácil extensión para crear nuevos tipos de robots
 * con diferentes estrategias de comercio y movimiento.
 */
public abstract class Robot {
    /** Posición actual en el tablero (0-based) */
    protected int location;
    /** Posición inicial, usada para regresar y reiniciar */
    protected int initialLocation;
    /** Moneda del juego, representa la riqueza actual del robot */
    protected int tenges;
    /** Referencia a la vista para la representación gráfica */
    protected RobotView view;
    /** Color identificativo del tipo de robot */
    protected String color;
    /** Indica si el robot está parpadeando (usado para mostrar el robot más rico) */
    protected boolean isBlinking = false;
    /** Historial de ganancias/pérdidas por movimiento */
    protected ArrayList<Integer> profitHistory = new ArrayList<>();

    /**
     * Inicializa un nuevo robot en una posición específica.
     * Los robots siempre comienzan sin tenges y recordarán su posición inicial
     * para poder regresar cuando sea necesario.
     * 
     * @param location Posición inicial en el tablero (0-based)
     */
    public Robot(int location) {
        this.location = location;
        this.initialLocation = location;
        this.tenges = 0;
        this.color = assignColor();
    }

    /**
     * Define el color representativo del robot.
     * Cada tipo de robot debe definir su propio color para diferenciarse visualmente.
     * 
     * @return Color en formato String que representa el tipo de robot
     */
    protected abstract String assignColor();

    /**
     * Calcula el costo energético de moverse una distancia.
     * Cada tipo de robot implementa su propia estrategia de costos,
     * permitiendo simular diferentes eficiencias de movimiento.
     * 
     * @param distance Número de casillas a moverse
     * @return Costo en tenges del movimiento
     */
    public abstract int getMovementCost(int distance);

    /**
     * Determina si el robot puede moverse hacia atrás.
     * Esta característica es clave para definir las estrategias de movimiento
     * y optimización de rutas de cada tipo de robot.
     * 
     * @return true si el robot puede moverse hacia atrás, false en caso contrario
     */
    public abstract boolean canMoveBackward();

    /**
     * Define la estrategia de recolección de tenges de una tienda.
     * Cada tipo de robot puede implementar diferentes estrategias,
     * como ser codicioso y tomar todo, o ser conservador y tomar solo una parte.
     * 
     * @param storeAmount Cantidad disponible en la tienda
     * @return Cantidad que el robot decide tomar
     */
    public abstract int collectFromStore(int storeAmount);

    /**
     * Identifica el tipo específico de robot.
     * Útil para logging y debugging.
     * 
     * @return Nombre del tipo de robot
     */
    public abstract String getType();

    /**
     * Reinicia el robot a su estado inicial.
     * Se usa cuando se necesita comenzar una nueva ronda o
     * cuando se quiere limpiar el historial de transacciones.
     */
    public void reboot() {
        location = initialLocation;
        tenges = 0;
        profitHistory.clear();
    }
    
    /**
     * Registra una transacción en el historial del robot.
     * Este registro es crucial para analizar el rendimiento y
     * la efectividad de las estrategias del robot.
     * 
     * @param profitAmount Ganancia (positiva) o pérdida (negativa) de la transacción
     */
    public void recordProfit(int profitAmount) {
        profitHistory.add(profitAmount);
    }
    
    /**
     * Proporciona el historial completo de transacciones.
     * Útil para análisis de rendimiento y visualización de estrategias.
     * 
     * @return Lista inmutable del historial de ganancias/pérdidas
     */
    public List<Integer> getProfitHistory() {
        return profitHistory;
    }

    public void setView(RobotView v) {
        this.view = v;
        if (isBlinking) {
            view.startBlinking();
        }
    }

    public void setBlinking(boolean shouldBlink) {
        if (this.isBlinking == shouldBlink) { return; }
        this.isBlinking = shouldBlink;
        if (view != null) {
            if (shouldBlink) {
                view.startBlinking();
            } else {
                view.stopBlinking();
            }
        }
    }
    
    // --- Getters y Setters ---
    public int getLocation() { 
        return location; 
    }
    
    public void setLocation(int loc) { 
        location = loc; 
    }
    
    public int getTenges() { 
        return tenges; 
    }
    
    public void addTenges(int v) { 
        tenges += v; 
    }
    
    public void setTenges(int v) { 
        tenges = v; 
    }
    
    public void returnToInitial() { 
        location = initialLocation; 
    }
    
    public RobotView getView() { 
        return view; 
    }
    
    public String getColor() { 
        return color; 
    }
}