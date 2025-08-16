package models;

import java.util.Objects;

public class Client {

    private final int id;
    private final String name;
    private final String initials;


    public Client(int id, String name, String initials) {
        assert (id >= 0) : "Id shouldn't be negative";
        assert (name != null) : "Name shouldn't be null";
        assert (initials != null) : "Initials shouldn't be null";

        this.id = id;
        this.name = name;
        this.initials = initials;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                name.equals(client.name) &&
                initials.equals(client.initials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, initials);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", initials='" + initials + '\'' +
                '}';
    }

}
