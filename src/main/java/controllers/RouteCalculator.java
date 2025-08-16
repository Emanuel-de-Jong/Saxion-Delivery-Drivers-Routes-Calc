package controllers;

import models.Complaint;
import models.DisjointSet;
import models.Route;
import models.Schedule;
import models.graph.Graph;
import models.graph.GraphEdge;
import models.graph.GraphNode;
import models.tree.SimpleTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Algorithms to calculate the best route for a schedule.
 */
public class RouteCalculator {

    private static Graph scheduleToGraph(Schedule schedule) {
        assert (schedule != null) : "Schedule shouldn't be null";

        Graph graph = new Graph();
        for (Complaint complaint : schedule) {
            GraphNode node = new GraphNode(complaint.getHome().getLocation());
            if (!graph.contains(node)) graph.add(node);
        }
        return graph;
    }


    /**
     * Converts the output from the kruskal algorithm to a SimpleTree object.
     *
     * @see #calcRouteKruskal
     */
    private static SimpleTree<GraphNode> edgeListToSimpleTree(ArrayList<GraphEdge> edges) {
        assert (edges != null) : "Edges shouldn't be null";

        // In edgeListToSimpleTreeLoop(), we recursively go over all nodes connected to a node.
        // So we need to convert the edges parameter into the following data type:
        HashMap<GraphNode, ArrayList<GraphNode>> nodes = Graph.getNodeConnections(edges);

        GraphNode home = null;
        // Find the home node.
        for (GraphNode node : nodes.keySet()) {
            if (node.equals(Graph.HOMENODE)) {
                home = node;
                break;
            }
        }

        if (home == null) {
            return null;
        }

        SimpleTree<GraphNode> root = edgeListToSimpleTreeLoop(null, home, nodes, new ArrayList<>());

        assert (root.getData().equals(Graph.HOMENODE)) : "Root tree data isn't the home node";
        return root;
    }

    /**
     * Recursive code of the edgeListToSimpleTree function.
     *
     * @param parentTree The future parent tree node of the "node" param. Should be null in calls as root has no parents.
     * @param node       The graph node that will be made a tree node and added to the tree.
     * @param nodes      All nodes to be added.
     * @param added      A list of graph nodes that were added already. Should be empty in call.
     * @see #edgeListToSimpleTree
     */
    private static SimpleTree edgeListToSimpleTreeLoop(SimpleTree<GraphNode> parentTree, GraphNode node, HashMap<GraphNode,
            ArrayList<GraphNode>> nodes, ArrayList<GraphNode> added) {
        assert (node != null) : "Node shouldn't be null";
        assert (nodes != null) : "Nodes shouldn't be null";
        assert (added != null) : "Added shouldn't be null";

        if (added.contains(node)) return null;
        added.add(node);

        // Makes a new tree with the node as data and inserts it into the parent tree.
        // On the first iteration it only does the first part, since root trees don't have a parent tree.
        SimpleTree newTree = parentTree != null ? parentTree.insert(node) : new SimpleTree(node);

        for (GraphNode n : nodes.get(node)) {
            edgeListToSimpleTreeLoop(newTree, n, nodes, added);
        }

        return newTree;
    }


    /**
     * Makes a list from the nodes in the given SimpleTree.
     * Ordered as a good route to travel to all nodes in a short time.
     */
    private static Route calcRouteFromSimpleTree(SimpleTree<GraphNode> tree) {
        assert (tree != null) : "Tree shouldn't be null";

        Route route = new Route();
        calcRouteFromSimpleTreeLoop(tree, route);
        route.add(tree.getData());

        return route;
    }

    /**
     * Recursive code of the calcRouteFromSimpleTree function.
     * The tree is walked through depth first.
     * Child nodes are walked through in order of the shortest distance from the previous child node.
     *
     * @see #calcRouteFromSimpleTree
     */
    private static void calcRouteFromSimpleTreeLoop(SimpleTree<GraphNode> tree, Route route) {
        assert (tree != null) : "Tree shouldn't be null";
        assert (route != null) : "Route shouldn't be null";

        // Children are cloned because the next loop removes them for convenience.
        ArrayList<SimpleTree> children = (ArrayList<SimpleTree>) tree.getChildren().clone();
        ArrayList<SimpleTree<GraphNode>> sortedChildren = new ArrayList<>();

        // Represents the child that was last added to sortedChildren.
        // Starts with tree so the first child that will be added is the one closest to the parent tree.
        // It's reassigned to that child before it's added to sortedChildren. So tree won't be put in sortedChildren.
        SimpleTree<GraphNode> lastSortedChild = tree;
        // Children are added to sortedChildren in order of their distance to the latest addition.
        // This results in a small total distance in most cases.
        // It's O(n^2) so it might get slow if there are a lot of children.
        while (children.size() > 0) {
            int lowestDistance = Integer.MAX_VALUE;
            SimpleTree lowestChild = null;
            for (SimpleTree<GraphNode> child : children) {
                int distance = lastSortedChild.getData().getDrivingDistance(child.getData());

                if (distance < lowestDistance) {
                    lowestDistance = distance;
                    lowestChild = child;
                }
            }

            lastSortedChild = lowestChild;
            sortedChildren.add(lowestChild);
            children.remove(lowestChild);
        }

        route.add(tree.getData());
        for (SimpleTree child : sortedChildren) {
            calcRouteFromSimpleTreeLoop(child, route);
        }
    }


    /**
     * Makes a route to follow from the locations in the given schedule with the Kruskal algorithm.
     * Might not be the optimal route, but is fast.
     * The following steps are taken:
     * 1. Make a graph from the schedule.
     * 2. Generate edges in the graph.
     * 3. Use kruskal to get a list representing a MST.
     * 4. Make a SimpleTree from that list.
     * 5. Walk through the tree and save the order as a Route.
     *
     * @return The filled Route. Can be null.
     */
    public static Route getRouteKruskal(Schedule schedule) {
        assert (schedule != null) : "Schedule shouldn't be null";

        ArrayList<GraphEdge> edges = calcRouteKruskal(scheduleToGraph(schedule));
        SimpleTree<GraphNode> tree = edgeListToSimpleTree(edges);
        if (tree == null) return null;

        Route route = new Route(calcRouteFromSimpleTree(tree));
        assert (route.get(0).equals(Graph.HOMENODE)) : "Route doesn't start at the home node";
        assert (route.get(route.size() - 1).equals(Graph.HOMENODE)) : "Route doesn't end at the home node";

//        Visualizer.drawEdges(edges);
//        Visualizer.drawSimpleTree(tree);

        schedule.setRoute(route);
        return route;
    }

    /**
     * Makes an edge list from the given graph with the Kruskal algorithm.
     * Edges are generated for the graph if there are non yet.
     */
    private static ArrayList<GraphEdge> calcRouteKruskal(Graph graph) {
        assert (graph != null) : "Graph shouldn't be null";

        // Generate edges since a graph made from a schedule doesn't have edges yet.
        graph.createEdges();

        // Sort edges by smallest to biggest distance.
        PriorityQueue<GraphEdge> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.distance));
        edgeQueue.addAll(graph.getEdges().values());

        // Classic Kruskals algorithm implementation.
        // 1. Make a set for each node.
        UnionFind unionFind = new UnionFind(graph);
        ArrayList<GraphEdge> result = new ArrayList<>(graph.size());
        // 2. Loop through the edges from shortest to longest.
        for (GraphEdge edge : edgeQueue) {
            // 3. Get the sets of the edge nodes.
            DisjointSet set1 = unionFind.find(edge.node1);
            DisjointSet set2 = unionFind.find(edge.node2);

            // 4. Ignore the edge if the nodes have the same set (prevents loops).
            // Otherwise, add the edge to the result and unite the sets.
            if (!set1.equals(set2)) {
                result.add(edge);
                unionFind.union(set1, set2);
            }
        }

        return result;
    }


    /**
     * Makes a route to follow from the locations in the given schedule by trying every combination.
     * Optimal route but slow.
     */
    public static Route getRouteBruteforce(Schedule schedule) {
        assert (schedule != null) : "Schedule shouldn't be null";

        Route route = new Route();
        route.addGraphNodeLocations(calcRouteBruteforce(scheduleToGraph(schedule), new Graph()));
        assert (route.get(0).equals(Graph.HOMENODE)) : "Route doesn't start at the home node";
        assert (route.get(route.size() - 1).equals(Graph.HOMENODE)) : "Route doesn't end at the home node";

        schedule.setRoute(route);
        return route;
    }

    /**
     * Recursive code of the getRouteBruteforce function.
     *
     * @param nodes The starting graph. Used as node list.
     * @param route The route that is ultimately returned. Should be empty in call.
     *              Of type Graph for the calcTotalDistance function.
     * @return The route. Of type Graph for the calcTotalDistance function.
     * @see #getRouteBruteforce
     */
    private static Graph calcRouteBruteforce(Graph nodes, Graph route) {
        assert (nodes != null) : "Nodes shouldn't be null";
        assert (route != null) : "Route shouldn't be null";

        // If nodes is empty, the route is complete.
        // The for loop won't run and route is returned.
        Graph minFullRoute = route;
        int minDistance = Integer.MAX_VALUE;
        for (GraphNode node : nodes) {
            // Nodes and route are cloned because each route we try should start from the current route.
            Graph newNodes = nodes.clone();
            // Each node in nodes is transferred to route 1 by 1.
            newNodes.remove(node);

            Graph newRoute = route.clone();
            newRoute.add(node);

            // Each level of recursion returns its minFullRoute.
            // Making this the optimal route for the current route + this node and beyond.
            // Though, the current route + a different node and beyond might be more optimal.
            Graph fullRoute = calcRouteBruteforce(newNodes, newRoute);
            int distance = fullRoute.calcTotalDistance();
            // The most optimal route (shortest distance) combination of "the current route + a node and beyond"
            // is saved in minFullRoute.
            if (distance < minDistance) {
                minFullRoute = fullRoute;
            }
        }

        return minFullRoute;
    }

}
