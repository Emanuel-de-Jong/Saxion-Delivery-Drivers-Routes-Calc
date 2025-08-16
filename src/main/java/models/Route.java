package models;

import models.graph.GraphNode;

import java.util.ArrayList;

/**
 * An ordered list of locations to go to.
 */
public class Route extends ArrayList<Location> {

    public Route() { }

    public Route(ArrayList<Location> locations) {
        super(locations);
        assert (locations != null) : "Locations shouldn't be null";
    }


    public void addGraphNodeLocations(ArrayList<GraphNode> nodes) {
        assert (nodes != null) : "Nodes shouldn't be null";

        for (GraphNode node : nodes) {
            add((Location) node);
        }
    }


    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < size(); i++) {
            Location node = get(i);
            result += String.format(
                    "%s) %s - %s\n",
                    i + 1,
                    node.x,
                    node.y);
        }
        return result;
    }

    @Override
    public Route clone() {
        return new Route(this);
    }

}
