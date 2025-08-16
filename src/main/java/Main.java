import controllers.RouteCalculator;
import controllers.Scheduler;
import controllers.Statistics;
import data.ComplaintDB;
import data.EmployeeDB;
import data.HomeDB;
import data.WaitingListDB;
import models.Employee;
import models.Route;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    /**
     * Interface to read user input from the System.in stream.
     */
    private final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        initializeDBs();
        menuLoop();
    }


    private void initializeDBs() {
        try {
            WaitingListDB.instance.loadFromCsv("src/main/resources/Waitinglist.csv");
            HomeDB.instance.loadFromCsv("src/main/resources/Homes.csv");
            ComplaintDB.instance.loadFromCsv("src/main/resources/Complaints.csv");
            EmployeeDB.instance.loadTestData();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        assert (WaitingListDB.instance.size() != 0) : "WaitingListDB data not loaded";
        assert (HomeDB.instance.size() != 0) : "HomeDB data not loaded";
        assert (ComplaintDB.instance.size() != 0) : "ComplaintDB data not loaded";
        assert (EmployeeDB.instance.size() != 0) : "EmployeeDB data not loaded";
    }


    private void menuLoop() {
        int choice = 1;
        while (choice != 0) {
            printMenu();
            choice = askNumber();
            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> createEmployeeSchedules();
                case 3 -> createScheduleRoutes();
                case 4 -> compareStructureOperationSpeeds();
            }
        }
    }

    private void printMenu() {
        System.out.println("\n" +
                "1) Add an employee\n" +
                "2) Create schedules for employees\n" +
                "3) Create routes for schedules\n" +
                "4) Compare structure operation speeds\n" +
                "0) Exit");
    }

    private int askNumber() {
        System.out.print("Choose a number: ");
        return scanner.nextInt();
    }


    /**
     * Adds an employee to EmployeeDB.instance.
     * User gives the name.
     */
    private void addEmployee() {
        String name = null;
        boolean nameIsValid = false;
        while (!nameIsValid) {
            System.out.print("Give a name: ");
            name = scanner.next();

            if (Pattern.matches("([A-Z][a-z]{1,50}[ ]?)+", name)) {
                nameIsValid = true;
            } else {
                System.out.println("The name can only have letters and each word must start with a capital letter");
            }
        }

        assert (name != null) : "Name shouldn't be null";

        Employee newEmployee = new Employee(EmployeeDB.instance.getNextId(), name);
        EmployeeDB.instance.add(newEmployee);

        System.out.println(newEmployee);
    }

    /**
     * Creates the schedule of each employee that has one.
     */
    private void createEmployeeSchedules() {
        Scheduler.createSchedules();
        for (Employee employee : EmployeeDB.instance.values()) {
            System.out.println(employee.getName());

            assert (employee.getSchedule() != null) : "Employee schedule shouldn't be null";
            System.out.print(employee.getSchedule());

            System.out.println("================");
        }
    }

    /**
     * Creates routes for all employee schedules.
     * User chooses the algorithm used to create the routes.
     */
    private void createScheduleRoutes() {
        System.out.println(
                "1) Good route with fast calculation\n" +
                "2) Perfect route with slow calculation");
        int calcType = askNumber();

        for (Employee employee : EmployeeDB.instance.values()) {
            if (employee.getSchedule() == null) {
                System.out.println(employee.getName() + " doesn't have a schedule");
                System.out.println("================");
                continue;
            }

            Route route;
            switch (calcType) {
                case 1:
                    route = RouteCalculator.getRouteKruskal(employee.getSchedule());
                    break;
                default:
                    route = RouteCalculator.getRouteBruteforce(employee.getSchedule());
            }

//            Visualizer.drawRoute(route);

            System.out.println(employee.getName());
            System.out.print(route);
            System.out.println("================");
        }
    }

    /**
     * Times the CRUD operations with different data sizes for a LinkedList and a RedBlackTree.
     */
    private void compareStructureOperationSpeeds() {
        Statistics.testLinkedListSpeed(new int[] {10_000, 50_000, 100_000}, 3);
        Statistics.testRedBlackTreeSpeed(new int[] {10_000, 50_000, 100_000}, 3);
    }

}
