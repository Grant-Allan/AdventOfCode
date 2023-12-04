package solutions.problem4;

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
public class Problem4Part2_EFFICIENT {
    /** Total value */
    private int total = 0;

    /** All linse in the file */
    private final Map<Integer, String> FILE = new HashMap<>();

    /** Record of duplicate values */
    private final Map<Integer, Integer> DUPLICATES = new HashMap<>();

    /** Constructor for Problem4Part2 */
    public Problem4Part2_EFFICIENT() {
        try {
            File input = new File("resources/Problem4Input.txt");
            Scanner scanner = new Scanner(input);

            // Read it into a list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Get the card and number sets
                String[] cardSets = line.split(": ", 2);
                String[] number = cardSets[0].split(" ");
                int cardNum = Integer.parseInt(number[number.length-1]);

                // Add to the file and duplicates
                FILE.put(cardNum, cardSets[1]);
                DUPLICATES.put(cardNum, 1);
            }

            // Process the FILE
            for (Map.Entry<Integer, String> entry : FILE.entrySet()) {
                //System.out.println("Card " + entry.getKey() + ": " + entry.getValue());
                System.out.println("Card " + entry.getKey());
                System.out.println("Total Duplicates: " + DUPLICATES.get(entry.getKey()));
                processLine(entry.getKey(), entry.getValue());
                System.out.println();
            }

            // Add up the results
            for (Integer value : DUPLICATES.values()) {
                //System.out.println("value: " + value);
                total += value;
                //System.out.println();
            }

            // Process it
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
                System.out.println();
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
        //System.out.println("Winning Numbers: " + Arrays.toString(winningNumbers));
        //System.out.println("Your Numbers: " + Arrays.toString(yourNumbers));

        // Check to see how many of your numbers are winning numbers
        int winners = 0;
        for (String yourNumb : yourNumbers) {
            for (String winningNum : winningNumbers) {
                if (Helper.isNumeric(yourNumb) &&
                        Helper.isNumeric(winningNum) &&
                        yourNumb.equals(winningNum)) {
                    // If it's a winning number, increase the winners count
                    winners += 1;
                }
            }
        }

        // Update the DUPLICATES map to track how many duplicates a card has
        while (winners > 0) {
            DUPLICATES.put((winners+curCard), DUPLICATES.get(winners+curCard) + DUPLICATES.get(curCard));
            winners--;
        }
    }
}
