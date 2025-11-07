package Domain;
import Presentation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa un robot en la Ruta de la Seda.
 * Define el comportamiento común de todos los robots.
 */
public abstract class Robot {
    protected int location;
    protected int initialLocation;
    protected int tenges;
    protected RobotView view;
    protected String color;
    protected boolean isBlinking = false;
    protected ArrayList<Integer> profitHistory = new ArrayList<>();

    /**
     * Constructor base para todos los robots.
     */
    public Robot(int location) {
        this.location = location;
        this.initialLocation = location;
        this.tenges = 0;
        this.color = assignColor();
    }

    /**
     * Método abstracto para asignar color según el tipo de robot.
     */
    protected abstract String assignColor();

    /**
     * Calcula el costo de movimiento para este robot.
     * Diferentes tipos de robots pueden tener diferentes costos.
     */
    public abstract int getMovementCost(int distance);

    /**
     * Determina si el robot puede moverse en dirección negativa.
     * @return true si puede retroceder, false si no
     */
    public abstract boolean canMoveBackward();

    /**
     * Procesa la recolección de tenges de una tienda.
     * @param storeAmount La cantidad que la tienda ofrece
     * @return La cantidad real que el robot toma
     */
    public abstract int collectFromStore(int storeAmount);

    /**
     * Retorna el tipo de robot como String.
     */
    public abstract String getType();

    /**
     * Reinicia el robot a su estado inicial.
     */
    public void reboot() {
        location = initialLocation;
        tenges = 0;
        profitHistory.clear();
    }
    
    public void recordProfit(int profitAmount) {
        profitHistory.add(profitAmount);
    }
    
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