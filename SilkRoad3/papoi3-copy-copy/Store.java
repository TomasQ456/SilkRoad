import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representa una tienda en la simulación de Silk Road.
 * Cada tienda tiene una ubicación, una cantidad de "tenges" (moneda) y un color.
 * El color cambia a negro cuando la tienda es vaciada por un robot.
 */
public class Store {
    private int location;
    private int tenges;
    private int initialTenges;
    private StoreView view;
    private String color;
    private int collected = 0;
    private int timesEmptied = 0;
    
    // Lista estática de colores disponibles para las nuevas tiendas.
    private static final List<String> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
        "green", "yellow", "magenta", "red", "blue"
    ));
    // Índice para asignar el siguiente color de la lista de forma cíclica.
    private static int nextColorIndex = 0;
    
    // Bloque estático que baraja la lista de colores una vez al cargar la clase.
    // Esto asegura que las tiendas tengan colores aleatorios en cada ejecución.
    static { 
        Collections.shuffle(AVAILABLE_COLORS); 
    }

    /**
     * Constructor para crear una nueva tienda.
     * @param location La posición de la tienda en el tablero (basada en índice 0).
     * @param tenges La cantidad inicial de tenges. Se asegura que sea al menos 1.
     */
    public Store(int location, int tenges) {
        this.location = location;
        this.tenges = Math.max(1, tenges); // Asegura que las tiendas tengan al menos 1 tenge.
        this.initialTenges = this.tenges;
        this.color = getNextUniqueColor();
    }  

    /**
     * Asigna un color único de la lista de colores disponibles de forma cíclica.
     * @return Un String que representa el color asignado.
     */
    private static String getNextUniqueColor() {
        String color = AVAILABLE_COLORS.get(nextColorIndex);
        nextColorIndex = (nextColorIndex + 1) % AVAILABLE_COLORS.size();
        return color;
    }

    /**
     * Vacía la tienda, poniendo sus tenges a 0 y actualizando las estadísticas.
     */
    public void empty() {
        collected += tenges;
        tenges = 0;
        timesEmptied++;
        updateView();
    }

    /**
     * Reinicia la tienda a su estado completamente original.
     * Restaura los tenges iniciales y resetea los contadores.
     */
    public void reboot() {
        tenges = initialTenges;
        collected = 0;
        timesEmptied = 0; 
        updateView();
    }
    
    /**
     * Obtiene el número de veces que la tienda ha sido vaciada.
     * @return El contador de veces que ha sido vaciada.
     */
    public int getTimesEmptied() {
        return timesEmptied;
    }

    /**
     * Obtiene la ubicación de la tienda en el tablero.
     * @return La posición de la tienda.
     */
    public int getLocation() { 
        return location; 
    }
    
    /**
     * Obtiene la cantidad actual de tenges en la tienda.
     * @return Los tenges actuales.
     */
    public int getTenges() { 
        return tenges; 
    }
    
    /**
     * Obtiene la cantidad inicial de tenges con la que se creó la tienda.
     * @return Los tenges iniciales.
     */
    public int getInitialTenges() { 
        return initialTenges; 
    }

    /**
     * Establece la cantidad de tenges a un valor específico.
     * @param v El nuevo valor de tenges.
     */
    public void setTenges(int v) {
        tenges = v;
    }
    
    /**
     * Reabastece la tienda, restaurando sus tenges al valor inicial.
     */
    public void resupply() { 
        tenges = initialTenges; 
        updateView(); 
    }
    
    /**
     * Obtiene el total de tenges que han sido recolectados de esta tienda.
     * @return El total de tenges recolectados.
     */
    public int getCollected() { 
        return collected; 
    }
    
    /**
     * Obtiene la vista gráfica asociada a esta tienda.
     * @return La instancia de StoreView.
     */
    public StoreView getView() { 
        return view; 
    }
    
    /**
     * Asigna una vista (representación gráfica) a esta tienda.
     * @param v La instancia de StoreView a asociar.
     */
    public void setView(StoreView v) { 
        view = v; 
    }
 
    /**
     * Devuelve el color actual de la tienda. Si la tienda está vacía (tenges <= 0),
     * devuelve "black". De lo contrario, devuelve su color asignado.
     * @return El nombre del color como un String.
     */
    public String getColor() {
        return tenges > 0 ? color : "black";
    }
    
    /**
     * Método privado para actualizar la representación gráfica de la tienda
     * si existe una vista asociada.
     */
    private void updateView() {
        if (view != null) {
            view.erase();
            view.draw();
        }
    }
}