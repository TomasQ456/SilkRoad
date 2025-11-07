package Domain;

/**
 * Robot NeverBack: Nunca retrocede.
 * Solo puede moverse hacia adelante en la espiral, nunca hacia atrás.
 */
public class NeverBackRobot extends Robot {
    private static final String COLOR = "purple";

    public NeverBackRobot(int location) {
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
        // ¡Nunca retrocede!
        return false;
    }

    @Override
    public int collectFromStore(int storeAmount) {
        // Toma todo lo que la tienda ofrece
        return storeAmount;
    }

    @Override
    public String getType() {
        return "NeverBack";
    }
}