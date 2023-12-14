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
 * 7111
 *
 * 3253 - too low
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
     * Find the index of a given value in an array
     * @param array the array being searched
     * @param value the value being searched for
     * @return the index (-1 for not found)
     */
    private int findIndexOf(int[] array, int value) {
        for (int i=0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
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
     * Count the number of times a given character appears in a string
     * @return
     */
    public int countChar(String substring) {
        int count = 0;
        for (int i=0; i < substring.length(); i++) {
            if (substring.charAt(i) == '#') {
                count++;
            }
            else if (substring.charAt(i) == '.') {
                return -1;
            }
        }
        return count;
    }

    /**
     * Find the largest value in a given array
     * @param array
     * @return
     */
    static int largest(int[] array) {
        int max = 0;
        for (int i=1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Find and replace the first instance of something in a string.
     * Meant to work when that "first instance" is the only instance.
     * @param line
     * @param find
     * @return the updated line
     */
    private String replaceFirst(String line, String find, int tagsFound) {
        for (int i=0; i <= line.length()-find.length(); i++) {
            String fullSubstring = line.substring(i, i + find.length());
            if ((countChar(fullSubstring) == tagsFound) &&
                fullSubstring.matches("[#|?]{"+find.length()+"}") &&
                line.substring(i+find.length(), i+find.length()+1).matches("[?|.]")) {
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
            //System.out.println(hotSprings + " end of permutation");
            return 1;
        } else if (sizes[curSize] == -1) {
            return findPermutations(sizes, curSize+1, hotSprings, startingPoint);
        }

        int count = 0;
        for (int j=startingPoint; j <= hotSprings.length()-sizes[curSize]-3; j++) {
            String updatedHotSprings = hotSprings;

            //TODO: current problem child:
            // ...?#?#????#??.???##???... -> [1, 1, 3, 1, 2, 2]

            boolean onlyViable = countChar(hotSprings.substring(j, j+sizes[curSize])) > largest(Arrays.copyOfRange(sizes, curSize, sizes.length));

            boolean hasSpaceBefore = updatedHotSprings
                    .substring(j+-1, j)
                    .matches("[?|.]");

            boolean canContain = updatedHotSprings
                    .substring(j, j+sizes[curSize])
                    .matches("[#|?]{"+sizes[curSize]+"}");

            boolean hasSpaceAfter = updatedHotSprings
                            .substring(j+sizes[curSize], j+sizes[curSize]+1)
                            .matches("[?|.]");

            if (onlyViable || (hasSpaceBefore && canContain && hasSpaceAfter)) {
                // Replace the section
                updatedHotSprings = (new StringBuilder(updatedHotSprings))
                        .replace(j, j+sizes[curSize], buildStringOf('-', sizes[curSize]))
                        .replace(j+sizes[curSize], j+sizes[curSize]+1, ".")
                        .toString();

                // Run for the next size in the series
                if (curSize+1 == sizes.length) {
                    System.out.println(updatedHotSprings + " curSize " + curSize + " just placed: " + sizes[curSize] + " at " + j +
                            " (" + onlyViable + " "  + hasSpaceBefore + " " + canContain + " " + hasSpaceAfter + " " + (hotSprings.charAt(j) == '#') + ")");
                }
                count += findPermutations(sizes, curSize+1, updatedHotSprings, j);

                // If this was forced into this location, only allow it to exist in this location
                if (onlyViable && (hotSprings.charAt(j) == '#')) {
                    break;
                }
            }
        }
        return count;
    }
}
