package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Solution to problem three, part 1 of Advent of Code.
 * https://adventofcode.com/2023/day/2
 *
 * Answer is
 */
public class Problem3Part1_MESSY {
    /* All symbols */
    private final String SYMBOLS = "@#$%&*-+=/";
    private final String NUMBERS = "0123456789";

    /* Current lines being considered */
    private final List<String> BLOCK = new ArrayList<>();
    private int lineLength;

    private final List<String> ALL_NUMBERS = new ArrayList<>();

    /** The row the symbol is in */
    public enum ROW {
        FIRST,
        MIDDLE,
        LAST;
    }

    /** Constructor for Problem3Part1 */
    public Problem3Part1_MESSY() {
        int total = 0;

        try {
            File input = new File("resources/Problem3Input.txt");
            Scanner scanner = new Scanner(input);

            // Process the first block
            BLOCK.add(scanner.nextLine());
            BLOCK.add(scanner.nextLine());
            BLOCK.add(scanner.nextLine());

            // Set line length
            lineLength = BLOCK.get(0).length();

            // Check the first two rows
            for (int i=0; i < lineLength; i++) {
                if (SYMBOLS.indexOf(BLOCK.get(0).charAt(i)) > -1) {
                    System.out.println("row: " + ROW.FIRST + ", index at " + i + ", symbol is " + BLOCK.get(0).charAt(i));
                    numbersCheck(i, ROW.FIRST);
                }

                if (SYMBOLS.indexOf(BLOCK.get(1).charAt(i)) > -1) {
                    System.out.println("row: " + ROW.MIDDLE + ", index at " + i + ", symbol is " + BLOCK.get(1).charAt(i));
                    numbersCheck(i, ROW.MIDDLE);
                }
            }

            // Process the rest of the file
            while (scanner.hasNextLine()) {
                // Remove the oldest line and add a new one
                BLOCK.remove(0);
                BLOCK.add(scanner.nextLine());

                // Search the middle line for connecting symbols
                for (int i=0; i < lineLength; i++) {
                    if (SYMBOLS.indexOf(BLOCK.get(1).charAt(i)) > -1) {
                        System.out.println("row: " + ROW.MIDDLE + ", index at " + i + ", symbol is " + BLOCK.get(1).charAt(i));
                        numbersCheck(i, ROW.MIDDLE);
                    }
                }
            }

            // Check the final line
            for (int i=0; i < lineLength; i++) {
                if (SYMBOLS.indexOf(BLOCK.get(2).charAt(i)) > -1) {
                    System.out.println("row: " + ROW.LAST + ", index at " + i + ", symbol is " + BLOCK.get(2).charAt(i));
                    numbersCheck(i, ROW.LAST);
                }
            }

            // Add all numbers to the total
            for (String number : ALL_NUMBERS) {
                //System.out.println(number);
                total += Integer.parseInt(number);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("FINAL TOTAL: " + total);
    }

    /**
     * Check around a symbol for numbers
     * @param i
     * @param row
     */
    private void numbersCheck(int i, ROW row) {
        // All spots being checked
        Map<Integer, List<Integer>> allSpots = new HashMap<>();

        // If this isn't the first row, add it
        if (row != ROW.FIRST) {
            List<Integer> row0Spots = new ArrayList<>();
            row0Spots.add(i - 1);
            row0Spots.add(i);
            row0Spots.add(i + 1);
            allSpots.put(0, row0Spots);
        }

        // Always add this
        List<Integer> row1Spots = new ArrayList<>();
        row1Spots.add(i-1);
        row1Spots.add(i+1);
        allSpots.put(1, row1Spots);

        List<Integer> row2Spots = new ArrayList<>();
        row2Spots.add(i - 1);
        row2Spots.add(i);
        row2Spots.add(i + 1);
        allSpots.put(2, row2Spots);

        // Check all spots
        for (int j=0; j < allSpots.size(); j++) {
            System.out.println(BLOCK.get(j));
        }
        for (int j=0; j < allSpots.size(); j++) {
            rowCheck(j, allSpots.get(j));
        }
        System.out.println("\n\n");
    }

    /**
     * Check a row for numbers by the symbol
     * @param row
     * @param spots
     */
    private void rowCheck(int row, List<Integer> spots) {
        // Numbers that appear
        List<Integer> doubledUpNumbers = new ArrayList<>();
        for (Integer spot : spots) {
            // If it's a value that was used already, skip it
            if (doubledUpNumbers.contains(spot)) {
                continue;
            }

            // If the current spot is a valid location and is a number, process it
            if ((spot >= 0) &&
                (spot < lineLength) &&
                (NUMBERS.indexOf(BLOCK.get(row).charAt(spot)) > -1)) {
                // Check to see if the number extends further left
                boolean num = true;
                int check = spot;
                while (num) {
                    if ((check-1 < 0) || (NUMBERS.indexOf(BLOCK.get(row).charAt(check - 1)) == -1)) {
                        num = false;
                    } else {
                        check--;
                    }
                }

                // Record the found number
                StringBuilder number = new StringBuilder();
                num = true;
                while (num) {
                    number.append(BLOCK.get(row).charAt(check));

                    // Make sure the next value is a number
                    if ((check+1 >= lineLength) || (NUMBERS.indexOf(BLOCK.get(row).charAt(check + 1)) == -1)) {
                        num = false;
                    } else {
                        check++;

                        // If check is a number in the row, make sure to skip it next time
                        doubledUpNumbers.add(check);
                    }
                }

                // Add the number to the list of found numbers
                System.out.print(number + ", ");
                ALL_NUMBERS.add(number.toString());
            }
        }
    }
}
