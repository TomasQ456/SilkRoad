package Domain;

import java.util.Random;

/**
 * Tienda Autónoma: Escoge su propia posición en el tablero.
 * Ignora la ubicación proporcionada y selecciona una aleatoria disponible.
 */
public class AutonomousStore extends Store {
    private static final String COLOR = "cyan";
    private static Random random = new Random();

    public AutonomousStore(int suggestedLocation, int tenges, int boardSize, java.util.function.Predicate<Integer> isAvailable) {
        super(findAvailableLocation(suggestedLocation, boardSize, isAvailable), tenges);
    }

    /**
     * Encuentra una ubicación disponible en el tablero.
     * Si la sugerida no está disponible, busca otra aleatoriamente.
     */
    private static int findAvailableLocation(int suggested, int boardSize, java.util.function.Predicate<Integer> isAvailable) {
        // Intentar con la ubicación sugerida primero
        if (isAvailable.test(suggested)) {
            return suggested;
        }
        
        // Buscar una ubicación aleatoria disponible
        int attempts = 0;
        int maxAttempts = boardSize * 2;
        
        while (attempts < maxAttempts) {
            int randomLoc = random.nextInt(boardSize);
            if (isAvailable.test(randomLoc)) {
                System.out.println("AutonomousStore: Ubicación " + (suggested + 1) + 
                                 " no disponible. Eligiendo ubicación " + (randomLoc + 1));
                return randomLoc;
            }
            attempts++;
        }
        
        // Si no encuentra ninguna, usar la sugerida (causará error en SilkRoad)
        return suggested;
    }

    @Override
    protected String assignColor() {
        return COLOR;
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
        return "Autonomous";
    }
}