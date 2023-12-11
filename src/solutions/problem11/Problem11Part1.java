package solutions.problem11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Solution to problem eleven, part one of Advent of Code.
 * https://adventofcode.com/2023/day/11
 *
 * Answer is
 */
public class Problem11Part1 {
    private final ArrayList<String> lines = new ArrayList<>();

    /** Constructor */
    public Problem11Part1() {
        try {
            File input = new File("resources/Problem11Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
                System.out.println(lines.get(lines.size()-1));
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
                if (checkColumn(i)) {
                    duplicateColumn(i);
                    i++;
                }
            }

            // Find each galaxy location, replacing it with a number
            int numGalaxies = 1;
            for (int i=0; i < lines.size(); i++) {
                while (lines.get(i).contains("#")) {
                    lines.set(i, lines.get(i).replaceFirst("#", String.valueOf(numGalaxies)));
                    numGalaxies++;
                }
            }

            // Print the expanded universe
            System.out.println();
            for (var line : lines) {
                System.out.println(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Checks to see if a column should be duplicated
     * @param column
     * @return
     */
    private boolean checkColumn(int column) {
        for (String line : lines) {
            if (line.charAt(column) == '#') {
                return false;
            }
        }
        return true;
    }

    private void duplicateColumn(int column) {
        for (int i=0; i < lines.size(); i++) {
            StringBuilder line = new StringBuilder(lines.remove(0));
            line.insert(column, '.');
            lines.add(line.toString());
        }
    }
}
