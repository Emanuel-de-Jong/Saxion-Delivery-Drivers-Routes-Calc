package data;

import models.Employee;

import java.util.LinkedHashMap;

public class EmployeeDB extends LinkedHashMap<Integer, Employee> {

    public static EmployeeDB instance = new EmployeeDB();

    private int lastId = 0;


    /**
     * Can be used during Complaint creation to have sequential ids.
     */
    public int getNextId() {
        return ++lastId;
    }

    public void add(Employee employee) {
        assert (employee != null) : "Employee shouldn't be null";

        putIfAbsent(employee.getId(), employee);
    }


    /**
     * Add generated Employees as test data.
     */
    public void loadTestData() {
        int i;
        for (i = 0; i < 5; i++) {
            add(new Employee(
                    i,
                    "Employee " + i));
        }
        lastId = i;
    }

}
