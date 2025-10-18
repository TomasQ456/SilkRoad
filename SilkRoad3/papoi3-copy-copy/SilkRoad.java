import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

/**
 * Gestiona la simulación de "Silk Road", un tablero en espiral donde interactúan
 * robots y tiendas. Esta clase controla el estado del juego, la lógica de movimiento,
 * la colocación de elementos y la interfaz gráfica.
 */
public class SilkRoad {
    private int size;
    private int[][] spiral;
    private ArrayList<Store> stores;
    private ArrayList<Robot> robots;
    private SilkRoadView view;
    private boolean lastOk = true;
    private Map<Integer, Store> storeLocations;

    /**
     * Inicializa los componentes principales de la simulación.
     * Este método es utilizado por los constructores para evitar la duplicación de código.
     * @param size La dimensión del tablero (para un tablero de n x n).
     */
    private void init(int size) {
        this.size = size;
        this.spiral = generateSpiral(size);
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.storeLocations = new HashMap<>();
        // Inicializa la vista solo si no está en modo "headless" (sin cabeza/sin GUI)
        if (System.getProperty("java.awt.headless") == null) {
            this.view = new SilkRoadView(this, this.size, this.spiral);
        } else {
            this.view = null;
        }
    }

    /**
     * Constructor para crear una simulación de Silk Road con un tamaño de tablero específico.
     * @param size La dimensión del tablero (e.g., 5 para un tablero de 5x5).
     */
    public SilkRoad(int size) {
        init(size);
    }

    /**
     * Constructor para crear una simulación a partir de una matriz de datos de entrada.
     * Este formato es útil para escenarios predefinidos, como maratones de programación.
     * @param marathonInput Una matriz donde cada fila define un robot o una tienda.
     * Formato: {tipo, ubicación, [tenges]}
     * - tipo 1: Robot
     * - tipo 2: Tienda
     */
    public SilkRoad(int[][] marathonInput) {
        int maxLocation = 0;
        for (int[] itemData : marathonInput) {
            if (itemData.length > 1) {
                maxLocation = Math.max(maxLocation, itemData[1]);
            }
        }
        int calculatedSize = (int) Math.ceil(Math.sqrt(maxLocation + 1));
        init(calculatedSize);
        for (int[] itemData : marathonInput) {
            int type = itemData[0];
            if (type == 1) { // Robot
                placeRobot(itemData[1]);
            } else if (type == 2) { // Tienda
                if (itemData.length > 2) {
                    placeStore(itemData[1], itemData[2]);
                }
            }
        }
        if (this.view != null) {
            makeVisible();
        }
    }

    /**
     * Coloca una nueva tienda en una ubicación específica del tablero.
     * La operación falla si la ubicación está fuera de los límites o ya está ocupada.
     * @param location La posición en el tablero (desde 1 hasta size*size).
     * @param tenges La cantidad inicial de "tenges" (moneda) en la tienda.
     */
    public void placeStore(int location, int tenges) {
        lastOk = true;
        int internalLocation = location - 1;

        if (location < 1 || internalLocation >= size * size) {
            lastOk = false;
            return;
        }

        boolean isOccupied = storeLocations.containsKey(internalLocation) ||
                             robots.stream().anyMatch(r -> r.getLocation() == internalLocation);

        if (isOccupied) {
            lastOk = false;
            return;
        }

        Store s = new Store(internalLocation, tenges);
        stores.add(s);
        storeLocations.put(internalLocation, s);
        if (view != null) view.drawStore(s);
    }

    /**
     * Coloca un nuevo robot en una ubicación específica del tablero.
     * La operación falla si la ubicación está fuera de los límites o ya está ocupada.
     * @param location La posición en el tablero (desde 1 hasta size*size).
     */
    public void placeRobot(int location) {
        lastOk = true;
        int internalLocation = location - 1;

        if (location < 1 || internalLocation >= size * size) {
            lastOk = false;
            return;
        }

        boolean isOccupied = storeLocations.containsKey(internalLocation) ||
                             robots.stream().anyMatch(r -> r.getLocation() == internalLocation);

        if (isOccupied) {
            lastOk = false;
            return;
        }

        Robot r = new Robot(internalLocation);
        robots.add(r);
        if (view != null) {
            view.drawRobot(r);
            updateBlinkingRobot();
        }
    }

    /**
     * Elimina una tienda de una ubicación específica.
     * @param location La posición de la tienda a eliminar (desde 1 hasta size*size).
     */
    public void removeStore(int location) {
        lastOk = false;
        location -= 1; // Convertir a índice interno
        Iterator<Store> it = stores.iterator();
        while(it.hasNext()) {
            Store s = it.next();
            if(s.getLocation() == location) {
                if(view != null) view.eraseStore(s);
                it.remove();
                storeLocations.remove(location);
                lastOk = true;
                break;
            }
        }
        if (!lastOk) {
            JOptionPane.showMessageDialog(null, "Error: No se encontró una tienda en la ubicación " + (location + 1) + " para eliminar.");
        }
    }

    /**
     * Elimina un robot de una ubicación específica.
     * @param location La posición del robot a eliminar (desde 1 hasta size*size).
     */
    public void removeRobot(int location) {
        lastOk = false;
        location -= 1; // Convertir a índice interno
        Iterator<Robot> it = robots.iterator();
        while(it.hasNext()){
            Robot r = it.next();
            if(r.getLocation() == location){
                r.setBlinking(false);
                if(view != null) view.eraseRobot(r);
                it.remove();
                lastOk = true;
                if(view != null) updateBlinkingRobot();
                break;
            }
        }
        if (!lastOk) {
            JOptionPane.showMessageDialog(null, "Error: No se encontró un robot en la ubicación " + (location + 1) + " para eliminar.");
        }
    }

    /**
     * Activa el movimiento automático para todos los robots. Cada robot busca la tienda
     * más rentable (ganancia - costo de movimiento) y se mueve hacia ella para vaciarla.
     * Solo se moverán si la ganancia potencial es positiva.
     */
    public void moveRobots() {
        ArrayList<Robot> robotsToMove = new ArrayList<>(this.robots);
        for (Robot robot : robotsToMove) {
            Store bestTargetStore = null;
            int maxProfit = Integer.MIN_VALUE;
            int stepsToMove = 0;
            int robotLocation = robot.getLocation();
            int totalSquares = size * size;

            for (Store store : this.stores) {
                if (store.getTenges() > 0) {
                    int storeLocation = store.getLocation();
                    // Calcular distancia en ambas direcciones de la espiral
                    int distFwd = (storeLocation - robotLocation + totalSquares) % totalSquares;
                    int distBwd = (robotLocation - storeLocation + totalSquares) % totalSquares;
                    int shortestDist = Math.min(distFwd, distBwd);
                    int direction = (distFwd <= distBwd) ? 1 : -1;

                    if (shortestDist == 0) continue;

                    int potentialProfit = store.getTenges() - shortestDist;
                    if (potentialProfit > maxProfit) {
                        maxProfit = potentialProfit;
                        bestTargetStore = store;
                        stepsToMove = shortestDist * direction;
                    }
                }
            }
            if (bestTargetStore != null && maxProfit > 0) {
                moveRobot(robot.getLocation() + 1, stepsToMove);
            }
        }
        if (view != null) updateBlinkingRobot();
    }

    /**
     * Mueve un robot específico un número determinado de pasos a lo largo de la espiral.
     * El robot se detendrá en la primera tienda con tenges que encuentre en su camino.
     * @param location La ubicación actual del robot a mover (desde 1 hasta size*size).
     * @param steps El número de pasos a mover. Positivo para avanzar, negativo para retroceder.
     */
    public void moveRobot(int location, int steps) {
        lastOk = true;
        final int targetLocation = location - 1;

        Robot robot = robots.stream()
                            .filter(r -> r.getLocation() == targetLocation)
                            .findFirst()
                            .orElse(null);

        if (robot == null) {
            lastOk = false;
            return;
        }
        int totalSquares = size * size;
        int currentLoc = robot.getLocation();
        int stepDir = (steps >= 0) ? 1 : -1;
        int stepsLeft = Math.abs(steps);
        int distanceTraveled = 0;
        Store targetStore = null;

        while (stepsLeft > 0) {
            currentLoc = (currentLoc + stepDir + totalSquares) % totalSquares;
            distanceTraveled++;
            Store storeAtStep = storeLocations.get(currentLoc);
            if (storeAtStep != null && storeAtStep.getTenges() > 0) {
                targetStore = storeAtStep;
                break;
            }
            stepsLeft--;
        }

        int profitThisMove = -distanceTraveled;
        robot.addTenges(-distanceTraveled); // Costo del viaje

        if (targetStore != null) {
            int collectedAmount = targetStore.getTenges();
            robot.addTenges(collectedAmount); // Ganancia de la tienda
            targetStore.empty();
            profitThisMove += collectedAmount;
        }

        robot.setLocation(currentLoc);
        robot.recordProfit(profitThisMove);

        if (view != null) {
            view.updateRobot(robot);
            view.updateProfitBar(profit(), getMaxProfit());
            updateBlinkingRobot();
        }
    }
    
    /**
     * Identifica al robot con la mayor cantidad de tenges (si es mayor a cero) y lo
     * hace parpadear en la interfaz gráfica para destacarlo.
     */
    private void updateBlinkingRobot() {
        if (robots.isEmpty()) {
            return;
        }
        Robot richestRobot = robots.stream()
                                   .max(Comparator.comparingInt(Robot::getTenges))
                                   .orElse(null);

        // No hacer parpadear si la ganancia máxima es cero o negativa
        if (richestRobot != null && richestRobot.getTenges() <= 0) {
            richestRobot = null;
        }

        for (Robot r : robots) {
            r.setBlinking(r == richestRobot);
        }
    }
    
    /**
     * Reabastece todas las tiendas, restaurando sus tenges a su valor inicial.
     */
    public void resupplyStores() {
        for(Store s : stores) {
            s.resupply();
        }
    }

    /**
     * Devuelve todos los robots a sus posiciones iniciales y reinicia sus tenges a cero.
     */
    public void returnRobots() {
        for(Robot r : robots) {
            r.returnToInitial();
            if(view != null) view.updateRobot(r);
        }
        if(view != null) updateBlinkingRobot();
    }
    
    /**
     * Reinicia toda la simulación a su estado original.
     * Las tiendas se reabastecen y los robots vuelven a su punto de partida.
     */
    public void reboot() {
        for(Store s : stores) {
            s.reboot();
        }
        for(Robot r : robots) {
            r.reboot();
            if(view != null) view.updateRobot(r);
        }
        if(view != null) {
            updateBlinkingRobot();
            view.updateProfitBar(profit(), getMaxProfit());
        }
    }

    /**
     * Calcula la ganancia total actual, sumando los tenges de todos los robots.
     * @return La ganancia total acumulada.
     */
    public int profit() {
        return robots.stream().mapToInt(Robot::getTenges).sum();
    }

    /**
     * Devuelve el estado actual de todas las tiendas.
     * @return Una matriz 2D donde cada fila es [ubicación, tenges actuales].
     */
    public int[][] stores() {
        return stores.stream()
            .sorted(Comparator.comparingInt(Store::getLocation))
            .map(s -> new int[]{s.getLocation() + 1, s.getTenges()})
            .toArray(int[][]::new);
    }
    
    /**
     * Devuelve cuántas veces ha sido vaciada cada tienda.
     * @return Una matriz 2D donde cada fila es [ubicación, número de veces vaciada].
     */
    public int[][] emptiedStores() {
        return stores.stream()
            .sorted(Comparator.comparingInt(Store::getLocation))
            .map(s -> new int[]{s.getLocation() + 1, s.getTimesEmptied()})
            .toArray(int[][]::new);
    }
    
    /**
     * Devuelve el historial de ganancias por movimiento para cada robot.
     * @return Una matriz 2D donde cada fila representa un robot. El primer elemento es
     * la ubicación del robot, y los siguientes son las ganancias de cada movimiento.
     */
    public int[][] profitPerMove() {
        int maxMoves = robots.stream()
            .mapToInt(r -> r.getProfitHistory().size())
            .max().orElse(0);

        return robots.stream()
            .sorted(Comparator.comparingInt(Robot::getLocation))
            .map(r -> {
                int[] row = new int[1 + maxMoves];
                row[0] = r.getLocation() + 1;
                List<Integer> history = r.getProfitHistory();
                for (int i = 0; i < history.size(); i++) {
                    row[i + 1] = history.get(i);
                }
                return row;
            })
            .toArray(int[][]::new);
    }

    /**
     * Devuelve el estado actual de todos los robots.
     * @return Una matriz 2D donde cada fila es [ubicación, tenges actuales].
     */
    public int[][] robots() {
        return robots.stream()
            .sorted(Comparator.comparingInt(Robot::getLocation))
            .map(r -> new int[]{r.getLocation() + 1, r.getTenges()})
            .toArray(int[][]::new);
    }

    /**
     * Hace visible la interfaz gráfica de la simulación.
     */
    public void makeVisible() {
        if(view != null) {
            view.drawBoard();
            view.showProfitBar();
            for(Store s : stores) view.drawStore(s);
            for(Robot r : robots) view.drawRobot(r);
            updateBlinkingRobot();
        }
    }

    /**
     * Oculta los elementos de la interfaz gráfica.
     */
    public void makeInvisible() {
         if (view != null) {
            for(Robot r : robots) r.setBlinking(false);
            for(Store s : stores) view.eraseStore(s);
            for(Robot r : robots) view.eraseRobot(r);
         }
    }
    
    /**
     * Termina la simulación, cierra la ventana y libera los recursos.
     */
    public void finish() {
        makeInvisible();
        if (view != null && view.getFrame() != null) {
            view.getFrame().dispose();
        }
        robots.clear();
        stores.clear();
        storeLocations.clear();
    }
    
    /**
     * Verifica si la última operación realizada fue exitosa.
     * @return true si la última operación se completó correctamente, false en caso contrario.
     */
    public boolean ok() {
        return lastOk;
    }

    /**
     * Genera una matriz que representa un camino en espiral.
     * @param n La dimensión de la matriz (n x n).
     * @return La matriz 2D con los números ordenados en espiral.
     */
    private int[][] generateSpiral(int n) {
        int[][] spiral = new int[n][n];
        int top = 0, bottom = n - 1, left = 0, right = n - 1;
        int num = 0;
        while(top <= bottom && left <= right){
            for(int j = left; j <= right; j++) spiral[top][j] = num++;
            top++;
            for(int i = top; i <= bottom; i++) spiral[i][right] = num++;
            right--;
            if(top <= bottom){
                for(int j = right; j >= left; j--) spiral[bottom][j] = num++;
                bottom--;
            }
            if(left <= right){
                for(int i = bottom; i >= top; i--) spiral[i][left] = num++;
                left++;
            }
        }
        return spiral;
    }

    /**
     * Obtiene la instancia de la vista (GUI).
     * @return el objeto SilkRoadView.
     */
    private SilkRoadView getView() {
        return view;
    }

    /**
     * Calcula la máxima ganancia posible en la simulación, que es la suma
     * de los tenges iniciales de todas las tiendas.
     * @return La máxima ganancia teórica.
     */
    private int getMaxProfit() {
        return stores.stream().mapToInt(Store::getInitialTenges).sum();
    }
    
    /**
     * Obtiene la lista de todos los robots en la simulación.
     * @return Un ArrayList de objetos Robot.
     */
    public ArrayList<Robot> getRobots() {
        return this.robots;
    }
    
    /**
     * Obtiene la lista de todas las tiendas en la simulación.
     * @return Un ArrayList de objetos Store.
     */
    public ArrayList<Store> getStores() {
        return this.stores;
    }
    
    /**
     * Obtiene el número total de casillas en el tablero.
     * @return El tamaño total del tablero (size * size).
     */
    public int getBoardSize() {
        return this.size * this.size;
    }
    
    /**
     * Pausa la ejecución del hilo actual durante un tiempo determinado.
     * Útil para controlar la velocidad de la simulación visual.
     * @param milliseconds El tiempo a esperar en milisegundos.
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}