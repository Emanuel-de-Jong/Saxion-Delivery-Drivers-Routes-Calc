package models.tree;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * A self balancing (Avl) tree.
 */
public class AvlTreeSet<E extends Comparable<E>> {

    public AvlSetNode<E> root;
    public int size = 0;


    public int getBalance(AvlSetNode<E> node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private int getHeight(AvlSetNode<E> node) {
        if (node == null) return 0;
        return node.height;
    }

    private AvlSetNode<E> insert(AvlSetNode<E> node, E key) {
        if (node == null) {
            size++;
            return new AvlSetNode<>(key);
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) { //smaller (left insert)
            node.left = insert(node.left, key);
        } else if (compare > 0) { //larger (right insert)
            node.right = insert(node.right, key);
        } else {
            return node;
        }

        //Set the height to the max of the two below plus one.
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        int balance = getBalance(node);

        if (balance >= 2 && key.compareTo(node.left.key) < 0) return rightRotate(node);    //LL
        if (balance <= -2 && key.compareTo(node.right.key) > 0) return leftRotate(node);   //RR
        if (balance >= 2 && key.compareTo(node.left.key) > 0) {                            //LR
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance <= -2 && key.compareTo(node.right.key) < 0) {                           //RL
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    public void insert(E key) {
        root = insert(root, key);
    }


    private AvlSetNode<E> leftRotate(AvlSetNode<E> a) {
        AvlSetNode<E> b = a.right;
        AvlSetNode<E> temp = b.left;

        b.left = a;
        a.right = temp;

        a.height = Math.max(getHeight(a.left), getHeight(a.right)) + 1;
        b.height = Math.max(getHeight(b.left), getHeight(b.right)) + 1;

        return b;
    }

    private AvlSetNode<E> rightRotate(AvlSetNode<E> b) {
        AvlSetNode<E> a = b.left;
        AvlSetNode<E> temp = a.right;

        a.right = b;
        b.left = temp;

        b.height = Math.max(getHeight(b.left), getHeight(b.right)) + 1;
        a.height = Math.max(getHeight(a.left), getHeight(a.right)) + 1;

        return a;
    }


    private AvlSetNode<E> find(AvlSetNode<E> node, E key) {
        if (node == null) return null;
        int compare = key.compareTo(node.key);
        if (compare < 0) return find(node.left, key);
        if (compare > 0) return find(node.right, key);
        return node;
    }

    public E find(E key) {
        return find(root, key).key;
    }


    private void forEach(AvlSetNode<E> node, Consumer<E> action) {
        if (node.left != null) forEach(node.left, action);
        action.accept(node.key);
        if (node.right != null) forEach(node.right, action);
    }

    public void forEach(Consumer<E> action) {
        forEach(root, action);
    }

    private void forEachNode(AvlSetNode<E> node, Consumer<AvlSetNode<E>> action) {
        if (node.left != null) forEachNode(node.left, action);
        action.accept(node);
        if (node.right != null) forEachNode(node.right, action);
    }

    public void forEachNode(Consumer<AvlSetNode<E>> action) {
        forEachNode(root, action);
    }


    private ArrayList<E> toArrayList(AvlSetNode<E> node) {
        ArrayList<E> out = new ArrayList<>();
        if (node.left != null) out.addAll(toArrayList(node.left));
        out.add(node.key);
        if (node.right != null) out.addAll(toArrayList(node.right));
        return out;
    }

    public ArrayList<E> toArrayList() {
        return toArrayList(root);
    }

    private ArrayList<AvlSetNode<E>> toNodeArrayList(AvlSetNode<E> node) {
        ArrayList<AvlSetNode<E>> out = new ArrayList<>();
        if (node.left != null) out.addAll(toNodeArrayList(node.left));
        out.add(node);
        if (node.right != null) out.addAll(toNodeArrayList(node.right));
        return out;
    }

    public ArrayList<AvlSetNode<E>> toNodeArrayList() {
        return toNodeArrayList(root);
    }


    @Override
    public String toString() {
        return "AvlTree{" + root.toStructuredString() + '}';
    }

}
