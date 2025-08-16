package models.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph extends ArrayList<GraphNode> {

    /**
     * Start node. Should always be the first element in a graph.
     * Represents the HQ in our program.
     */
    public static final GraphNode HOMENODE = new GraphNode(500, 500);

    private final HashMap<String, GraphEdge> edges = new HashMap<>();


    public Graph() {
        add(HOMENODE);
    }

    public Graph(Graph g) {
        super(g);
    }


    public HashMap<String, GraphEdge> getEdges() {
        return edges;
    }

    private void addEdge(GraphNode node1, GraphNode node2, int distance) {
        assert (node1 != null) : "Node1 shouldn't be null";
        assert (node2 != null) : "Node2 shouldn't be null";
        assert (distance >= 1) : "Distance shouldn't be less than 1";

        GraphEdge edge = new GraphEdge(node1, node2, distance);
        edges.putIfAbsent(edge.id, edge);
    }

    public HashMap<GraphNode, ArrayList<GraphNode>> getNodeConnections() {
        return getNodeConnections(new ArrayList<>(edges.values()));
    }

    public static HashMap<GraphNode, ArrayList<GraphNode>> getNodeConnections(ArrayList<GraphEdge> edges) {
        HashMap<GraphNode, ArrayList<GraphNode>> nodes = new HashMap<>();

        for (GraphEdge edge : edges) {
            nodes.putIfAbsent(edge.node1, new ArrayList<>());
            nodes.putIfAbsent(edge.node2, new ArrayList<>());
            nodes.get(edge.node1).add(edge.node2);
            nodes.get(edge.node2).add(edge.node1);
        }

        return nodes;
    }


    /**
     * Generates edges for the graph if there are none yet.
     * Each node checks for the closest node left, right, top and bottom from it, creating a web.
     */
    public void createEdges() {
        if (edges.size() != 0) return;

        for (GraphNode node : this) {
            GraphNode left = null;
            GraphNode right = null;
            GraphNode top = null;
            GraphNode bottom = null;

            // We want the lowest distance, so the start value should be the highest.
            int leftDistance = Integer.MAX_VALUE;
            int rightDistance = Integer.MAX_VALUE;
            int topDistance = Integer.MAX_VALUE;
            int bottomDistance = Integer.MAX_VALUE;

            for (GraphNode edgeNode : this) {
                // Ignore self.
                if (edgeNode.equals(node)) continue;

                int deltaX = node.getDeltaX(edgeNode);
                int deltaY = node.getDeltaY(edgeNode);
                int distance = node.getDrivingDistance(edgeNode);
                // Decide if the node should compete for horizontal (left or right) or vertical (up or down)
                // depending on which absolute axis distance is the shortest.
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX < 0) {
                        if (distance < leftDistance) {
                            leftDistance = distance;
                            left = edgeNode;
                        }
                    } else {
                        if (distance < rightDistance) {
                            rightDistance = distance;
                            right = edgeNode;
                        }
                    }
                } else {
                    if (deltaY < 0) {
                        if (distance < topDistance) {
                            topDistance = distance;
                            top = edgeNode;
                        }
                    } else {
                        if (distance < bottomDistance) {
                            bottomDistance = distance;
                            bottom = edgeNode;
                        }
                    }
                }
            }

            if (left != null) addEdge(node, left, leftDistance);
            if (right != null) addEdge(node, right, rightDistance);
            if (top != null) addEdge(node, top, topDistance);
            if (bottom != null) addEdge(node, bottom, bottomDistance);
        }
    }

    /**
     * Sum of distances from each node to the next in the graph list.
     * Includes going back to the home node at the end.
     */
    public int calcTotalDistance() {
        int distance = 0;

        assert (this.get(0) != null) : "Graph doesn't have home node";
        distance += HOMENODE.getDrivingDistance(this.get(0));
        for (int i = 1; i < this.size(); i++) {
            distance += this.get(i - 1).getDrivingDistance(this.get(i));
        }
        distance += HOMENODE.getDrivingDistance(this.get(this.size() - 1));

        return distance;
    }


    @Override
    public Graph clone() {
        return new Graph(this);
    }

}
