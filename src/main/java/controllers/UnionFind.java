package controllers;

import models.DisjointSet;
import models.graph.Graph;
import models.graph.GraphNode;

import java.util.HashMap;

/**
 * Helper functions for DisjointSets.
 * Used by Kruskal's algorithm.
 */
public class UnionFind {

    private final HashMap<Integer, DisjointSet> sets;


    public UnionFind(Graph graph) {
        assert (graph != null) : "Graph shouldn't be null";

        sets = new HashMap<>();
        for (GraphNode node : graph) {
            DisjointSet set = new DisjointSet();
            set.setParent(set);
            sets.put(node.id, set);
        }
    }


    public DisjointSet find(GraphNode node) {
        assert (node != null) : "Node shouldn't be null";

        return find(sets.get(node.id));
    }

    /**
     * Gets the root set of the given set.
     */
    public DisjointSet find(DisjointSet set) {
        assert (set != null) : "Set shouldn't be null";

        // The set is the root set if its parent is itself.
        while (set.getParent() != set) {
            // Assign the parent to its parent until half the parents have the root set as parent.
            // This makes the next find with this set faster since the next line will set the root set directly.
            // Only half the parents are updated because we always skip a parent.
            set.setParent(set.getParent().getParent());
            // Set the set to its parent until it's the root set.
            set = set.getParent();
        }

        return set;
    }

    /**
     * Unites the given sets.
     * The parent set with the lowest rank is merged into the other parent set.
     */
    public void union(DisjointSet set1, DisjointSet set2) {
        assert (set1 != null) : "Set1 shouldn't be null";
        assert (set2 != null) : "Set2 shouldn't be null";

        set1 = find(set1);
        set2 = find(set2);

        // A set can't be merged with itself.
        if (set1.equals(set2)) return;

        // Set the parent of one set to the other set depending on the rank.
        if (set1.getRank() < set2.getRank()) {
            set2.setParent(set1);
        } else {
            set1.setParent(set2);
        }

        if (set1.getRank() == set2.getRank()) {
            set1.incrementRank();
            set2.incrementRank();
        }
    }

}
