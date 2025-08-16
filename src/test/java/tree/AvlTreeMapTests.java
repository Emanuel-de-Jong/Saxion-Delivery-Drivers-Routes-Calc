package tree;

import models.tree.AvlTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvlTreeMapTests {

    private AvlTreeMap<Double, Double> map;


    private void fill(int amountOfNodes) {
        for (int i = 0; i < amountOfNodes; i++) {
            map.insert(Math.random(), Math.random());
        }

        assertEquals(amountOfNodes, map.getSize());
    }


    @BeforeEach
    public void setup() {
        map = new AvlTreeMap<>();
    }


    @RepeatedTest(5)
    @DisplayName("Can the program search?")
    public void canSearch() {
        fill(1000000);

        double rand = Math.random();
        map.insert(rand, 10d);

        assertEquals(10d, map.find(rand));
    }

    @RepeatedTest(5)
    @DisplayName("Is the tree balanced?")
    public void isBalanced() {
        fill(1000000);

        // Save the lowest and highest balance looking at each node.
        AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger max = new AtomicInteger(Integer.MIN_VALUE);
        map.forEachNode(node -> {
            int bal = map.getBalance(node);
            if (bal < min.get()) min.set(bal);
            if (bal > max.get()) max.set(bal);
        });

        // If the balance doesn't deviate by more than 1, the tree is balanced.
        assertTrue(min.get() >= -1);
        assertTrue(max.get() <= 1);
    }


    @RepeatedTest(3)
    @DisplayName("Insert 1.000 nodes")
    public void insert1_000Nodes() {
        fill(1_000);
    }

    @RepeatedTest(3)
    @DisplayName("Insert 10.000 nodes")
    public void insert10_000Nodes() {
        fill(10_000);
    }

    @RepeatedTest(3)
    @DisplayName("Insert 100.000 nodes")
    public void insert100_000Nodes() {
        fill(100_000);
    }

    @Disabled
    @RepeatedTest(3)
    @DisplayName("Insert 1.000.000 nodes")
    public void insert1_000_000Nodes() {
        fill(1_000_000);
    }

    @Disabled
    @RepeatedTest(3)
    @DisplayName("Insert 10.000.000 nodes")
    public void insert10_000_000Nodes() {
        fill(10_000_000);
    }

}