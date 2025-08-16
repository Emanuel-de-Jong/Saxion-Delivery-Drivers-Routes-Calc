package models;

public class Employee {

    private final int id;
    private String name;
    private Schedule schedule;


    public Employee(int id) {
        assert (id >= 0) : "Id shouldn't be negative";

        this.id = id;
    }

    public Employee(int id, String name) {
        this(id);
        assert (name != null) : "Name shouldn't be null";

        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        assert (schedule != null) : "Schedule shouldn't be null";

        this.schedule = schedule;
    }


    @Override
    public String toString() {
        return "Employee{" + '\n' +
                "\tid=" + id + ",\n" +
                "\tname=" + name + ",\n" +
                "\tschedule=" + schedule + ",\n" +
                '}';
    }

}
