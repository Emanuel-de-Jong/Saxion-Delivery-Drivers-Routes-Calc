package controller;

import controllers.RouteCalculator;
import enums.ComplaintType;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteCalculatorTests {

    private Schedule getTestSchedule(int numberOfComplaints) {
        Random rnd = new Random();

        // All that matters is the home location, which is a random number between 0 and 1000 for x and y.
        Schedule schedule = new Schedule();
        HashSet<Location> prevLocations = new HashSet<>();
        for (int i = 0; i < numberOfComplaints; i++) {
            Location location;
            // Keep creating a new location until it's unique.
            // The chance of an overlap is roughly 1000^1000, so this is only to prevent those hard to find
            // one in a million test fails.
            do {
                location = new Location(rnd.nextInt(1000), rnd.nextInt(1000));
            } while (prevLocations.contains(location));

            prevLocations.add(location);

            schedule.add(new Complaint(i,
                    new Home(i, location, 0, 1, false),
                    ComplaintType.electricity,
                    1));
        }

        return schedule;
    }


    @Test()
    @DisplayName("No (assertion) errors on Kruskal with empty schedule.")
    public void kruskalEmpty() {
        try {
            RouteCalculator.getRouteKruskal(new Schedule());
        } catch (Error ignored) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    @Test()
    @DisplayName("Route from Kruskal goes over all nodes")
    public void kruskalRouteNoGaps() {
        Route route = RouteCalculator.getRouteKruskal(getTestSchedule(5));
        // +2 for going from and to the home node
        assertEquals(5 + 2, route.size());
    }


    @Test()
    @DisplayName("No (assertion) errors on bruteforce with empty schedule.")
    public void bruteforceEmpty() {
        try {
            RouteCalculator.getRouteBruteforce(new Schedule());
        } catch (Error ignored) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    @Test()
    @DisplayName("Route from bruteforce goes over all nodes")
    public void bruteforceRouteNoGaps() {
        Route route = RouteCalculator.getRouteBruteforce(getTestSchedule(5));
        // +2 for going from and to the home node
        assertEquals(5 + 2, route.size());
    }

}
