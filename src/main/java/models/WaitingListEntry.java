package models;

import java.util.Objects;

public class WaitingListEntry {

    private final int id;
    private Client client;
    /**
     * Entries with higher urgency get chosen faster.
     */
    private int urgency;
    private int rooms;
    private boolean garden;


    public WaitingListEntry(int id) {
        assert (id >= 0) : "Id shouldn't be negative";

        this.id = id;
    }

    public WaitingListEntry(int id, Client client, int urgency, int rooms, boolean garden) {
        this(id);
        assert (client != null) : "Client shouldn't be null";
        assert (urgency >= 0) : "Urgency shouldn't be negative";
        assert (rooms >= 1) : "Rooms shouldn't be less than 1";

        this.client = client;
        this.urgency = urgency;
        this.rooms = rooms;
        this.garden = garden;
    }


    public int getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitingListEntry that = (WaitingListEntry) o;
        return id == that.id &&
                urgency == that.urgency &&
                rooms == that.rooms &&
                garden == that.garden &&
                client.equals(that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, urgency, rooms, garden);
    }

    @Override
    public String toString() {
        return "WaitingListEntry{\n" +
                "\tid=" + id + ",\n" +
                "\tclient=" + client + ",\n" +
                "\turgency=" + urgency + ",\n" +
                "\trooms=" + rooms + ",\n" +
                "\tgarden=" + garden + "\n" +
                '}';
    }

}
