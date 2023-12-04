package solutions;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Solution to problem four, part 1 of Advent of Code.
 * https://adventofcode.com/2023/day/4
 *
 * Answer is 23673
 */
public class Problem4Part1 {
    /** Total value */
    private int total = 0;

    /** Constructor for Problem4Part1 */
    public Problem4Part1() {
        try {
            File input = new File("resources/Problem4Input.txt");
            Scanner scanner = new Scanner(input);

            // Process it
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
                processLine(line);
                System.out.println();
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("\n\nFINAL TOTAL: " + total);
    }

    /** Process the input line */
    private void processLine(String line) {
        // Get rid of the "Card n:" part
        String numSets = line.split(": ", 2)[1];

        // Split it into the two different sets
        String[] sets = numSets.split("\\| ");
        String[] winningNumbers = sets[0].split(" ");
        String[] yourNumbers = sets[1].split(" ");
        System.out.println("Winning Numbers: " + Arrays.toString(winningNumbers));
        System.out.println("Your Numbers: " + Arrays.toString(yourNumbers));

        // Check to see how many of your numbers are winning numbers
        int matches = 0;
        Double points = 0.5;
        for (String yourNumb : yourNumbers) {
            for (String winningNum : winningNumbers) {
                if (Helper.isNumeric(yourNumb) &&
                    Helper.isNumeric(winningNum) &&
                    yourNumb.equals(winningNum)) {
                    matches += 1;
                    points *= 2;
                }
            }
        }

        // If at least one match was found, add it to the total
        int pointsAsInt = points.intValue();
        System.out.println("Found " + matches + " matches");
        System.out.println("Worth " + pointsAsInt + " points");
        if (pointsAsInt > 0) {
            total += pointsAsInt;
        }
    }
}
