package models;

import java.util.Objects;

public class Home {

    private final int id;
    private final Location location;
    private final int rent;
    private final int rooms;
    private final boolean garden;


    public Home(int id, Location location, int rent, int rooms, boolean garden) {
        assert (id >= 0) : "Id shouldn't be negative";
        assert (location != null) : "Location shouldn't be null";
        assert (rent >= 0) : "Rent shouldn't be negative";
        assert (rooms >= 1) : "Rooms shouldn't be less than 1";

        this.id = id;
        this.location = location;
        this.rent = rent;
        this.rooms = rooms;
        this.garden = garden;
    }


    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Home home = (Home) o;
        return id == home.id &&
                rent == home.rent &&
                rooms == home.rooms &&
                garden == home.garden &&
                location.equals(home.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, rent, rooms, garden);
    }

    @Override
    public String toString() {
        return "Home{" + '\n' +
                "\tid=" + id + ",\n" +
                "\tlocation=" + location + ",\n" +
                "\trent=" + rent + ",\n" +
                "\trooms=" + rooms + ",\n" +
                "\tgarden=" + garden + "\n" +
                '}';
    }

}
