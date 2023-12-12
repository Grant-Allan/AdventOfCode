package solutions.problem11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Solution to problem eleven, part one of Advent of Code.
 * https://adventofcode.com/2023/day/11
 *
 * Answer is 9686930
 */
public class Problem11Part1 {
    private char[][] universe;
    private Map<Integer, int[]> galaxyCoords = new HashMap<>();

    /** Constructor */
    public Problem11Part1() {
        try {
            File input = new File("resources/Problem11Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            ArrayList<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
                //System.out.println(lines.get(lines.size()-1));
            }

            // Check for rows that need to be duplicated
            for (int i=0; i < lines.size(); i++) {
                if (!lines.get(i).contains("#")) {
                    lines.add(i, lines.get(i));
                    i++;
                }
            }

            // Check for columns that need to be duplicated
            for (int i=0; i < lines.get(0).length(); i++) {
                if (checkColumn(lines, i)) {
                    duplicateColumn(lines, i);
                    i++;
                }
            }

            // Build the 2D char array
            universe = new char[lines.size()][lines.get(0).length()];
            for (int i=0; i < lines.size(); i++) {
                universe[i] = lines.get(i).toCharArray();
            }

            // Find each galaxy location, replacing it with a number
            int numGalaxies = 1;
            for (int i=0; i < universe.length; i++) {
                for (int j = 0; j < universe[0].length; j++) {
                    if (universe[i][j] == '#') {
                        // Add the coordinate to the galaxy locations
                        galaxyCoords.put(numGalaxies++, new int[] { i, j });
                    }
                }
            }

            // Print the expanded universe
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
     * Find the summed distances between galaxies
     * @return the summed distances between galaxies
     */
    private int getTotal() {
        // The total distance
        int total = 0;

        // Go through all galaxies and find the total
        for (var galaxy1 : galaxyCoords.entrySet()) {
            for (var galaxy2 : galaxyCoords.entrySet()) {
                if (!Objects.equals(galaxy1.getKey(), galaxy2.getKey())) {
                    total += Math.abs(galaxy2.getValue()[0]-galaxy1.getValue()[0]) +
                             Math.abs(galaxy2.getValue()[1]-galaxy1.getValue()[1]);
                }
            }
        }

        // Adjust for having duplicate paths (Ex: g5 to g9 and g9 to g5)
        total /= 2;

        return total;
    }

    /**
     * Checks to see if a column should be duplicated
     * @param lines
     * @param column
     * @return
     */
    private boolean checkColumn(ArrayList<String> lines, int column) {
        for (String line : lines) {
            if (line.charAt(column) == '#') {
                return false;
            }
        }
        return true;
    }

    /**
     * Duplicate a column
     * @param lines
     * @param column
     */
    private void duplicateColumn(ArrayList<String> lines, int column) {
        for (int i=0; i < lines.size(); i++) {
            StringBuilder line = new StringBuilder(lines.remove(0));
            line.insert(column, '.');
            lines.add(line.toString());
        }
    }
}
