/**
 * Robot.java
 * Clase externa que representa un robot.
 * Ya conoce su posición inicial (startLocation) y puede resetearse sin snapshot externo.
 */
public class Robot {
    private final int startLocation;
    private int currentLocation;
    private int tenges;

    public Robot(int startLocation) {
        this.startLocation = startLocation;
        this.currentLocation = startLocation;
        this.tenges = 0;
    }

    public int getStartLocation() {
        return startLocation;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(int loc) {
        this.currentLocation = loc;
    }

    public int getTenges() {
        return tenges;
    }

    public void addTenges(int amount) {
        if (amount > 0) this.tenges += amount;
    }

    /**
     * Resetea el robot a su posición inicial y limpia lo recolectado.
     * (útil para reboot() / returnRobots())
     */
    public void resetToStart() {
        this.currentLocation = this.startLocation;
        this.tenges = 0;
    }

    @Override
    public String toString() {
        return "Robot{start=" + startLocation + ", loc=" + currentLocation + ", tenges=" + tenges + "}";
    }
}

