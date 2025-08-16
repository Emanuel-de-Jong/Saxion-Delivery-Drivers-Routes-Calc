package models.tree;

/**
 * Represents a node in a AvlTreeMap.
 */
public class AvlMapNode<K extends Comparable<K>, V> {

    public final K key;
    public final V value;

    private int height;
    private AvlMapNode<K, V> left;
    private AvlMapNode<K, V> right;


    public AvlMapNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.height = 1;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AvlMapNode<K, V> getLeft() {
        return left;
    }

    public void setLeft(AvlMapNode<K, V> left) {
        this.left = left;
    }

    public AvlMapNode<K, V> getRight() {
        return right;
    }

    public void setRight(AvlMapNode<K, V> right) {
        this.right = right;
    }


    public String toOrderedString() {
        StringBuilder out = new StringBuilder();
        if (left != null) out.append(left.toOrderedString()).append(" ");
        out.append(this);
        if (right != null) out.append(" ").append(right.toOrderedString());
        return out.toString();
    }

    public String toStructuredString() {
        StringBuilder out = new StringBuilder();
        out.append(this).append(":").append(height);
        if (left != null) out.append(" ").append(left.toStructuredString());
        if (right != null) out.append(" ").append(right.toStructuredString());
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvlMapNode node = (AvlMapNode) o;
        return value.equals(node.value);
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }

}
