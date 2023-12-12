package solutions.problem11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Solution to problem eleven, part two of Advent of Code.
 * https://adventofcode.com/2023/day/11
 *
 * Answer is 630728425490
 */
public class Problem11Part2 {
    private char[][] universe;
    private final Map<Integer, int[]> galaxyCoords = new HashMap<>();
    private final ArrayList<int[]> usedPairs = new ArrayList<>();

    private final ArrayList<Integer> emptyColumns = new ArrayList<>();
    private final ArrayList<Integer> emptyRows = new ArrayList<>();

    /** Constructor */
    public Problem11Part2() {
        try {
            File input = new File("resources/Problem11Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            ArrayList<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }

            // Build the 2D char array
            universe = new char[lines.size()][lines.get(0).length()];
            for (int i=0; i < lines.size(); i++) {
                universe[i] = lines.get(i).toCharArray();
            }

            // Find each galaxy location
            int numGalaxies = 1;
            for (int i=0; i < universe.length; i++) {
                for (int j = 0; j < universe[0].length; j++) {
                    if (universe[i][j] == '#') {
                        // Add the coordinate to the galaxy locations
                        galaxyCoords.put(numGalaxies++, new int[] { i, j });
                    }
                }
            }

            // Find which rows and columns don't have a galaxy in them
            for (int i=0; i < universe.length; i++) {
                // Check for no galaxy in row
                if (!hasRowValue(galaxyCoords, i)) {
                    emptyRows.add(i);
                }
            }
            for (int i=0; i < universe[0].length; i++) {
                // Check for no galaxy in column
                if (!hasColumnValue(galaxyCoords, i)) {
                    emptyColumns.add(i);
                }
            }

            // Print the universe
            for (char[] chars : universe) {
                for (int j = 0; j < universe[0].length; j++) {
                    System.out.print(chars[j]);
                }
                System.out.println();
            }

            System.out.println("\n\nTOTAL: " + getTotal());

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Check to see if the given row value is in the list of coordinates
     * @param mapOfCoordinates the coordinates being checked against
     * @param rowValue the row value being checked
     * @return true for yes, false for no
     */
    private boolean hasRowValue(Map<Integer, int[]> mapOfCoordinates, int rowValue) {
        for (int[] coordinates : mapOfCoordinates.values()) {
            if (rowValue == coordinates[0]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check to see if the given column value is in the list of coordinates
     * @param mapOfCoordinates the coordinates being checked against
     * @param columnValue the column value being checked
     * @return true for yes, false for no
     */
    private boolean hasColumnValue(Map<Integer, int[]> mapOfCoordinates, int columnValue) {
        for (int[] coordinates : mapOfCoordinates.values()) {
            if (columnValue == coordinates[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find the summed distances between galaxies
     * @return the summed distances between galaxies
     */
    private long getTotal() {
        // The total distance
        long total = 0;
        // Go through all galaxies and find the total
        for (var galaxy1 : galaxyCoords.entrySet()) {
            for (var galaxy2 : galaxyCoords.entrySet()) {
                if (!Objects.equals(galaxy1.getKey(), galaxy2.getKey()) &&
                    checkIfUsed(galaxy1.getKey(), galaxy2.getKey())) {
                    // Add galaxy to used list
                    usedPairs.add(new int[] { galaxy1.getKey(), galaxy2.getKey() });

                    // Take the expanded distance into account
                    int xMin = Math.min(galaxy1.getValue()[1], galaxy2.getValue()[1]);
                    int xMax = Math.max(galaxy1.getValue()[1], galaxy2.getValue()[1]);
                    for (int i=xMin+1; i <= xMax; i++) {
                        total += emptyColumns.contains(i) ? 1_000_000 : 1;
                    }

                    int yMin = Math.min(galaxy1.getValue()[0], galaxy2.getValue()[0]);
                    int yMax = Math.max(galaxy1.getValue()[0], galaxy2.getValue()[0]);
                    for (int i=yMin+1; i <= yMax; i++) {
                        total += emptyRows.contains(i) ? 1_000_000 : 1;
                    }
                }
            }
        }

        return total;
    }

    /**
     * Check to see if two galaxies have been used before
     * @param g1
     * @param g2
     * @return true if not used, false if used
     */
    private boolean checkIfUsed(int g1, int g2) {
        for (var used : usedPairs) {
            if ((used[0] == g1 && used[1] == g2) || (used[0] == g2 && used[1] == g1)) {
                return false;
            }
        }
        return true;
    }
}
