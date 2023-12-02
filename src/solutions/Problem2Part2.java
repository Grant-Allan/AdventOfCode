package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solution to problem two, part 2 of Advent of Code.
 * https://adventofcode.com/2023/day/2
 *
 * Answer is
 */
public class Problem2Part2 {
    /* The colors used */
    private final String RED = "red";
    private final String GREEN = "green";
    private final String BLUE = "blue";

    /** Constructor for Problem2Part1 */
    public Problem2Part2() {
        int total = 0;

        try {
            File input = new File("resources/Problem2Input.txt");
            Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);

                // Get each pulls for that game
                String[] allPulls = (line.split(": ")[1]).split("; ");

                // Process each pull to see if it's valid
                int minRed = 0;
                int minGreen = 0;
                int minBlue = 0;
                for (String pull : allPulls) {
                    // Split it into each color and its number
                    String[] numOfColors = pull.split(", ");
                    System.out.println("pull: " + Arrays.toString(numOfColors));

                    // Verify each color
                    for (String numOfColor : numOfColors) {
                        // Split into color and number
                        String[] numAndColor = numOfColor.split(" ");
                        System.out.println("     numAndColor: " + Arrays.toString(numAndColor));

                        int number = Integer.parseInt(numAndColor[0]);
                        String color = numAndColor[1];

                        // Verify red colors
                        if (RED.equals(color) && (number > minRed)) {
                            minRed = number;
                        }

                        // Verify green colors
                        if (GREEN.equals(color) && (number > minGreen)) {
                            minGreen = number;
                        }

                        // Verify blue colors
                        if (BLUE.equals(color) && (number > minBlue)) {
                            minBlue = number;
                        }
                    }
                }
                System.out.println("Min Red: " + minRed);
                System.out.println("Min Green: " + minGreen);
                System.out.println("Min Blue: " + minBlue);

                // Add the sum to the total
                int sum = minRed*minGreen*minBlue;
                total += sum;
                System.out.println("sum: " + sum);
                System.out.println("Current Total: " + total);

                System.out.println("");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("FINAL TOTAL: " + total);
    }
}
