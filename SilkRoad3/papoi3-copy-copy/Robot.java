import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un robot en la simulación de Silk Road.
 * Cada robot tiene una ubicación, una cantidad de "tenges" (moneda) acumulados,
 * y un historial de sus ganancias por movimiento.
 */
public class Robot {
    private int location;
    private int initialLocation;
    private int tenges;
    private RobotView view;
    private String color;
    private boolean isBlinking = false;
    private ArrayList<Integer> profitHistory = new ArrayList<>();

    // Lista estática de colores disponibles para asignar a los robots.
    private static final List<String> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
        "red", "blue", "green", "yellow", "magenta", "black"
    ));
    // Índice para asignar el siguiente color de la lista.
    private static int nextColorIndex = 0;
    
    // Bloque estático para barajar la lista de colores al cargar la clase,
    // asegurando que los colores se asignen en un orden aleatorio en cada ejecución.
    static { 
        Collections.shuffle(AVAILABLE_COLORS); 
    }
    
    /**
     * Constructor para crear un nuevo robot en una ubicación específica.
     * @param location La posición inicial del robot en el tablero (basada en índice 0).
     */
    public Robot(int location) {
        this.location = location;
        this.initialLocation = location;
        this.tenges = 0;
        this.color = getNextUniqueColor();
    }

    /**
     * Asigna un color único de la lista de colores disponibles de forma cíclica.
     * @return Un String que representa el color asignado.
     */
    private static String getNextUniqueColor() {
        if (AVAILABLE_COLORS.isEmpty()) { return "black"; }
        String color = AVAILABLE_COLORS.get(nextColorIndex);
        nextColorIndex = (nextColorIndex + 1) % AVAILABLE_COLORS.size();
        return color;
    }
    
    /**
     * Reinicia el estado del robot a su configuración inicial.
     * La ubicación vuelve a ser la inicial, los tenges se resetean a 0
     * y el historial de ganancias se limpia.
     */
    public void reboot() {
        location = initialLocation;
        tenges = 0;
        profitHistory.clear(); 
    }
    
    /**
     * Registra la ganancia (o pérdida) de un movimiento en el historial del robot.
     * @param profitAmount La cantidad de tenges ganados o perdidos en el movimiento.
     */
    public void recordProfit(int profitAmount) {
        profitHistory.add(profitAmount);
    }
    
    /**
     * Obtiene el historial de ganancias por movimiento del robot.
     * @return Una lista de enteros con las ganancias de cada movimiento.
     */
    public List<Integer> getProfitHistory() {
        return profitHistory;
    }

    /**
     * Asigna una vista (representación gráfica) a este robot.
     * @param v La instancia de RobotView asociada a este robot.
     */
    public void setView(RobotView v) {
        this.view = v;
        if (isBlinking) {
            view.startBlinking();
        }
    }

    /**
     * Activa o desactiva el efecto de parpadeo para la vista del robot.
     * @param shouldBlink true para empezar a parpadear, false para detenerlo.
     */
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
    
    /**
     * Obtiene la ubicación actual del robot.
     * @return La posición actual como un entero.
     */
    public int getLocation() { 
        return location; 
    }
    
    /**
     * Establece la ubicación actual del robot.
     * @param loc La nueva posición del robot.
     */
    public void setLocation(int loc) { 
        location = loc; 
    }
    
    /**
     * Obtiene la cantidad actual de tenges que ha acumulado el robot.
     * @return La cantidad de tenges.
     */
    public int getTenges() { 
        return tenges; 
    }
    
    /**
     * Añade una cantidad de tenges al total del robot.
     * @param v La cantidad de tenges a añadir (puede ser negativa).
     */
    public void addTenges(int v) { 
        tenges += v; 
    }
    
    /**
     * Establece la cantidad total de tenges del robot a un valor específico.
     * @param v El nuevo valor total de tenges.
     */
    public void setTenges(int v) { 
        tenges = v; 
    }
    
    /**
     * Devuelve el robot a su ubicación de inicio.
     */
    public void returnToInitial() { 
        location = initialLocation; 
    }
    
    /**
     * Obtiene la vista gráfica asociada a este robot.
     * @return La instancia de RobotView.
     */
    public RobotView getView() { 
        return view; 
    }
    
    /**
     * Obtiene el color del robot.
     * @return Un String con el nombre del color.
     */
    public String getColor() { 
        return color; 
    }
}