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
                String hotSprings = line.getKey();
                int[] sizes = line.getValue();

                // Find any spots that demand a specific number
                for (int i=0; i < sizes.length; i++) {
                    String find = buildStringOf('#', sizes[i]);
                    if (occurrencesOfIn(find, hotSprings) == 1) {
                        sizes[i] = -1;
                        hotSprings = (new StringBuilder(hotSprings))
                                .replace(i, i+find.length(), buildStringOf('-', sizes[i]))
                                .toString();
                    }
                }

                // Search for the possible permutations of remaining numbers
                total += findPermutations(sizes, 0, hotSprings, 0);
            }

            System.out.println("\n\nTotal: " + total);

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
     * Find the possible permutations in a given string
     * @param sizes different hot spring sizes
     * @param curSize the current hot spring being checked
     * @param hotSprings the hot springs locations
     * @param startingPoint starting point in the string to look for where a hotspring can be placed
     * @return the number of permutations found
     */
    private int findPermutations(int[] sizes, int curSize, String hotSprings, int startingPoint) {
        if ((curSize >= sizes.length) || (sizes[curSize] == -1)) {
            return 0;
        }

        int count = 0;
        for (int j=startingPoint; j <= hotSprings.length()-sizes[curSize]-1; j++) {
            if (hotSprings.substring(curSize, curSize + sizes[curSize]).matches("[#|?]{"+sizes[curSize]+"}")) {
                System.out.println("HERE!!!");
                // Replace the section
                hotSprings = (new StringBuilder(hotSprings))
                        .replace(j, j+sizes[curSize], buildStringOf('-', sizes[curSize]))
                        .replace(j+sizes[curSize], j+sizes[curSize]+1, ".")
                        .toString();
                count += findPermutations(sizes, curSize+1, hotSprings, j+1) + 1;
            }
        }
        return count;
    }
}
