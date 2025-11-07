package Domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Resuelve y simula el problema de la maratón "The Silk Road ... with Robots!".
 * Esta clase orquesta la simulación día a día usando la clase SilkRoad.
 */
public class SilkRoadContest {

    // --- Clases internas de ayuda para la solución ---
    private static class Move {
        Robot robot; Store store; int profit;
        Move(Robot r, Store s, int p) { robot = r; store = s; profit = p; }
        public String toString() { return "Robot en " + (robot.getLocation() + 1) + " a tienda en " + (store.getLocation() + 1) + " (Profit: " + profit + ")"; }
    }
    private static class Result {
        int maxProfit = 0;
        List<Move> bestMoves = new ArrayList<>();
    }

    /**
     * Simula la solución del problema día a día, mostrando los movimientos óptimos.
     * @param days La entrada del problema de la maratón.
     * @param slow Si es true, la simulación se ejecuta lentamente con pausas.
     */
    public void simulate(int[][] days, boolean slow) {
        System.out.println("--- INICIANDO SIMULACIÓN COMPLETA ---");
        SilkRoad road = null;
        ArrayList<int[]> dailyInputs = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            if (road != null) {
                road.finish(); // Cierra la ventana del día anterior.
            }
            
            System.out.println("\n================ DÍA " + (i + 1) + " ================");
            
            // 1. Añadir el evento del día actual
            int[] currentEvent = days[i];
            dailyInputs.add(currentEvent);
            if (currentEvent[0] == 1) {
                System.out.println("Evento del día: Añadiendo nuevo Robot en la posición " + currentEvent[1] + ".");
            } else {
                System.out.println("Evento del día: Añadiendo nueva Tienda en la posición " + currentEvent[1] + " con " + currentEvent[2] + " tenges.");
            }
            
            // 2. Crear una nueva instancia de SilkRoad para simular el reinicio de cada día.
            System.out.println("Estado del tablero: Reabasteciendo tiendas y devolviendo robots a sus posiciones iniciales.");
            road = new SilkRoad(dailyInputs.toArray(new int[0][0]));
            
            if (slow) road.wait(2000);

            // 3. Calcular la solución óptima para el estado actual del tablero.
            System.out.println("Fase de cálculo: Buscando la mejor combinación de movimientos para maximizar la ganancia...");
            Result solution = findOptimalAssignment(road);
            
            if (slow) road.wait(1500);

            // 4. Ejecutar y mostrar los movimientos de la solución encontrada.
            if (solution.bestMoves.isEmpty()) {
                System.out.println("Resultado del cálculo: No se encontraron movimientos rentables para este día.");
            } else {
                System.out.println("Fase de movimiento: Ejecutando los movimientos óptimos encontrados.");
                for (Move move : solution.bestMoves) {
                    System.out.println("  -> Moviendo: " + move);
                    int robotStart = move.robot.getLocation() + 1;
                    int storeTarget = move.store.getLocation();
                    int boardSize = road.getBoardSize();
                    
                    int distFwd = (storeTarget - (robotStart - 1) + boardSize) % boardSize;
                    int distBwd = ((robotStart - 1) - storeTarget + boardSize) % boardSize;
                    int steps = (distFwd <= distBwd) ? distFwd : -distBwd;
                    
                    road.moveRobot(robotStart, steps);
                    if (slow) road.wait(2000);
                }
            }
            
            // 5. Mostrar el resultado final del día.
            System.out.println("----------------------------------------------");
            System.out.println(">>> Profit máximo para el Día " + (i + 1) + ": " + solution.maxProfit + " <<<");
            System.out.println("----------------------------------------------");
            if (slow && i < days.length - 1) road.wait(4000);
        }
        System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
    }
    
    /**
     * Resuelve el problema de la maratón y devuelve un arreglo con el profit máximo de cada día.
     */
    public int[] solve(int[][] days) {
        int[] dailyProfits = new int[days.length];
        ArrayList<int[]> dailyInputs = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            dailyInputs.add(days[i]);
            SilkRoad road = new SilkRoad(dailyInputs.toArray(new int[0][0]));
            Result solution = findOptimalAssignment(road);
            dailyProfits[i] = solution.maxProfit;
        }
        return dailyProfits;
    }

    // --- Lógica de Solución Óptima (Backtracking) ---
    private Result findOptimalAssignment(SilkRoad road) {
        List<Robot> robots = road.getRobots();
        List<Store> stores = road.getStores().stream().filter(s -> s.getTenges() > 0).collect(Collectors.toList());
        Result finalResult = new Result();
        if (robots.isEmpty() || stores.isEmpty()) { return finalResult; }
        solveAssignmentRecursive(0, robots, stores, new ArrayList<>(), 0, finalResult, road.getBoardSize());
        return finalResult;
    }
    
    private void solveAssignmentRecursive(int robotIndex, List<Robot> robots, List<Store> availableStores, List<Move> currentMoves, int currentProfit, Result result, int boardSize) {
        if (robotIndex == robots.size()) {
            if (currentProfit > result.maxProfit) {
                result.maxProfit = currentProfit;
                result.bestMoves = new ArrayList<>(currentMoves);
            }
            return;
        }
        Robot currentRobot = robots.get(robotIndex);
        solveAssignmentRecursive(robotIndex + 1, robots, availableStores, currentMoves, currentProfit, result, boardSize);
        for (int i = 0; i < availableStores.size(); i++) {
            Store store = availableStores.get(i);
            int robotLoc = currentRobot.getLocation();
            int storeLoc = store.getLocation();
            int distance = Math.min((storeLoc - robotLoc + boardSize) % boardSize, (robotLoc - storeLoc + boardSize) % boardSize);
            int profit = store.getTenges() - distance;
            if (profit > 0) {
                List<Store> remainingStores = new ArrayList<>(availableStores);
                remainingStores.remove(i);
                currentMoves.add(new Move(currentRobot, store, profit));
                solveAssignmentRecursive(robotIndex + 1, robots, remainingStores, currentMoves, currentProfit + profit, result, boardSize);
                currentMoves.remove(currentMoves.size() - 1);
            }
        }
    }
    
    /**
     * Método main para probar la clase directamente.
     */
    public static void main(String[] args) {
        int[][] sampleDays = {
            {1, 20},
            {2, 15, 15},
            {2, 40, 50},
            {1, 50},
            {2, 80, 20},
            {2, 70, 30}
        };

        SilkRoadContest contest = new SilkRoadContest();
        
        System.out.println("--- Ejecutando solve() ---");
        int[] solution = contest.solve(sampleDays);
        System.out.println("Resultado de solve (profits diarios): " + Arrays.toString(solution));
        System.out.println("Salida esperada: [0, 10, 35, 50, 50, 60]");
        System.out.println();
        
        System.out.println("--- Ejecutando simulate() ---");
        contest.simulate(sampleDays, true);
    }
}
