import java.awt.Point;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        SilkRoad sr = new SilkRoad(30);
        sr.placeStore(2, 10);
        sr.placeStore(7, 0);
        sr.placeRobot(0);
        sr.placeRobot(15);
        SilkRoadView roadView = new SilkRoadView(30, 800, 20);
        roadView.drawRoad();
        Map<Integer, Point> posMap = roadView.getPosMap();
        for (int[] s : sr.stores()) {
            int loc = s[0];
            int tenges = s[1];
            Point cell = posMap.get(loc);
            new StoreView(cell, roadView.getCellSize(), 20, tenges);
        }
        for (int[] r : sr.robots()) {
            int loc = r[0];
            int tenges = r[1];
            Point cell = posMap.get(loc);
            new RobotView(cell, roadView.getCellSize(), 20, tenges);
        }
    }
}

