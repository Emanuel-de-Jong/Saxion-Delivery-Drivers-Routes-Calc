package models.graph;

import models.Location;

import java.util.Objects;

/**
 * Represents a node in a Graph.
 * Extends Location for easy access to location data and functions.
 */
public class GraphNode extends Location {

    /**
     * X and y merged as strings for identification.
     */
    public final int id;


    public GraphNode(int x, int y) {
        super(x, y);
        id = Integer.parseInt(x + "" + y);
    }

    public GraphNode(Location location) {
        this(location.x, location.y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode node = (GraphNode) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                "location=" + super.toString() +
                '}';
    }

}