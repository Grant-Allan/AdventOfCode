package solutions.problem12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Solution to problem twelve, part one of Advent of Code.
 * https://adventofcode.com/2023/day/12
 *
 * Answer is
 */
public class Problem12Part1 {
    /** Constructor */
    public Problem12Part1() {
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

            // Process them
            long total = 0;
            for (var line : lines.entrySet()) {
                System.out.println();
                System.out.println("Line: " + line.getKey());
                int count = processLine(3, ".." + line.getKey() + "..", line.getValue());
                System.out.println("Count: " + count);
                total += count;
            }

            System.out.println("\n\nTOTAL: " + total);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private int processLine(Integer start, String line, int[] sizes) {
        int count = 0;

        //
        // Need to make it cascade the changes, like with the old version
        // That is, find every possible permutation. Which it doesn't right now.
        //

        // Process the sizes
        String updatedLine = line;
        for (int size : sizes) {
            updatedLine = processSize(start, updatedLine, size);
        }

        // Check to see whether this was a valid combination
        count += (updatedLine.contains("#")) ? 0 : 1;
        if (count == 1) {
            System.out.println(updatedLine);
        }

        // Check to see if the end of the line has been reached
        if (start < line.length()) {
            count += processLine(++start, line, sizes);
        }

        return count;
    }

    private String processSize(Integer start, String line, int size) {
        //
        // Need to check to see if there was or wasn't a valid spot. Otherwise,
        // it doesn't know if not all sizes could be placed
        //

        // Go through the line and look for a place the size will fit
        for (int i = start; i < line.length()-size; i++) {
            // If the substring is valid for being replaced
            boolean validLeftCap = Character.toString(line.charAt(i-1)).matches("[?|.]");
            boolean validSpot = line.substring(i, i+size).matches("[#|?]{"+size+"}");
            boolean validRightCap = Character.toString(line.charAt(i+size)).matches("[?|.]");
            if (validLeftCap && validSpot && validRightCap) {
                line = (new StringBuilder(line))
                        .replace(i-1, i, ".")
                        .replace(i, i+size, buildStringOf('-', size))
                        .replace(i+size, i+size+1, ".")
                        .toString();
                System.out.println(line + " <-- " + size + " at " + i);

                // Update start and exit
                start = i+size;
                break;
            }
        }

        return line;
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
}
