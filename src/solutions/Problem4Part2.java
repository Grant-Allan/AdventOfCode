package solutions;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Solution to problem four, part 1 of Advent of Code.
 * https://adventofcode.com/2023/day/4
 *
 * Answer is 12263631
 */
public class Problem4Part2 {
    /** Total value */
    private int total = 0;

    /** All linse in the file */
    private final Map<Integer, ArrayList<String>> FILE = new HashMap<>();

    /** Constructor for Problem4Part2 */
    public Problem4Part2() {
        try {
            File input = new File("resources/Problem4Input.txt");
            Scanner scanner = new Scanner(input);

            // Read it into a list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Get the card and num sets
                String[] cardSets = line.split(": ", 2);
                String[] number = cardSets[0].split(" ");

                FILE.put(Integer.parseInt(number[number.length-1]), new ArrayList<>());
                FILE.get(Integer.parseInt(number[number.length-1])).add(cardSets[1]);
            }

            // Add the baseline number of cards to the total
            total += FILE.size();

            // Process the FILE
            for (var entry : FILE.entrySet()) {
                // Go through the list of duplicates to be processed
                for (String set : entry.getValue()) {
                    System.out.println("Card " + entry.getKey() + ": " + set);
                    processLine(entry.getKey(), set);
                    System.out.println();
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("\n\nFINAL TOTAL: " + total);
    }

    /**
     * Process the input line.
     *
     * @param curCard the card number
     * @param numSets the string containing the winner number set and your number set
     */
    private void processLine(int curCard, String numSets) {
        // Split it into the two different sets
        String[] sets = numSets.split("\\| ");
        String[] winningNumbers = sets[0].split(" ");
        String[] yourNumbers = sets[1].split(" ");
        System.out.println("Winning Numbers: " + Arrays.toString(winningNumbers));
        System.out.println("Your Numbers: " + Arrays.toString(yourNumbers));

        // Check to see how many of your numbers are winning numbers
        for (String yourNumb : yourNumbers) {
            for (String winningNum : winningNumbers) {
                if (Helper.isNumeric(yourNumb) &&
                    Helper.isNumeric(winningNum) &&
                    yourNumb.equals(winningNum)) {
                    // If it's a winning number, increase the total and duplicate the following lines accordingly
                    total += 1;
                    curCard += 1;

                    // Get the set for the current card being duplicated
                    String set = FILE.get(curCard).get(0);

                    // Duplicate it
                    FILE.get(curCard).add(set);
                }
            }
        }
    }
}
