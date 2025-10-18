import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Resuelve y simula el problema de la maratón "The Silk Road ... with Robots!".
 * Esta clase orquesta la simulación día a día, utilizando una estrategia de backtracking
 * para encontrar la asignación óptima de robots a tiendas que maximice la ganancia total.
 */
public class SilkRoadContest {
    /**
     * Clase interna para representar un movimiento potencial de un robot a una tienda.
     * Almacena el robot, la tienda de destino y la ganancia calculada para ese movimiento.
     */
    private static class Move {
        Robot robot;
        Store store;
        int profit;

        /**
         * Constructor para un objeto Move.
         * @param r El robot que realiza el movimiento.
         * @param s La tienda de destino.
         * @param p La ganancia neta del movimiento (tenges de la tienda - costo del viaje).
         */
        Move(Robot r, Store s, int p) {
            robot = r;
            store = s;
            profit = p;
        }

        /**
         * Representación en cadena del movimiento para una fácil depuración y visualización.
         * @return Una cadena que describe el movimiento.
         */
        public String toString() {
            return "Robot en " + (robot.getLocation() + 1) + " a tienda en " + (store.getLocation() + 1) + " (Profit: " + profit + ")";
        }
    }

    /**
     * Clase interna para almacenar el resultado del algoritmo de optimización.
     * Guarda la ganancia máxima encontrada y la lista de movimientos que la producen.
     */
    private static class Result {
        int maxProfit = 0;
        List<Move> bestMoves = new ArrayList<>();
    }

    /**
     * Simula la solución del problema día a día, mostrando los eventos y movimientos óptimos en la consola.
     * Ideal para visualizar el comportamiento del algoritmo paso a paso.
     * @param days Una matriz que representa los eventos de cada día. Cada fila es un evento.
     * @param slow Si es true, la simulación se ejecuta lentamente con pausas para facilitar la observación visual.
     */
    public void simulate(int[][] days, boolean slow) {
        System.out.println("--- INICIANDO SIMULACIÓN COMPLETA ---");
        SilkRoad road = null;
        ArrayList<int[]> dailyInputs = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            if (road != null) {
                road.finish(); // Cierra la ventana de la simulación anterior
            }
            
            System.out.println("DÍA " + (i + 1) + " ");
        
            int[] currentEvent = days[i];
            dailyInputs.add(currentEvent);
            if (currentEvent[0] == 1) { // Evento de añadir robot
                System.out.println("Evento del día: Añadiendo nuevo Robot en la posición " + currentEvent[1] + ".");
            } else { // Evento de añadir tienda
                System.out.println("Evento del día: Añadiendo nueva Tienda en la posición " + currentEvent[1] + " con " + currentEvent[2] + " tenges.");
            }
            
            System.out.println("Estado del tablero: Reabasteciendo tiendas y devolviendo robots a sus posiciones iniciales.");
            road = new SilkRoad(dailyInputs.toArray(new int[0][0]));
            
            if (slow) road.wait(2000);
            System.out.println("Fase de cálculo: Buscando la mejor combinación de movimientos para maximizar la ganancia...");
            Result solution = findOptimalAssignment(road);
            
            if (slow) road.wait(1500);

            if (solution.bestMoves.isEmpty()) {
                System.out.println("Resultado del cálculo: No se encontraron movimientos rentables para este día.");
            } else {
                System.out.println("Fase de movimiento: Ejecutando los movimientos óptimos encontrados.");
                for (Move move : solution.bestMoves) {
                    System.out.println("  -> Moviendo: " + move);
                    int robotStart = move.robot.getLocation() + 1;
                    int storeTarget = move.store.getLocation();
                    int boardSize = road.getBoardSize();
                    
                    // Calcular el camino más corto
                    int distFwd = (storeTarget - (robotStart - 1) + boardSize) % boardSize;
                    int distBwd = ((robotStart - 1) - storeTarget + boardSize) % boardSize;
                    int steps = (distFwd <= distBwd) ? distFwd : -distBwd;
                    
                    road.moveRobot(robotStart, steps);
                    if (slow) road.wait(2000);
                }
            }
            
            System.out.println("----------------------------------------------");
            System.out.println(">>> Profit máximo para el Día " + (i + 1) + ": " + solution.maxProfit + " <<<");
            System.out.println("----------------------------------------------");
            if (slow && i < days.length - 1) road.wait(4000);
        }
        System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
    }
    
    /**
     * Resuelve el problema de la maratón de forma no visual.
     * Calcula la ganancia máxima para cada día y la devuelve como un arreglo.
     * @param days La entrada del problema, donde cada fila es un evento diario.
     * @return Un arreglo de enteros con la ganancia máxima de cada día.
     */
    public int[] solve(int[][] days) {
        int[] dailyProfits = new int[days.length];
        ArrayList<int[]> dailyInputs = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            dailyInputs.add(days[i]);
            SilkRoad road = new SilkRoad(dailyInputs.toArray(new int[0][0])); // Modo sin GUI (headless)
            Result solution = findOptimalAssignment(road);
            dailyProfits[i] = solution.maxProfit;
        }
        return dailyProfits;
    }

    /**
     * Prepara e inicia el proceso de búsqueda recursiva para encontrar la asignación óptima.
     * @param road La instancia de SilkRoad que contiene el estado actual del tablero.
     * @return Un objeto Result con la ganancia máxima y los movimientos correspondientes.
     */
    private Result findOptimalAssignment(SilkRoad road) {
        List<Robot> robots = road.getRobots();
        List<Store> stores = road.getStores().stream().filter(s -> s.getTenges() > 0).collect(Collectors.toList());
        Result finalResult = new Result();
        if (robots.isEmpty() || stores.isEmpty()) {
            return finalResult; // No hay movimientos posibles
        }
        solveAssignmentRecursive(0, robots, stores, new ArrayList<>(), 0, finalResult, road.getBoardSize());
        return finalResult;
    }
    
    /**
     * Algoritmo de backtracking recursivo para resolver el problema de asignación.
     * Explora todas las combinaciones posibles de asignaciones de robots a tiendas (donde cada tienda solo
     * puede ser visitada una vez) y encuentra la combinación que maximiza la ganancia total.
     * @param robotIndex El índice del robot actual que se está considerando para una asignación.
     * @param robots La lista de todos los robots.
     * @param availableStores La lista de tiendas que aún no han sido asignadas.
     * @param currentMoves La lista de movimientos en la ruta de recursión actual.
     * @param currentProfit La ganancia acumulada en la ruta de recursión actual.
     * @param result El objeto Result para almacenar la mejor solución encontrada globalmente.
     * @param boardSize El tamaño total del tablero.
     */
    private void solveAssignmentRecursive(int robotIndex, List<Robot> robots, List<Store> availableStores, List<Move> currentMoves, int currentProfit, Result result, int boardSize) {
        // Caso base: Todos los robots han sido considerados.
        if (robotIndex == robots.size()) {
            if (currentProfit > result.maxProfit) {
                result.maxProfit = currentProfit;
                result.bestMoves = new ArrayList<>(currentMoves);
            }
            return;
        }

        Robot currentRobot = robots.get(robotIndex);

        // Opción 1: El robot actual no se mueve. Procede al siguiente robot.
        solveAssignmentRecursive(robotIndex + 1, robots, availableStores, currentMoves, currentProfit, result, boardSize);

        // Opción 2: Asignar el robot actual a cada una de las tiendas disponibles.
        for (int i = 0; i < availableStores.size(); i++) {
            Store store = availableStores.get(i);
            int robotLoc = currentRobot.getLocation();
            int storeLoc = store.getLocation();
            int distance = Math.min((storeLoc - robotLoc + boardSize) % boardSize, (robotLoc - storeLoc + boardSize) % boardSize);
            int profit = store.getTenges() - distance;

            if (profit > 0) { // Solo considera movimientos rentables
                List<Store> remainingStores = new ArrayList<>(availableStores);
                remainingStores.remove(i); // La tienda ya no está disponible para otros robots

                currentMoves.add(new Move(currentRobot, store, profit));
                // Llamada recursiva para el siguiente robot con el estado actualizado
                solveAssignmentRecursive(robotIndex + 1, robots, remainingStores, currentMoves, currentProfit + profit, result, boardSize);
                currentMoves.remove(currentMoves.size() - 1); // Backtrack: deshace el movimiento
            }
        }
    }
    
    /**
     * Método principal para probar la clase directamente.
     * Ejecuta los métodos solve() y simulate() con datos de ejemplo y muestra los resultados.
     * @param args Argumentos de la línea de comandos (no utilizados).
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