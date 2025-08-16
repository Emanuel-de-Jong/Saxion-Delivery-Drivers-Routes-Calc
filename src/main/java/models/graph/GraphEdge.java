package models.graph;

/**
 * Represents a line between 2 GraphNodes.
 */
public class GraphEdge {

    /**
     * Node ids merged as strings for identification.
     */
    public final String id;
    public final GraphNode node1;
    public final GraphNode node2;
    public final int distance;


    public GraphEdge(GraphNode node1, GraphNode node2, int distance) {
        assert (node1 != null) : "Node1 shouldn't be null";
        assert (node2 != null) : "Node2 shouldn't be null";
        assert (distance >= 1) : "Distance shouldn't be less than 1";

        this.distance = distance;

        // The id should be the same regardless of the order the nodes were given.
        // This is done by always making node1 the node with the smallest id.
        if (node1.id < node2.id) {
            this.node1 = node1;
            this.node2 = node2;
        } else {
            this.node1 = node2;
            this.node2 = node1;
        }

        id = createId(node1, node2);
    }


    /**
     * Returns the 2 nodes in an array.
     * To simplify iteration.
     */
    public GraphNode[] getNodes() {
        return new GraphNode[]{node1, node2};
    }


    /**
     * Creates a unique id by merging the node ids as strings.
     */
    public static String createId(GraphNode node1, GraphNode node2) {
        assert (node1 != null) : "Node1 shouldn't be null";
        assert (node2 != null) : "Node2 shouldn't be null";

        // The id should be the same regardless of the order the nodes were given.
        // This is done by always making the first node the node with the smallest id.
        if (node1.id < node2.id) {
            return node1.id + ":" + node2.id;
        } else {
            return node2.id + ":" + node1.id;
        }
    }

}
