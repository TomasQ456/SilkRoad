package Domain;

/**
 * Robot Greedy (Codicioso): Toma el 150% del dinero de las tiendas.
 * Pero gasta el doble en movimiento (2 tenges por casilla en vez de 1).
 * Alto riesgo, alta recompensa.
 */
public class GreedyRobot extends Robot {
    private static final String COLOR = "darkGray";

    public GreedyRobot(int location) {
        super(location);
    }

    @Override
    protected String assignColor() {
        return COLOR;
    }

    @Override
    public int getMovementCost(int distance) {
        // ¡Gasta el DOBLE! 2 tenges por casilla
        return distance * 2;
    }

    @Override
    public boolean canMoveBackward() {
        return true;
    }

    @Override
    public int collectFromStore(int storeAmount) {
        // ¡Toma el 150%! (1.5 veces lo que la tienda tiene)
        return (storeAmount * 3) / 2;
    }

    @Override
    public String getType() {
        return "Greedy";
    }
}