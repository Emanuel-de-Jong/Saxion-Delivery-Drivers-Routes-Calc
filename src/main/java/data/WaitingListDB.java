package data;

import models.Client;
import models.WaitingListEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class WaitingListDB extends LinkedHashMap<Integer, WaitingListEntry> {

    public static WaitingListDB instance = new WaitingListDB();

    private int lastId = 0;


    /**
     * Can be used during Complaint creation to have sequential ids.
     */
    public int getNextId() {
        return ++lastId;
    }

    public void add(WaitingListEntry entry) {
        assert (entry != null) : "Entry shouldn't be null";

        putIfAbsent(entry.getId(), entry);
    }


    /**
     * Add new entries from the data in the CSV of the given filePath.
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
                add(new WaitingListEntry(
                        Integer.parseInt(args[0]),
                        new Client(Integer.parseInt(args[0]), args[1], args[2]),
                        Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]),
                        Boolean.parseBoolean(args[5])));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

}
