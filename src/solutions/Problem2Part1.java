package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solution to problem two, part 1 of Advent of Code.
 * https://adventofcode.com/2023/day/2
 *
 * Answer is 1734
 */
public class Problem2Part1 {
    /* The maximum allowed amount of each cube color */
    private final int MAX_RED_CUBES = 12;
    private final int MAX_GREEN_CUBES = 13;
    private final int MAX_BLUE_CUBES = 14;

    /* The colors used */
    private final String RED = "red";
    private final String GREEN = "green";
    private final String BLUE = "blue";

    /** Constructor for Problem2Part1 */
    public Problem2Part1() {
        int total = 0;

        try {
            File input = new File("resources/Problem2Input.txt");
            Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);

                // Get the game being played
                String game = line.split(": ", 2)[0];
                int gameNum = Integer.parseInt(game.split(" ")[1]);

                // Get each pulls for that game
                String[] allPulls = (line.split(": ")[1]).split("; ");

                // Process each pull to see if it's valid
                boolean valid = true;
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
                        if (RED.equals(color) && (number > MAX_RED_CUBES)) {
                            valid = false;
                        }

                        // Verify green colors
                        if (GREEN.equals(color) && (number > MAX_GREEN_CUBES)) {
                            valid = false;
                        }

                        // Verify blue colors
                        if (BLUE.equals(color) && (number > MAX_BLUE_CUBES)) {
                            valid = false;
                        }
                    }
                }

                // If all pulls were valid, add the game number to the total
                if (valid) {
                    System.out.println("All pulls were valid! Adding " + gameNum + " to total!");
                    total += gameNum;
                    System.out.println("Current total: " + total);
                } else {
                    System.out.println("Pulls for " + gameNum + " were not valid! Skipping it.");
                }

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
