package Domain;
import Presentation.*;

/**
 * Representa un punto de comercio en la Ruta de la Seda.
 * Esta clase abstracta define el comportamiento base de todas las tiendas
 * y establece los mecanismos de interacción con los robots comerciantes.
 * 
 * <p>Las tiendas son entidades que:
 * <ul>
 *   <li>Contienen una cantidad de tenges para comerciar</li>
 *   <li>Tienen diferentes estrategias de negociación con los robots</li>
 *   <li>Mantienen estadísticas de sus transacciones</li>
 *   <li>Pueden ser reabastecidas o reiniciadas</li>
 * </ul>
 * 
 * <p>El sistema permite crear diferentes tipos de tiendas con distintas
 * estrategias de negociación, lo que enriquece la dinámica del juego.
 */
public abstract class Store {
    /** Posición fija en el tablero (0-based) */
    protected int location;
    /** Cantidad actual de tenges disponibles para comerciar */
    protected int tenges;
    /** Cantidad inicial de tenges, usada para reabastecer */
    protected int initialTenges;
    /** Referencia a la vista para la representación gráfica */
    protected StoreView view;
    /** Color identificativo del tipo de tienda */
    protected String color;
    /** Total de tenges recolectados por robots */
    protected int collected = 0;
    /** Número de veces que la tienda ha sido vaciada */
    protected int timesEmptied = 0;

    /**
     * Inicializa una nueva tienda con una ubicación y cantidad de tenges específica.
     * Se asegura que la tienda siempre tenga al menos 1 tenge para mantener
     * la dinámica del juego.
     * 
     * @param location Posición en el tablero (0-based)
     * @param tenges Cantidad inicial de tenges (mínimo 1)
     */
    public Store(int location, int tenges) {
        this.location = location;
        this.tenges = Math.max(1, tenges);
        this.initialTenges = this.tenges;
        this.color = assignColor();
    }

    /**
     * Define el color distintivo de la tienda.
     * Cada tipo de tienda usa un color diferente para facilitar
     * su identificación visual en el tablero.
     * 
     * @return Color en formato String que representa el tipo de tienda
     */
    protected abstract String assignColor();

    /**
     * Implementa la lógica de negociación con un robot.
     * Este método es el núcleo de la interacción comercial y define
     * cómo cada tipo de tienda responde a las solicitudes de los robots.
     * 
     * @param robot El robot que intenta comerciar con la tienda
     * @return Cantidad de tenges que la tienda decide entregar
     */
    public abstract int empty(Robot robot);

    /**
     * Reabastece la tienda a su cantidad inicial de tenges.
     * Útil para simular ciclos comerciales o reiniciar rondas
     * sin perder las estadísticas acumuladas.
     */
    public void resupply() {
        tenges = initialTenges;
        updateView();
    }

    /**
     * Reinicia completamente la tienda a su estado inicial.
     * A diferencia de resupply(), este método también reinicia
     * todas las estadísticas de comercio.
     */
    public void reboot() {
        tenges = initialTenges;
        collected = 0;
        timesEmptied = 0;
        updateView();
    }

    /**
     * Identifica el tipo específico de tienda.
     * Útil para logging, debugging y toma de decisiones estratégicas.
     * 
     * @return Nombre del tipo de tienda
     */
    public abstract String getType();

    // --- Métodos de acceso y modificación ---
    
    /**
     * Obtiene la posición de la tienda en el tablero.
     * @return Posición (0-based)
     */
    public int getLocation() { 
        return location; 
    }
    
    /**
     * Obtiene la cantidad actual de tenges disponibles.
     * @return Cantidad de tenges
     */
    public int getTenges() { 
        return tenges; 
    }
    
    /**
     * Obtiene la cantidad inicial de tenges.
     * Útil para cálculos estadísticos y reabastecimiento.
     * @return Cantidad inicial de tenges
     */
    public int getInitialTenges() { 
        return initialTenges; 
    }
    
    /**
     * Actualiza la cantidad de tenges disponibles.
     * @param v Nueva cantidad de tenges
     */
    public void setTenges(int v) { 
        tenges = v; 
    }
    
    /**
     * Obtiene el número de veces que la tienda ha sido vaciada.
     * Métrica útil para análisis de rendimiento.
     * @return Número de veces vaciada
     */
    public int getTimesEmptied() {
        return timesEmptied;
    }
    
    /**
     * Obtiene el total de tenges recolectados por robots.
     * @return Total de tenges recolectados
     */
    public int getCollected() { 
        return collected; 
    }
    
    /**
     * Gestiona la representación visual de la tienda.
     * @return Vista actual de la tienda
     */
    public StoreView getView() { 
        return view; 
    }
    
    /**
     * Actualiza la representación visual de la tienda.
     * @param v Nueva vista a asignar
     */
    public void setView(StoreView v) { 
        view = v; 
    }
    
    /**
     * Determina el color de la tienda según su estado.
     * Las tiendas vacías se muestran en azul para indicar
     * que no tienen tenges disponibles.
     * 
     * @return Color actual de la tienda
     */
    public String getColor() {
        return tenges > 0 ? color : "blue";
    }
    
    /**
     * Actualiza la representación visual de la tienda.
     * Se llama automáticamente cuando hay cambios en el estado.
     */
    protected void updateView() {
        if (view != null) {
            view.erase();
            view.draw();
        }
    }
}