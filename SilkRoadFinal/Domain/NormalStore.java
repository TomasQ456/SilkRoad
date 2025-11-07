package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tienda Normal: Comportamiento estándar, entrega todos sus tenges al robot.
 */
public class NormalStore extends Store {
    private static final List<String> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
        "green", "yellow", "magenta", "red", "blue"
    ));
    private static int nextColorIndex = 0;
    
    static { 
        Collections.shuffle(AVAILABLE_COLORS); 
    }

    public NormalStore(int location, int tenges) {
        super(location, tenges);
    }

    @Override
    protected String assignColor() {
        String color = AVAILABLE_COLORS.get(nextColorIndex);
        nextColorIndex = (nextColorIndex + 1) % AVAILABLE_COLORS.size();
        return color;
    }

    @Override
        public int empty(Robot robot) {
            if (tenges > 0) {
                int amount = tenges;
                collected += tenges;
                tenges = 0;
                timesEmptied++;
                updateView();
                return robot.collectFromStore(amount);
            }
            return 0;
        }

    @Override
    public String getType() {
        return "Normal";
    }
}