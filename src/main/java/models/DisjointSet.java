package models;

public class DisjointSet {

    /**
     * Parent of the set.
     * The last parent in the reference chain is the root set.
     * Could be outdated if the set was united with another set (union) and hasn't been searched (find) after.
     *
     * @see controllers.UnionFind#union
     * @see controllers.UnionFind#find
     */
    private DisjointSet parent;
    /**
     * Represents the height of the set, but could be outdated.
     * Is compared to other sets to decide which way to union.
     * Lower ranks are merged into bigger ranks.
     */
    private int rank = 0;


    public DisjointSet getParent() {
        return parent;
    }

    public void setParent(DisjointSet parent) {
        assert (parent != null) : "Parent shouldn't be null";

        this.parent = parent;
    }

    public int getRank() {
        return rank;
    }

    public void incrementRank() {
        this.rank++;
    }

}
