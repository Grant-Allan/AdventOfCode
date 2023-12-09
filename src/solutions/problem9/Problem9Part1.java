package solutions.problem9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solution to problem nine, part one of Advent of Code.
 * https://adventofcode.com/2023/day/9
 *
 * Answer is 1479011877
 */
public class Problem9Part1 {
    private final ArrayList<int[]> lines = new ArrayList<>();

    /** Constructor */
    public Problem9Part1() {
        try {
            File input = new File("resources/Problem9Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the nodes
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                lines.add(new int[line.length]);
                for (int i=0; i < line.length; i++) {
                    lines.get(lines.size()-1)[i] = Integer.parseInt(line[i]);
                }
            }

            int total = 0;
            for (var line : lines) {
                total += processLine(line);
            }
            System.out.println("\n\nTotal: " + total);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Find the next value in each line and return it
     * @param line the sequence of numbers
     * @return the next number in the sequence
     */
    private int processLine(int[] line) {
        // Set up the history arrays
        ArrayList<int[]> history = new ArrayList<>();
        history.add(line);
        for (int i=line.length-1; i > 0; i--) {
            history.add(new int[i]);
        }

        // Get the entire history
        for (int i=0; i < history.size()-1; i++) {
            for (int j=0; j < history.get(i+1).length; j++) {
                history.get(i+1)[j] = history.get(i)[j+1]-history.get(i)[j];
            }
        }

        // Find the next value
        System.out.println("Calculating next number for " + Arrays.toString(line));
        int value = 0;
        for (int i=history.size()-1; i >= 0; i--) {
            int v2 = history.get(i)[history.get(i).length-1];
            System.out.print(v2 + " + " + value + " = ");
            value = v2 + value;
            System.out.println(value);
        }
        System.out.println();

        return value;
    }
}
