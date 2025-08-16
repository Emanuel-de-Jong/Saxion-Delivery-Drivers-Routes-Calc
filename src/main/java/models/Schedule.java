package models;

import java.util.ArrayList;

/**
 * List of complaints for an employee to finish.
 */
public class Schedule extends ArrayList<Complaint> {

    /**
     * Order in which to drive to the locations of the complaints.
     */
    private Route route;


    public void setRoute(Route route) {
        assert (route != null) : "Route shouldn't be null";

        this.route = route;
    }

    public int getTotalEstimatedTime() {
        int total = 0;
        for (Complaint complaint : this) {
            total += complaint.getEstimatedTime();
        }
        return total;
    }


    @Override
    public String toString() {
        String result = "";
        for (Complaint complaint : this) {
            result += String.format(
                    "%s: %s (%s minutes)\n",
                    complaint.getHome().getId(),
                    complaint.getType(),
                    complaint.getEstimatedTime());
        }
        result += "Total: " + getTotalEstimatedTime() + " minutes\n";
        return result;
    }

}
