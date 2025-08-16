package models.tree;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An AVL tree with keys for sorting and values for storage.
 */
public class AvlTreeMap<K extends Comparable<K>, V> {

    private AvlMapNode<K, V> root;
    private int size = 0;


    public AvlMapNode<K, V> getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }

    public int getBalance(AvlMapNode<K, V> node) {
        if (node == null) return 0;
        return getHeight(node.getLeft()) - getHeight(node.getRight());
    }

    private int getHeight(AvlMapNode<K, V> node) {
        if (node == null) return 0;
        return node.getHeight();
    }

    private AvlMapNode<K, V> insert(AvlMapNode<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new AvlMapNode<>(key, value);
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) { //smaller (left insert)
            node.setLeft(insert(node.getLeft(), key, value));
        } else if (compare > 0) { //larger (right insert)
            node.setRight(insert(node.getRight(), key, value));
        } else {
            return node;
        }

        //Set the height to the max of the two below plus one.
        node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);

        int balance = getBalance(node);

        if (balance >= 2 && key.compareTo(node.getLeft().key) < 0) return rightRotate(node);    //LL
        if (balance <= -2 && key.compareTo(node.getRight().key) > 0) return leftRotate(node);   //RR
        if (balance >= 2 && key.compareTo(node.getLeft().key) > 0) {                            //LR
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }
        if (balance <= -2 && key.compareTo(node.getRight().key) < 0) {                           //RL
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }
        return node;
    }

    public void insert(K key, V value) {
        root = insert(root, key, value);
    }

    public void insert(ArrayList<AvlMapNode<K, V>> nodes) {
        for (AvlMapNode<K, V> node : nodes) {
            insert(node.key, node.value);
        }
    }


    private AvlMapNode<K, V> leftRotate(AvlMapNode<K, V> a) {
        AvlMapNode<K, V> b = a.getRight();
        AvlMapNode<K, V> temp = b.getLeft();

        b.setLeft(a);
        a.setRight(temp);

        a.setHeight(Math.max(getHeight(a.getLeft()), getHeight(a.getRight())) + 1);
        b.setHeight(Math.max(getHeight(b.getLeft()), getHeight(b.getRight())) + 1);

        return b;
    }

    private AvlMapNode<K, V> rightRotate(AvlMapNode<K, V> b) {
        AvlMapNode<K, V> a = b.getLeft();
        AvlMapNode<K, V> temp = a.getRight();

        a.setRight(b);
        b.setLeft(temp);

        b.setHeight(Math.max(getHeight(b.getLeft()), getHeight(b.getRight())) + 1);
        a.setHeight(Math.max(getHeight(a.getLeft()), getHeight(a.getRight())) + 1);

        return a;
    }


    private AvlMapNode<K, V> find(AvlMapNode<K, V> node, K key) {
        if (node == null) return null;
        int compare = key.compareTo(node.key);
        if (compare < 0) return find(node.getLeft(), key);
        if (compare > 0) return find(node.getRight(), key);
        return node;
    }

    public V find(K key) {
        return find(root, key).value;
    }


    private void forEach(AvlMapNode<K, V> node, BiConsumer<K, V> action) {
        if (node.getLeft() != null) forEach(node.getLeft(), action);
        action.accept(node.key, node.value);
        if (node.getRight() != null) forEach(node.getRight(), action);
    }

    public void forEach(BiConsumer<K, V> action) {
        forEach(root, action);
    }

    private void forEachNode(AvlMapNode<K, V> node, Consumer<AvlMapNode<K, V>> action) {
        if (node.getLeft() != null) forEachNode(node.getLeft(), action);
        action.accept(node);
        if (node.getRight() != null) forEachNode(node.getRight(), action);
    }

    public void forEachNode(Consumer<AvlMapNode<K, V>> action) {
        forEachNode(root, action);
    }

    private ArrayList<V> toArrayList(AvlMapNode<K, V> node) {
        ArrayList<V> out = new ArrayList<>();
        if (node.getLeft() != null) out.addAll(toArrayList(node.getLeft()));
        out.add(node.value);
        if (node.getRight() != null) out.addAll(toArrayList(node.getRight()));
        return out;
    }


    public ArrayList<V> toArrayList() {
        return toArrayList(root);
    }

    private ArrayList<AvlMapNode<K, V>> toNodeArrayList(AvlMapNode<K, V> node) {
        ArrayList<AvlMapNode<K, V>> out = new ArrayList<>();
        if (node.getLeft() != null) out.addAll(toNodeArrayList(node.getLeft()));
        out.add(node);
        if (node.getRight() != null) out.addAll(toNodeArrayList(node.getRight()));
        return out;
    }

    public ArrayList<AvlMapNode<K, V>> toNodeArrayList() {
        return toNodeArrayList(root);
    }


    @Override
    public String toString() {
        return "AvlTree{" + root.toOrderedString() + '}';
    }

}
