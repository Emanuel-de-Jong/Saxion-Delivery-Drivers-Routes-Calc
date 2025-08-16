package controllers;

import data.ComplaintDB;
import data.EmployeeDB;
import models.Complaint;
import models.Employee;
import models.Schedule;
import models.tree.AvlMapNode;
import models.tree.AvlTreeMap;

import java.util.ArrayList;

/**
 * Creates and fills schedules in a time efficient way.
 */
public class Scheduler {

    /**
     * The maximum minutes a schedule should be planned for.
     * It represents the minutes in a work day since a new schedule is made every day.
     */
    public static final int MIN_PER_DAY = 8 * 60;
    /**
     * Used by calcSchedule() to compare routes with the best route found so far.
     * Should be reset to 0 after every outside call.
     *
     * @see #calcSchedule
     */
    private static int bestTotalTime = 0;


    /**
     * Chooses unfinished complaints focused on minimum estimated time left over.
     * WARNING: Using an AvlTreeMap was a bad idea and results in suboptimal results!
     *          The code will keep being used for other systems that depend on a filled schedule,
     *          but it should not be seen as an algorithm for grading!
     */
    public static void createSchedules() {
        ComplaintDB complaintDB = ComplaintDB.instance.filterByFinished(false);

        // Make a list of AvlMapNodes where each unfinished complaint is a node.
        ArrayList<AvlMapNode<Integer, Complaint>> nodes = new ArrayList<>();
        for (Complaint complaint : complaintDB.values()) {
            nodes.add(new AvlMapNode<>(complaint.getEstimatedTime(), complaint));
        }

        for (Employee employee : EmployeeDB.instance.values()) {
            AvlTreeMap<Integer, Complaint> tree = new AvlTreeMap<>();
            // Insert the (remaining) nodes in a new tree.
            tree.insert(nodes);

            ArrayList<AvlMapNode> solution = calcSchedule(tree.getRoot(), 0);
            // Needs to be reset after each call.
            bestTotalTime = 0;

            Schedule schedule = new Schedule();
            for (AvlMapNode<Integer, Complaint> node : solution) {
                // Remove each chosen node from nodes so multiple schedules can't have the same complaint.
                nodes.removeIf(n -> n.equals(node));
                // Add the node complaint to the schedule.
                schedule.add(node.value);
            }

            assert (schedule.getTotalEstimatedTime() <= MIN_PER_DAY) : "Schedule exceeds the work minutes in a day";
            employee.setSchedule(schedule);
        }
    }

    /**
     * Recursive code of the createSchedules function.
     *
     * @param node      Node to check. Should be root node on call.
     * @param totalTime Current sum of node estimated times. Should be 0 on call.
     * @see #createSchedules
     */
    private static ArrayList<AvlMapNode> calcSchedule(AvlMapNode<Integer, Complaint> node, int totalTime) {
        assert (node != null) : "Node shouldn't be null";
        assert (totalTime >= 0) : "TotalTime shouldn't be negative";

        ArrayList<AvlMapNode> solution = new ArrayList<>();

        // Don't forget the key is the estimated time in our case.
        totalTime += node.key;
        if (totalTime > MIN_PER_DAY) return solution;

        ArrayList<AvlMapNode> children = new ArrayList<>();
        if (node.getLeft() != null) children.add(node.getLeft());
        if (node.getRight() != null) children.add(node.getRight());

        ArrayList<AvlMapNode> bestSolution = null;
        for (AvlMapNode childNode : children) {
            ArrayList<AvlMapNode> childSolution = calcSchedule(childNode, totalTime);
            // Since the tree is iterated depth first, one of the leaf notes connected to the child
            // might set the bestTotalTime.
            // We check this by comparing the childSolution total time to bestTotalTime.
            // If it's identical, we set bestSolution to its solution.
            if (totalTime + calcTime(childSolution) == bestTotalTime) bestSolution = childSolution;
        }

        // If bestSolution isn't null, a chain reaction upward is started to collect the path to the solution.
        if (bestSolution != null) {
            // Add the node to the solution of the child and return it.
            // Each parent note will get here and add itself until it's returned to the outside caller.
            bestSolution.add(0, node);
            return bestSolution;
        }

        // If the time of this solution is the best so far, set the new bestTotalTime and add this note
        // to the returning solution to start the bestSolution chain reaction.
        if (totalTime > bestTotalTime) {
            solution.add(node);
            bestTotalTime = totalTime;
        }

        return solution;
    }

    /**
     * Sums up and returns the estimated times of all given nodes.
     */
    private static int calcTime(ArrayList<AvlMapNode> nodes) {
        assert (nodes != null) : "Nodes shouldn't be null";

        int totalTime = 0;
        for (AvlMapNode node : nodes) {
            totalTime += (int) node.key;
        }
        return totalTime;
    }

}
