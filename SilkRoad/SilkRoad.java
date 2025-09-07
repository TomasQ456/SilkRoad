import java.util.*;

/**
 * SilkRoad.java
 * Versión que usa Store y Robot como clases externas y
 * almacena las referencias iniciales en startStores / startRobots.
 *
 * Mantiene la API: placeStore, removeStore, placeRobot, removeRobot, moveRobot, resupplyStores,
 * returnRobots, reboot, porfit, stores, robots, makeVisible, makeInvisible, finish, ok.
 */
public class SilkRoad {
    private final int _length;
    private final TreeMap<Integer, Store> stores = new TreeMap<>();
    private final TreeMap<Integer, Deque<Robot>> robotsAtLocation = new TreeMap<>();
    private final List<Store> startStores = new ArrayList<>();
    private final List<Robot> startRobots = new ArrayList<>();

    private int profit = 0;
    private boolean visible = false;
    private boolean ok = true;
    private boolean finished = false;

    public SilkRoad(int length) {
        if (length <= 0) throw new IllegalArgumentException("length must be > 0");
        this._length = length;
    }

    private boolean invalidStatePrevented() {
        if (finished) {
            ok = false;
            return true;
        }
        return false;
    }


    public void placeStore(int location, int tengas) {
        if (invalidStatePrevented()) return;
        ok = false;
        if (location < 0 || location > _length || tengas < 0) return;
        if (stores.containsKey(location)) return; 
        Store s = new Store(location, tengas);
        stores.put(location, s);
        boolean alreadyStart = startStores.stream().anyMatch(ss -> ss.getLocation() == location);
        if (!alreadyStart) startStores.add(s);

        ok = true;
    }

    public void removeStore(int location) {
        if (invalidStatePrevented()) return;
        ok = false;
        if (!stores.containsKey(location)) return;
        Store removed = stores.remove(location);
        for (Iterator<Store> it = startStores.iterator(); it.hasNext();) {
            if (it.next().getLocation() == location) { it.remove(); break; }
        }

        ok = true;
    }

    public void placeRobot(int location) {
        if (invalidStatePrevented()) return;
        ok = false;
        if (location < 0 || location > _length) return;
        boolean startHas = startRobots.stream().anyMatch(r -> r.getStartLocation() == location);
        boolean currentHas = robotsAtLocation.containsKey(location) && !robotsAtLocation.get(location).isEmpty();
        if (startHas || currentHas) return;

        Robot r = new Robot(location);
        robotsAtLocation.computeIfAbsent(location, k -> new ArrayDeque<>()).addLast(r);
        startRobots.add(r);

        ok = true;
    }

    public void removeRobot(int location) {
        if (invalidStatePrevented()) return;
        ok = false;
        Deque<Robot> dq = robotsAtLocation.get(location);
        if (dq == null || dq.isEmpty()) return;
        Robot removed = dq.removeFirst();
        if (dq.isEmpty()) robotsAtLocation.remove(location);


        for (Iterator<Robot> it = startRobots.iterator(); it.hasNext();) {
            Robot sr = it.next();
            if (sr.getStartLocation() == removed.getStartLocation()) { it.remove(); break; }
        }
        ok = true;
    }

    public void moveRobot(int location, int meters) {
        if (invalidStatePrevented()) return;
        ok = false;
        Deque<Robot> dq = robotsAtLocation.get(location);
        if (dq == null || dq.isEmpty()) return;

        Robot r = dq.removeFirst();
        if (dq.isEmpty()) robotsAtLocation.remove(location);

        int newLoc = r.getCurrentLocation() + meters;
        if (newLoc < 0) newLoc = 0;
        if (newLoc > _length) newLoc = _length;
        r.setCurrentLocation(newLoc);

        Store s = stores.get(newLoc);
        if (s != null && s.getCurrentTenges() > 0) {
            int collected = s.getCurrentTenges();
            s.setCurrentTenges(0);
            r.addTenges(collected);
            profit += collected - Math.abs(meters);
        } else {
            profit -= Math.abs(meters);
        }

        robotsAtLocation.computeIfAbsent(newLoc, k -> new ArrayDeque<>()).addLast(r);
        ok = true;
    }

    public void resupplyStores() {
        if (invalidStatePrevented()) return;
        ok = false;
        for (Store s : stores.values()) {
            s.resupply();
        }
        ok = true;
    }

    public void returnRobots() {
        if (invalidStatePrevented()) return;
        ok = false;

        robotsAtLocation.clear();
        for (Robot sr : startRobots) {
            sr.resetToStart();
            robotsAtLocation.computeIfAbsent(sr.getStartLocation(), k -> new ArrayDeque<>()).addLast(sr);
        }
        ok = true;
    }


    public void reboot() {
        if (invalidStatePrevented()) return;
        ok = false;

        stores.clear();
        for (Store s : startStores) {
            s.resetToInitial();
            stores.put(s.getLocation(), s);
        }

        robotsAtLocation.clear();
        for (Robot r : startRobots) {
            r.resetToStart();
            robotsAtLocation.computeIfAbsent(r.getStartLocation(), k -> new ArrayDeque<>()).addLast(r);
        }

        profit = 0;
        ok = true;
    }


    public int porfit() {
        return profit;
    }


    public int[][] stores() {
        int n = stores.size();
        int[][] out = new int[n][2];
        int i = 0;
        for (Map.Entry<Integer, Store> e : stores.entrySet()) {
            out[i][0] = e.getKey();
            out[i][1] = e.getValue().getCurrentTenges();
            i++;
        }
        return out;
    }


    public int[][] robots() {
        int total = robotsAtLocation.values().stream().mapToInt(Deque::size).sum();
        int[][] out = new int[total][2];
        int idx = 0;
        for (Map.Entry<Integer, Deque<Robot>> e : robotsAtLocation.entrySet()) {
            int loc = e.getKey();
            for (Robot r : e.getValue()) {
                out[idx][0] = loc;
                out[idx][1] = r.getTenges();
                idx++;
            }
        }
        return out;
    }

    public void makeVisible() {
        visible = true;
        ok = true;
    }
    public void makeInvisible() {
        visible = false;
        ok = true;
    }

    public void finish() {
        finished = true;
        ok = true;
    }

    public boolean ok() {
        return ok;
    }
}

