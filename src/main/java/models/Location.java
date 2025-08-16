package models;

public class Location {

    public final int x;
    public final int y;


    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getDeltaX(Location location) {
        assert (location != null) : "Location shouldn't be null";

        return location.x - x;
    }

    public int getDeltaY(Location location) {
        assert (location != null) : "Location shouldn't be null";

        return location.y - y;
    }


    public int getDrivingDistance(Location location) {
        assert (location != null) : "Location shouldn't be null";

        return Math.abs(getDeltaY(location)) + Math.abs(getDeltaX(location));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return location.x == x && location.y == y;
    }

    @Override
    public String toString() {
        return "Location{" +
                "X=" + x +
                ", Y=" + y +
                '}';
    }

}
