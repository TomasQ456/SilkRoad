package Domain;
import Presentation.*;

/**
 * Clase abstracta que representa una tienda en la Ruta de la Seda.
 * Define el comportamiento común de todas las tiendas.
 */
public abstract class Store {
    protected int location;
    protected int tenges;
    protected int initialTenges;
    protected StoreView view;
    protected String color;
    protected int collected = 0;
    protected int timesEmptied = 0;

    /**
     * Constructor base para todas las tiendas.
     */
    public Store(int location, int tenges) {
        this.location = location;
        this.tenges = Math.max(1, tenges);
        this.initialTenges = this.tenges;
        this.color = assignColor();
    }

    /**
     * Método abstracto para asignar color según el tipo de tienda.
     */
    protected abstract String assignColor();

    /**
     * Método abstracto que define cómo la tienda es vaciada por un robot.
     * @param robot El robot que intenta vaciar la tienda
     * @return La cantidad de tenges que el robot obtiene (puede ser 0 si no puede vaciarla)
     */
    public abstract int empty(Robot robot);

    /**
     * Reabastecer la tienda a su estado inicial.
     */
    public void resupply() {
        tenges = initialTenges;
        updateView();
    }

    /**
     * Reiniciar completamente la tienda.
     */
    public void reboot() {
        tenges = initialTenges;
        collected = 0;
        timesEmptied = 0;
        updateView();
    }

    /**
     * Retorna el tipo de tienda como String.
     */
    public abstract String getType();

    // --- Getters y Setters ---
    public int getLocation() { 
        return location; 
    }
    
    public int getTenges() { 
        return tenges; 
    }
    
    public int getInitialTenges() { 
        return initialTenges; 
    }
    
    public void setTenges(int v) { 
        tenges = v; 
    }
    
    public int getTimesEmptied() {
        return timesEmptied;
    }
    
    public int getCollected() { 
        return collected; 
    }
    
    public StoreView getView() { 
        return view; 
    }
    
    public void setView(StoreView v) { 
        view = v; 
    }
    
    public String getColor() {
        return tenges > 0 ? color : "blue";
    }
    
    protected void updateView() {
        if (view != null) {
            view.erase();
            view.draw();
        }
    }
}
