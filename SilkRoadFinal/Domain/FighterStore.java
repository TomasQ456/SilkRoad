package Domain;

/**
 * Tienda Luchadora: Solo robots con más tenges que ella pueden tomar su dinero.
 * Defiende sus recursos de robots pobres.
 */
public class FighterStore extends Store {
    private static final String COLOR = "orange";

    public FighterStore(int location, int tenges) {
        super(location, tenges);
    }

    @Override
    protected String assignColor() {
        return COLOR;
    }

    @Override
    public int empty(Robot robot) {
        if (tenges > 0) {
            // Solo permite el vaciado si el robot tiene MÁS tenges que la tienda
            if (robot.getTenges() > tenges) {
                int amount = tenges;
                collected += tenges;
                tenges = 0;
                timesEmptied++;
                updateView();
                System.out.println("FighterStore en " + (location + 1) + 
                                 ": Robot con " + robot.getTenges() + 
                                 " tenges venció y tomó " + amount + " tenges.");
                return amount;
            } else {
                System.out.println("FighterStore en " + (location + 1) + 
                                 ": Robot con " + robot.getTenges() + 
                                 " tenges no pudo vencer (necesita > " + tenges + ").");
                return 0;
            }
        }
        return 0;
    }

    @Override
    public String getType() {
        return "Fighter";
    }
}