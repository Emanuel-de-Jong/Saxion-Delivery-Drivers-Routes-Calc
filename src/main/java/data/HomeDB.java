package data;

import models.Home;
import models.Location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class HomeDB extends LinkedHashMap<Integer, Home> {

    public static HomeDB instance = new HomeDB();

    private int lastId = 0;


    /**
     * Can be used during Complaint creation to have sequential ids.
     */
    public int getNextId() {
        return ++lastId;
    }

    public void add(Home home) {
        assert (home != null) : "Home shouldn't be null";

        putIfAbsent(home.getId(), home);
    }


    /**
     * Add new homes from the data in the CSV of the given filePath.
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
                add(new Home(
                        Integer.parseInt(args[0]),
                        new Location(Integer.parseInt(args[1]), Integer.parseInt(args[2])),
                        Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]),
                        Boolean.parseBoolean(args[5])));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

}
