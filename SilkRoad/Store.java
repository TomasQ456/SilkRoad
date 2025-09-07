public class Store {
    private final int location;
    private final int initialTenges;
    private int currentTenges;

    public Store(int location, int initialTenges) {
        this.location = location;
        this.initialTenges = initialTenges;
        this.currentTenges = initialTenges;
    }

    public int getLocation() {
        return location;
    }

    public int getInitialTenges() {
        return initialTenges;
    }

    public int getCurrentTenges() {
        return currentTenges;
    }

    public void setCurrentTenges(int amount) {
        if (amount < 0) amount = 0;
        this.currentTenges = amount;
    }

    public boolean isEmpty() {
        return currentTenges <= 0;
    }

    public void resupply() {
        this.currentTenges = this.initialTenges;
    }


    public void resetToInitial() {
        this.currentTenges = this.initialTenges;
    }

    @Override
    public String toString() {
        return "Store{loc=" + location + ", current=" + currentTenges + ", initial=" + initialTenges + "}";
    }
}

