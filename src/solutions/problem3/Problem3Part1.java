package solutions.problem3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Solution to problem three, part 1 of Advent of Code.
 * https://adventofcode.com/2023/day/3
 *
 * Answer is 525911
 */
public class Problem3Part1 {
    /** All symbols */
    private final String SYMBOLS = "@#$%&*-+=/";

    /** All numbers gathered */
    private final List<String> ALL_NUMBERS = new ArrayList<>();

    /** The input file, as a 2D character array */
    private char[][] file;
    private int xLength;
    private int yLength;

    /* Locations of numbers that have already been used */
    private final List<int[]> USED_LOCATIONS = new ArrayList<>();

    /** Constructor for Problem3Part1 */
    public Problem3Part1() {
        int total = 0;

        try {
            File input = new File("resources/Problem3Input.txt");
            Scanner scanner = new Scanner(input);

            // Read it into a list
            List<String> allLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                allLines.add(scanner.nextLine());
            }

            // Create a 2D array of characters out of the file
            xLength = allLines.get(0).length();
            yLength = allLines.size();
            file = new char[xLength][yLength];

            // Fill the file char array
            for (int i=0; i < allLines.size(); i++) {
                file[i] = allLines.get(i).toCharArray();
            }

            // Run through every line looking for symbols
            for (int x=0; x < xLength; x++) {
                for (int y=0; y < yLength; y++) {
                    // Check to see if the current x column of row y is a symbol
                    if (SYMBOLS.indexOf(file[x][y]) > -1) {
                        System.out.println("(" + x + ", " + y + "): " + file[x][y]);
                        checkArea(x, y);
                        System.out.println("\n");
                    }
                }
            }

            // Add all numbers to the total
            for (String number : ALL_NUMBERS) {
                total += Integer.parseInt(number);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("\n\nFINAL TOTAL: " + total);
    }

    /**
     * Check around a detected symbol for numbers
     *
     * @param x the x location of the symbol
     * @param y the y location of the symbol
     */
    private void checkArea(int x, int y) {
        // The locations that need to be checked
        List<int[]> area = new ArrayList<>();

        // Upper row
        area.add(new int[] {x-1, y-1});
        area.add(new int[] {x-1, y});
        area.add(new int[] {x-1, y+1});

        // Middle row
        area.add(new int[] {x, y-1});
        area.add(new int[] {x, y+1});

        // Lower row
        area.add(new int[] {x+1, y-1});
        area.add(new int[] {x+1, y});
        area.add(new int[] {x+1, y+1});

        // Process locations in the area to check for numbers
        for (int[] loc : area) {
            // If a location has been used, skip it
            if (previouslyUsed(loc)) {
                continue;
            }

            // If it is a digit, track it to where the number starts
            while ((loc[0] >= 0) &&
                    (loc[0] < xLength) &&
                    (loc[1] >= 0) &&
                    (loc[1] < yLength) &&
                    Character.isDigit(file[loc[0]][loc[1]])) {
                if ((loc[1] > 0) && Character.isDigit(file[loc[0]][loc[1]-1])) {
                    loc[1]--;
                } else {
                    break;
                }
            }

            // Having gotten the starting location of the number, build the entire number
            StringBuilder number = new StringBuilder();
            while ((loc[0] >= 0) &&
                    (loc[0] < xLength) &&
                    (loc[1] >= 0) &&
                    (loc[1] < yLength) &&
                    Character.isDigit(file[loc[0]][loc[1]])) {
                USED_LOCATIONS.add(loc.clone());
                number.append(file[loc[0]][loc[1]]);
                loc[1]++;
            }

            // Add the number to the list of found numbers
            if (number.length() != 0) {
                System.out.print(number + ", ");
                ALL_NUMBERS.add(number.toString());
            }
        }
    }

    /**
     * Checks to see if a location has been used before
     * @param loc the location being checked
     * @return true for used, false for not
     */
    private boolean previouslyUsed(int[] loc) {
        for (int[] usedLoc : USED_LOCATIONS) {
            if ((usedLoc[0] == loc[0]) && (usedLoc[1] == loc[1])) {
                return true;
            }
        }
        return false;
    }
}
