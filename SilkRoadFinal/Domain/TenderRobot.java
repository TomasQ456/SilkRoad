package Domain;

/**
 * Robot Tender (Tierno): Solo toma la mitad del dinero de las tiendas.
 * Es considerado con las tiendas y no las vacía completamente.
 */
public class TenderRobot extends Robot {
    private static final String COLOR = "lightGray";

    public TenderRobot(int location) {
        super(location);
    }

    @Override
    protected String assignColor() {
        return COLOR;
    }

    @Override
    public int getMovementCost(int distance) {
        // Costo estándar: 1 tenge por casilla
        return distance;
    }

    @Override
    public boolean canMoveBackward() {
        return true;
    }

    @Override
    public int collectFromStore(int storeAmount) {
        // Solo toma la MITAD de lo que la tienda ofrece
        // La tienda mantiene la otra mitad
        return storeAmount / 2;
    }

    @Override
    public String getType() {
        return "Tender";
    }
}