package models.tree;

/**
 * Represents a node in a AvlTreeSet.
 */
public class AvlSetNode<E extends Comparable<E>> {

    public E key;
    public int height;
    public AvlSetNode<E> left;
    public AvlSetNode<E> right;


    public AvlSetNode(E key) {
        this.key = key;
        this.height = 1;
    }


    public String toOrderedString() {
        StringBuilder out = new StringBuilder();
        if (left != null) {
            out.append(left.toOrderedString());
            out.append(" ");
        }
        out.append(this);
        if (right != null) {
            out.append(" ");
            out.append(right.toOrderedString());
        }
        return out.toString();
    }

    public String toStructuredString() {
        StringBuilder out = new StringBuilder();
        out.append(this).append(':').append(height);
        if (left != null) out.append(" ").append(left.toStructuredString());
        if (right != null) out.append(" ").append(right.toStructuredString());
        return out.toString();
    }

    @Override
    public String toString() {
        return String.valueOf(key);
    }

}
