package Domain;

import java.util.Random;

/**
 * Tienda Casino: Duplica las ganancias del robot o lo deja sin nada.
 * 50% de probabilidad de ganar el doble, 50% de perder todo.
 * ¡Alto riesgo, alta recompensa!
 */
public class CasinoStore extends Store {
    private static final String COLOR = "pink";
    private static Random random = new Random();

    public CasinoStore(int location, int tenges) {
        super(location, tenges);
    }

    @Override
    protected String assignColor() {
        return COLOR;
    }

    @Override
    public int empty(Robot robot) {
        if (tenges > 0) {
            int storeAmount = tenges;
            
            // 50% de probabilidad de ganar o perder
            boolean wins = random.nextBoolean();
            
            if (wins) {
                // ¡GANA! Duplica las ganancias
                int winAmount = storeAmount * 2;
                collected += storeAmount;
                tenges = 0;
                timesEmptied++;
                updateView();
                return winAmount;
            } else {
                // ¡PIERDE! El robot pierde todo lo que traía
                int lostAmount = robot.getTenges();
                collected += storeAmount;
                tenges = 0;
                timesEmptied++;
                updateView();
                return -lostAmount;
            }
        }
        return 0;
    }

    @Override
    public String getType() {
        return "Casino";
    }
}