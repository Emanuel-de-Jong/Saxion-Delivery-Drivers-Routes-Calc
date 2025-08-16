package models;

import enums.ComplaintType;

import java.util.Objects;

public class Complaint {

    public static final float HOURLY_COST = 12.5f;

    private final int id;
    private final Home home;
    private final ComplaintType type;
    private final int estimatedTime;
    /**
     * The time it ended up taking
     */
    private Integer actualTime;
    /**
     * Cost of for example materials
     */
    private Integer otherCosts;
    /**
     * Total cost including time and materials.
     */
    private float totalCost;


    public Complaint(int id, Home home, ComplaintType type, int estimatedTime) {
        assert (id >= 0) : "Id shouldn't be negative";
        assert (home != null) : "Home shouldn't be null";
        assert (type != null) : "Type shouldn't be null";
        assert (estimatedTime >= 1) : "EstimatedTime shouldn't be less than 1";

        this.id = id;
        this.home = home;
        this.type = type;
        this.estimatedTime = estimatedTime;
    }


    public int getId() {
        return id;
    }

    public Home getHome() {
        return home;
    }

    public ComplaintType getType() {
        return type;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public Float getTotalCost() {
        return totalCost;
    }

    /**
     * Returns true if both actualTime and otherCosts have a value.
     */
    public boolean isFinished() {
        return actualTime != null &&
                otherCosts != null;
    }


    /**
     * Sets the actualTime and otherCosts and calculates the totalCost.
     * Calls isFinished() and doesn't change anything when it returns false.
     *
     * @see #isFinished()
     */
    public void finish(int actualTime, int otherCosts) {
        assert (actualTime >= 1) : "ActualTime shouldn't be less than 1";
        assert (otherCosts >= 0) : "OtherCosts shouldn't be negative";

        if (isFinished()) return;

        this.actualTime = actualTime;
        this.otherCosts = otherCosts;

        totalCost = (actualTime * Complaint.HOURLY_COST) + otherCosts;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complaint complaint = (Complaint) o;
        return id == complaint.id &&
                estimatedTime == complaint.estimatedTime &&
                actualTime == complaint.actualTime &&
                Double.compare(complaint.otherCosts, otherCosts) == 0 &&
                Double.compare(complaint.totalCost, totalCost) == 0 &&
                home.equals(complaint.home) && type == complaint.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, home, type, estimatedTime, actualTime, otherCosts, totalCost);
    }

    @Override
    public String toString() {
        return "Complaint{\n" +
                "\tid=" + id + ",\n" +
                "\thome=" + home + ",\n" +
                "\ttype=" + type + ",\n" +
                "\testimatedTime=" + estimatedTime + ",\n" +
                "\tactualTime=" + actualTime + ",\n" +
                "\totherCosts=" + otherCosts + ",\n" +
                "\ttotalCost=" + totalCost + "\n" +
                '}';
    }

}
