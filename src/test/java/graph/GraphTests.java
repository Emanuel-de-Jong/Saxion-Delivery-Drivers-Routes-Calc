package graph;

import models.graph.Graph;
import models.graph.GraphNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTests {

    private Graph graph;


    private void fill(int amountOfNodes) {
        Random rnd = new Random();

        // -1 because graphs already have a home node as the first node.
        for (int i = 0; i < amountOfNodes - 1; i++) {
            graph.add(new GraphNode(rnd.nextInt(1000), rnd.nextInt(1000)));
        }

        assertEquals(amountOfNodes, graph.size());
    }


    @BeforeEach
    public void setup() {
        graph = new Graph();
    }


    @RepeatedTest(5)
    @DisplayName("Graph edge creation doesn't have unconnected forests")
    public void createEdgesNoGaps() {
        fill(20);
        graph.createEdges();

        // To save the nodes that were connected to the home node.
        HashSet<GraphNode> linkedNodes = new HashSet<>();
        HashMap<GraphNode, ArrayList<GraphNode>> connections = graph.getNodeConnections();
        // Go over all connections starting from the home node.
        createEdgesNoGapsLoop(graph.get(0), connections, linkedNodes);

        // If the amount of nodes in the graph equals the amount of nodes connected to the home node,
        // all nodes must be connected to each other.
        assertEquals(graph.size(), linkedNodes.size());
    }

    /**
     * Recursive code of the createEdgesNoGaps test.
     */
    private void createEdgesNoGapsLoop(GraphNode node, HashMap<GraphNode, ArrayList<GraphNode>> connections,
                                       HashSet<GraphNode> linkedNodes) {
        linkedNodes.add(node);

        for (GraphNode connectedNode : connections.get(node)) {
            if (linkedNodes.contains(connectedNode)) continue;
            createEdgesNoGapsLoop(connectedNode, connections, linkedNodes);
        }
    }


    @RepeatedTest(3)
    @DisplayName("Create edges for 100.000 nodes")
    public void createEdges100_000Nodes() {
        fill(100_000);
    }

    @RepeatedTest(3)
    @DisplayName("Create edges for 1.000.000 nodes")
    public void createEdges1_000_000Nodes() {
        fill(1_000_000);
    }

    @RepeatedTest(3)
    @DisplayName("Create edges for 10.000.000 nodes")
    public void createEdges10_000_000Nodes() {
        fill(10_000_000);
    }

}
