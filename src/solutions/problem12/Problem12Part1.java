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
 * 9059 - too high
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

            // Go through each line and figure out how many configurations are possible
            int total = 0;
            for (var line : lines.entrySet()) {
                String hotSprings = line.getKey() + ".";
                int[] sizes = line.getValue();
                System.out.println(hotSprings + " -> " + Arrays.toString(sizes));

                // Find any spots that demand a specific number
                for (int i=0; i < sizes.length; i++) {
                    String find = buildStringOf('#', sizes[i]);
                    if (occurrencesOfIn(find, hotSprings) == 1) {
                        hotSprings = replaceFirst(hotSprings, find);
                        sizes[i] = -1;
                    }
                }
                String adjusted = hotSprings.replace('?', '#');
                for (int i=0; i < sizes.length; i++) {
                    String find = buildStringOf('#', sizes[i]);
                    if (occurrencesOfIn(find, adjusted) == 1) {
                        hotSprings = replaceFirstAdjusted(hotSprings, find);
                        sizes[i] = -1;
                    }
                }

                // Search for the possible permutations of remaining numbers
                System.out.println(hotSprings + " -> " + Arrays.toString(sizes));
                int smallTotal = findPermutations(sizes, 0, hotSprings+".", 0);
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
     * Find the number of occurrences of a given string in a given line.
     * Overlap is allowed.
     *
     * @param find the string being searched for
     * @param line the line being searched in
     * @return the number of occurrences
     */
    private int occurrencesOfIn(String find, String line) {
        int count = 0;
        for (int i=0; i <= line.length()-find.length(); i++) {
            if (find.equals(line.substring(i, i + find.length()))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Find and replace the first instance of something in a string.
     * Meant to work when that "first instance" is the only instance.
     * @param line
     * @param find
     * @return the updated line
     */
    private String replaceFirst(String line, String find) {
        for (int i=0; i <= line.length()-find.length(); i++) {
            if (find.equals(line.substring(i, i + find.length()))) {
                return (new StringBuilder(line))
                        .replace(i, i+find.length(), buildStringOf('-', find.length()))
                        .replace(i+find.length(), i+find.length()+1, ".")
                        .toString();
            }
        }
        return line;
    }

    /**
     * Find and replace the first instance of something in a string.
     * Meant to work when that "first instance" is the only instance.
     * @param line
     * @param find
     * @return the updated line
     */
    private String replaceFirstAdjusted(String line, String find) {
        String adjusted = line.replace('?', '#');
        for (int i=0; i <= adjusted.length()-find.length(); i++) {
            if (find.equals(adjusted.substring(i, i + find.length()))) {
                return (new StringBuilder(line))
                        .replace(i, i+find.length(), buildStringOf('-', find.length()))
                        .replace(i+find.length(), i+find.length()+1, ".")
                        .toString();
            }
        }
        return line;
    }

    /**
     * Find the possible permutations in a given string
     * @param sizes different hot spring sizes
     * @param curSize the current hot spring being checked
     * @param hotSprings the hot springs locations
     * @param startingPoint starting point in the string to look for where a hotspring can be placed
     * @return the number of permutations found
     */
    private int findPermutations(int[] sizes, int curSize, String hotSprings, int startingPoint) {
        if (curSize >= sizes.length) {
            System.out.println("At " + curSize + " so increasing by 1");
            return 1;
        } else if (sizes[curSize] == -1) {
            return findPermutations(sizes, curSize+1, hotSprings, startingPoint);
        }

        int count = 0;
        for (int j=startingPoint; j <= hotSprings.length()-sizes[curSize]; j++) {
            String updatedHotSprings = hotSprings;
            if (updatedHotSprings
                    .substring(j, j+sizes[curSize])
                    .matches("[#|?]{"+sizes[curSize]+"}") &&
                    updatedHotSprings
                            .substring(j+sizes[curSize], j+sizes[curSize]+1)
                            .matches("[?|.]")) {
                // Replace the section
                updatedHotSprings = (new StringBuilder(updatedHotSprings))
                        .replace(j, j+sizes[curSize], buildStringOf('-', sizes[curSize]))
                        .replace(j+sizes[curSize], j+sizes[curSize]+1, ".")
                        .toString();

                // Run for the next size in the series
                System.out.println(updatedHotSprings + " curSize " + curSize + " just placed: " + sizes[curSize] + " at " + j);
                count += findPermutations(sizes, curSize+1, updatedHotSprings, j);
            }
        }
        return count;
    }
}
