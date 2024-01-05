package solutions.problem12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Solution to problem twelve, part one of Advent of Code.
 * https://adventofcode.com/2023/day/12
 *
 * Answer is
 *
 * not
 * 9541 - too high
 * 9537 - too high
 * 9071 - too high
 * 9059 - too high
 *
 * 8751
 *
 * 8110
 *
 * 7497
 *
 * 7111
 *
 * 3253 - too low
 */
public class Problem12Part1_OLD {
    /** Constructor */
    public Problem12Part1_OLD() {
        try {
            File input = new File("resources/Problem12Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            Map<String, int[]> lines = new HashMap<>();
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                lines.put(line[0], Arrays.stream(line[1].split(",")).mapToInt(Integer::parseInt).toArray());
            }

            // Print them out
            for (var line : lines.entrySet()) {
                System.out.println(line.getKey() + " " + Arrays.toString(line.getValue()));
            }
            System.out.println();

            // Go through each line and figure out how many configurations are possible
            int total = 0;
            for (var line : lines.entrySet()) {
                String hotSprings = "..." + line.getKey() + "...";
                int[] sizes = line.getValue();
                System.out.println(hotSprings + " -> " + Arrays.toString(sizes));

                // Search for the possible permutations of remaining numbers
                int smallTotal = findPermutations(sizes, 0, hotSprings, 3);

                System.out.println("Small Total: " + smallTotal);
                System.out.println();

                total += smallTotal;
            }

            System.out.println("\n\nFinal Total: " + total);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Build a string of a given character
     * @param filler character it will be made of
     * @param length length of the string
     * @return the string
     */
    private String buildStringOf(char filler, int length) {
        if (length > 0) {
            char[] array = new char[length];
            Arrays.fill(array, filler);
            return new String(array);
        }
        return "";
    }

    /**
     * Find the possible permutations in a given string
     * @param sizes different hot spring sizes
     * @param curSize the current hot spring being checked
     * @param hotSprings the hot springs locations
     * @param startingPoint starting point in the string to look for where a hot spring can be placed
     * @return the number of permutations found
     */
    private int findPermutations(int[] sizes, int curSize, String hotSprings, int startingPoint) {
        if (curSize >= sizes.length) {
            return 1;
        }

        int count = 0;
        for (int j=startingPoint; j <= hotSprings.length()-sizes[curSize]-3; j++) {
            // Check to see if it must be placed there
            boolean required = (hotSprings.charAt(j) == '#');

            // Can be placed at this spot
            boolean hasSpaceBefore = Character.toString(hotSprings.charAt(j-1)).matches("[?|.]");
            boolean canContain = hotSprings.substring(j, j+sizes[curSize]).matches("[#|?]{"+sizes[curSize]+"}");
            boolean hasSpaceAfter = Character.toString(hotSprings.charAt(j+sizes[curSize])).matches("[?|.]");

            // If it can (or must) be placed at this spot
            if ((required && canContain) || (hasSpaceBefore && canContain && hasSpaceAfter)) {
                // Replace the section
                String updatedHotSprings = (new StringBuilder(hotSprings))
                        .replace(j-1, j, ".")
                        .replace(j, j+sizes[curSize], buildStringOf('-', sizes[curSize]))
                        .replace(j+sizes[curSize], j+sizes[curSize]+1, ".")
                        .toString();

                // Run for the next size in the series
                if (curSize+1 == sizes.length) {
                    System.out.println(updatedHotSprings + " " + required + " " + (hasSpaceBefore && hasSpaceAfter));
                }
                count += findPermutations(sizes, curSize+1, updatedHotSprings, j);

                // If it has to be in this position, don't allow it to keep sliding
                if (required) {
                    break;
                }
            }
        }
        return count;
    }
}
