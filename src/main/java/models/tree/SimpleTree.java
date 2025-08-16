package models.tree;

import java.util.ArrayList;

/**
 * A hybrid of a tree and a node.
 * A full tree can be formed by looking at all children recursively.
 * Can be seen as a LinkedList where nodes can have any amount of neighbours.
 */
public class SimpleTree<T> {

    private final ArrayList<SimpleTree<T>> children = new ArrayList<>();
    private int depth = 0;
    private final T data;


    public SimpleTree(T data) {
        this.data = data;
    }

    public SimpleTree(int depth, T data) {
        this(data);
        this.depth = depth;
    }


    public int getHeight() {
        if (children.size() == 0) return depth;

        int height = 0;
        for (SimpleTree child : children) {
            int childHeight = child.getHeight();
            if (childHeight > height) {
                height = childHeight;
            }
        }

        return ++height;
    }

    public T getData() {
        return data;
    }

    public SimpleTree<T> getChild(int index) {
        return children.get(index);
    }

    public ArrayList<SimpleTree<T>> getChildren() {
        return children;
    }

    /**
     * Inserts the given data into a new tree and makes the new tree a child of this tree.
     *
     * @return A new tree with the given data inside it.
     */
    public SimpleTree<T> insert(T data) {
        SimpleTree<T> child = new SimpleTree(depth + 1, data);
        children.add(child);
        return child;
    }

}
