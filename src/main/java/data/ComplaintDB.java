package data;

import enums.ComplaintType;
import models.Complaint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ComplaintDB extends LinkedHashMap<Integer, Complaint> {

    public static ComplaintDB instance = new ComplaintDB();

    private int lastId = 0;


    /**
     * Can be used during Complaint creation to have sequential ids.
     */
    public int getNextId() {
        return ++lastId;
    }

    public void add(Complaint complaint) {
        assert (complaint != null) : "Complaint shouldn't be null";

        putIfAbsent(complaint.getId(), complaint);
    }


    /**
     * A hard copy of the ComplaintDB filtered by the complaint finish values.
     *
     * @param include Only keeps finished complaints when true and vise versa.
     */
    public ComplaintDB filterByFinished(boolean include) {
        ComplaintDB result = new ComplaintDB();
        for (Map.Entry<Integer, Complaint> entry : entrySet()) {
            if (entry.getValue().isFinished() == include) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        assert (result.size() == 0 || !result.values().stream().anyMatch(c -> c.isFinished() != include)) :
                "Result is not filtered";
        return result;
    }

    /**
     * A hard copy of the ComplaintDB filtered by the complaint homeId values.
     *
     * @param homeId The homeId to match.
     */
    public ComplaintDB filterByHomeId(int homeId) {
        assert (homeId >= 0) : "HomeId shouldn't be negative";

        ComplaintDB result = new ComplaintDB();
        for (Map.Entry<Integer, Complaint> entry : entrySet()) {
            if (entry.getValue().getHome().getId() == homeId) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        assert (result.values().stream().findFirst().get().getHome().getId() == homeId) : "Result is not filtered";
        return result;
    }

    /**
     * A hard copy of the ComplaintDB sorted by the complaint totalCost values.
     *
     * @param asc Whether the result should start with the highest or lowest value.
     */
    public ComplaintDB sortByTotalCost(boolean asc) {
        // Add the entries to a list since LinkedHashMaps can't use the sort method.
        List<Map.Entry<Integer, Complaint>> sorted = new ArrayList<>(this.entrySet());
        Collections.sort(sorted, (e1, e2) -> {
            if (asc)
                return Math.round(e1.getValue().getTotalCost() - e2.getValue().getTotalCost());
            return Math.round(e2.getValue().getTotalCost() - e1.getValue().getTotalCost());
        });

        // Put the sorted entries back in a new ComplaintDB instance.
        ComplaintDB result = new ComplaintDB();
        for (Map.Entry<Integer, Complaint> entry : sorted) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Add new complaints from the data in the CSV of the given filePath.
     */
    public void loadFromCsv(String filePath) throws IOException {
        File file = new File(filePath);
        assert (file.exists()) : "FilePath should lead to a file";

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        // Skip first header line
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] args = line.split(";");

            try {
                Complaint complaint = new Complaint(
                        Integer.parseInt(args[0]),
                        HomeDB.instance.get(Integer.parseInt(args[1])),
                        ComplaintType.valueOf(args[2]),
                        Integer.parseInt(args[3]));

                // Some entries already have an ActualTime and OtherCosts value.
                // There are no entries with only one of the two, so a check for length is sufficient.
                if (args.length == 6) {
                    complaint.finish(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                }

                add(complaint);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

}
