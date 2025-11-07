package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Robot Normal: Comportamiento estándar.
 * Puede moverse en ambas direcciones, costo de 1 tenge por casilla, toma todo de las tiendas.
 */
public class NormalRobot extends Robot {
    private static final List<String> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
        "red", "blue", "green", "yellow", "magenta", "black"
    ));
    private static int nextColorIndex = 0;
    
    static { 
        Collections.shuffle(AVAILABLE_COLORS); 
    }

    public NormalRobot(int location) {
        super(location);
    }

    @Override
    protected String assignColor() {
        if (AVAILABLE_COLORS.isEmpty()) { return "black"; }
        String color = AVAILABLE_COLORS.get(nextColorIndex);
        nextColorIndex = (nextColorIndex + 1) % AVAILABLE_COLORS.size();
        return color;
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
        // Toma todo lo que la tienda ofrece
        return storeAmount;
    }

    @Override
    public String getType() {
        return "Normal";
    }
}