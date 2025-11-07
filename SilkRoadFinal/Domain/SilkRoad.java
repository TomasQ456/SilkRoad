package Domain;
import Shapes.*;
import Presentation.*;

import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class SilkRoad {
    private int size;
    private int[][] spiral;
    private ArrayList<Store> stores;
    private ArrayList<Robot> robots;
    private SilkRoadView view;
    private boolean lastOk = true;
    private Map<Integer, Store> storeLocations;

    private void init(int size) {
        this.size = size;
        this.spiral = generateSpiral(size);
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.storeLocations = new HashMap<>();
        if (System.getProperty("java.awt.headless") == null) {
            this.view = new SilkRoadView(this, this.size, this.spiral);
        } else {
            this.view = null;
        }
    }

    public SilkRoad(int size) {
        init(size);
    }

    /**
     * Constructor que acepta entrada de maratón.
     * Formato: {tipo, ubicación, [tenges para tiendas]}
     * Tipo 1 = Robot, Tipo 2 = Tienda
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
            if (type == 1) {
                placeRobot(itemData[1]);
            } else if (type == 2) {
                if (itemData.length > 2) {
                    placeStore(itemData[1], itemData[2]);
                }
            }
        }
        
        if (this.view != null) {
            makeVisible();
        }
    }

    public void placeStore(int location, int tenges, String type) {
            // Por defecto fallará hasta que confirmemos lo contrario
            lastOk = false;
            // Validaciones básicas de entrada
            if (location < 1) return;
            int requestedLoc = location - 1;
            if (requestedLoc >= size * size) return;
            if (tenges < 0) return;
            
            // Crear la instancia correspondiente de Store
            Store s;
            if ("autonomous".equalsIgnoreCase(type)) {
                // El predicado de availability no usa variables locales mutadas,
                // por eso no hay problema de "effectively final".
                s = new AutonomousStore(requestedLoc, tenges, size * size,
                    loc -> !storeLocations.containsKey(loc)
                           && robots.stream().noneMatch(r -> java.util.Objects.equals(r.getLocation(), loc))
                );
            } else if ("fighter".equalsIgnoreCase(type)) {
                s = new FighterStore(requestedLoc, tenges);
            } else if ("casino".equalsIgnoreCase(type)) {
                s = new CasinoStore(requestedLoc, tenges);
            } else {
                // Default a NormalStore si type es null o no reconocido
                s = new NormalStore(requestedLoc, tenges);
            }
        
            // Obtener la ubicación real que tiene la tienda (AutonomousStore puede cambiarla)
            int actualLoc = s.getLocation();
            
            // Comprobar ocupación: usamos Objects.equals para comparar de forma segura
            // si r.getLocation() devuelve Integer o int (autoboxing).
            boolean isOccupied = storeLocations.containsKey(actualLoc)
                                ||
                                 robots.stream().anyMatch(r -> java.util.Objects.equals(r.getLocation(), actualLoc));
        
            if (isOccupied) {
                lastOk = false;
                return;
            }
        
            storeLocations.put(actualLoc, s);
            this.stores.add(s);
            for(Store a : stores) view.drawStore(s);
    
            lastOk = true;
        }

    /**
     * Coloca una tienda normal (por defecto).
     */
    public void placeStore(int location, int tenges) {
        placeStore(location, tenges, "normal");
    }

    /**
     * Coloca un robot del tipo especificado.
     * @param location Ubicación (1-indexed)
     * @param type Tipo: "normal", "neverback", "tender", "greedy"
     */
    public void placeRobot(int location, String type) {
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
        
        Robot r;
        if (type.equalsIgnoreCase("neverback")) {
            r = new NeverBackRobot(internalLocation);
        } else if (type.equalsIgnoreCase("tender")) {
            r = new TenderRobot(internalLocation);
        } else if (type.equalsIgnoreCase("greedy")) {
            r = new GreedyRobot(internalLocation);
        } else {
            r = new NormalRobot(internalLocation);
        }
        
        robots.add(r);
        if (view != null) {
            view.drawRobot(r);
            updateBlinkingRobot();
        }
        view.updateProfitBar(profit(), getMaxProfit());
    }

    /**
     * Coloca un robot normal (por defecto).
     */
    public void placeRobot(int location) {
        placeRobot(location, "normal");
    }
    
    public void removeStore(int location) {
        lastOk = false;
        location -= 1;
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
            JOptionPane.showMessageDialog(null, "Error: No store found at location " + (location + 1) + " to remove.");
        }
        view.updateProfitBar(profit(), getMaxProfit());
    }
    
    public void removeRobot(int location) {
        lastOk = false;
        location -= 1;
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
            JOptionPane.showMessageDialog(null, "Error: No robot found at location " + (location + 1) + " to remove.");
        }
    }
    
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
                    int distFwd = (storeLocation - robotLocation + totalSquares) % totalSquares;
                    int distBwd = (robotLocation - storeLocation + totalSquares) % totalSquares;
                    
                    // Considerar si el robot puede retroceder
                    int shortestDist;
                    int direction;
                    if (robot.canMoveBackward()) {
                        shortestDist = Math.min(distFwd, distBwd);
                        direction = (distFwd <= distBwd) ? 1 : -1;
                    } else {
                        // Solo puede ir hacia adelante
                        shortestDist = distFwd;
                        direction = 1;
                    }
                    
                    if (shortestDist == 0) continue;
                    
                    // Calcular costo usando el método del robot
                    int movementCost = robot.getMovementCost(shortestDist);
                    // Calcular ganancia potencial usando el método del robot
                    int potentialCollection = robot.collectFromStore(store.getTenges());
                    int potentialProfit = potentialCollection - movementCost;
                    
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
        view.updateProfitBar(profit(), getMaxProfit());
    }

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
        
        // Verificar si es NeverBack y está tratando de retroceder
        if (!robot.canMoveBackward() && steps < 0) {
            System.out.println("NeverBackRobot en " + location + " no puede retroceder.");
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
        
        // Usar el costo de movimiento específico del robot
        int movementCost = robot.getMovementCost(distanceTraveled);
        int profitThisMove = -movementCost;
        robot.addTenges(-movementCost);
        
        if (targetStore != null) {
            // La tienda decide cuánto dar usando empty()
            int collectedAmount = targetStore.empty(robot);
            robot.addTenges(collectedAmount);
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

    private void updateBlinkingRobot() {
        if (robots.isEmpty()) {
            return;
        }
        Robot richestRobot = robots.stream()
                                   .max(Comparator.comparingInt(Robot::getTenges))
                                   .orElse(null);
        if (richestRobot != null && richestRobot.getTenges() <= 0) {
            richestRobot = null;
        }
        for (Robot r : robots) {
            r.setBlinking(r == richestRobot);
        }
    }
    
    public void resupplyStores() {
        for(Store s : stores) { 
            s.resupply(); 
        }
        view.updateProfitBar(profit(), getMaxProfit());
    }

    public void returnRobots() {
        for(Robot r : robots) { 
            r.returnToInitial(); 
            if(view != null) view.updateRobot(r); 
        }
        if(view != null) updateBlinkingRobot();
    }

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

    public int profit() {
        return robots.stream().mapToInt(Robot::getTenges).sum();
    }

    public int[][] stores() {
        return stores.stream()
            .sorted(Comparator.comparingInt(Store::getLocation))
            .map(s -> new int[]{s.getLocation() + 1, s.getTenges()})
            .toArray(int[][]::new);
    }
    
    public int[][] emptiedStores() {
        return stores.stream()
            .sorted(Comparator.comparingInt(Store::getLocation))
            .map(s -> new int[]{s.getLocation() + 1, s.getTimesEmptied()})
            .toArray(int[][]::new);
    }
    
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

    public int[][] robots() {
        return robots.stream()
            .sorted(Comparator.comparingInt(Robot::getLocation))
            .map(r -> new int[]{r.getLocation() + 1, r.getTenges()})
            .toArray(int[][]::new);
    }

    public void makeVisible() {
        if(view != null) {
            view.drawBoard();
            view.showProfitBar();
            for(Store s : stores) view.drawStore(s);
            for(Robot r : robots) view.drawRobot(r);
            updateBlinkingRobot();
        }
    }

    public void makeInvisible() {
         if (view != null) {
            for(Robot r : robots) r.setBlinking(false);
            for(Store s : stores) view.eraseStore(s);
            for(Robot r : robots) view.eraseRobot(r);
        }
    }

    public void finish() {
        makeInvisible();
        if (view != null && view.getFrame() != null) {
            view.getFrame().dispose();
        }
        robots.clear();
        stores.clear();
        storeLocations.clear();
    }

    public boolean ok() { 
        return lastOk; 
    }

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

    private SilkRoadView getView() {
        return view;
    }

    private int getMaxProfit() {
        return stores.stream().mapToInt(Store::getInitialTenges).sum();
    }

    public ArrayList<Robot> getRobots() {
        return this.robots;
    }
    
    public ArrayList<Store> getStores() {
        return this.stores;
    }
    
    public int getBoardSize() {
        return this.size * this.size;
    }
    
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
