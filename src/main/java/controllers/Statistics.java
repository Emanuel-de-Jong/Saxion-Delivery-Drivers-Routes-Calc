package controllers;

import models.WaitingListEntry;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.Consumer;

public class Statistics {

    public static void testLinkedListSpeed(int[] loopCounts, int repeatCount) {
        assert (!Arrays.stream(loopCounts).anyMatch(i -> i < 1)) : "LoopCounts shouldn't have numbers less than 1";
        assert (repeatCount >= 1) : "repeatCount shouldn't be less than 1";

        System.out.println("=== Linked list ===");
        for (int r = 1; r <= repeatCount; r++) {
            System.out.println("== Iteration " + r + " ==");

            for (int loopCount : loopCounts) {
                LinkedList<WaitingListEntry> linkedList = new LinkedList<>();

                System.out.println("= Loop count " + loopCount + " =");
                testOperationSpeed("Add", (i) -> linkedList.add(new WaitingListEntry(i)), loopCount, true);
                testOperationSpeed("Get", linkedList::get, loopCount, true);
                testOperationSpeed("Remove", linkedList::remove, loopCount, false);
            }
        }
    }

    public static void testRedBlackTreeSpeed(int[] loopCounts, int repeatCount) {
        assert (!Arrays.stream(loopCounts).anyMatch(i -> i < 1)) : "LoopCounts shouldn't have numbers less than 1";
        assert (repeatCount >= 1) : "repeatCount shouldn't be less than 1";

        System.out.println("=== Red black tree ===");
        for (int r = 1; r <= repeatCount; r++) {
            System.out.println("== Iteration " + r + " ==");

            for (int loopCount : loopCounts) {
                TreeMap<Integer, WaitingListEntry> redBlackTree = new TreeMap<>();

                System.out.println("= Loop count " + loopCount + " =");
                testOperationSpeed("Add", (i) -> redBlackTree.put(i, new WaitingListEntry(i)), loopCount, true);
                testOperationSpeed("Get", redBlackTree::get, loopCount, true);
                testOperationSpeed("Remove", redBlackTree::remove, loopCount, false);
            }
        }
    }


    /**
     * Prints the duration of the given operation.
     * The operation is called loopCount times and gets an Integer between 0 and the loopCount.
     * The direction of the call arguments is decided by the given asc bool.
     *
     * @param name      Operation name. Used in print.
     * @param operation Function to test.
     * @param loopCount Amount of times the operation is called.
     * @param asc       Whether the arguments should increase in ascending order or decrease in descending order.
     */
    private static void testOperationSpeed(String name, Consumer<Integer> operation, int loopCount, boolean asc) {
        assert (name != null) : "Name shouldn't be null";
        assert (operation != null) : "Operation shouldn't be null";
        assert (loopCount >= 1) : "LoopCount shouldn't be less than 1";

        Instant start = Instant.now();
        if (asc) {
            for (int i = 0; i <= loopCount; i++) {
                operation.accept(i);
            }
        } else {
            for (int i = loopCount; i >= 0; i--) {
                operation.accept(i);
            }
        }
        System.out.println(name + " " + Duration.between(start, Instant.now()).toNanos() + " nanos");
    }

}
