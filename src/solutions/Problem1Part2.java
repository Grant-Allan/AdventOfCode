package solutions;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Solve problem one, part two of Advent of Code.
 * https://adventofcode.com/2023/day/1
 *
 * Answer is 53221
 */
public class Problem1Part2 {
    public Problem1Part2() {
        int total = 0;

        try {
            File input = new File("resources/Problem1Input.txt");
            Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<String> allValues = new ArrayList<>();

                // Get the first number
                for (int i=0; i < line.length(); i++) {
                    if (Helper.isNumeric(String.valueOf(line.charAt(i)))) {
                        allValues.add(String.valueOf(line.charAt(i)));
                    }

                    // Check for three letter words
                    if (i+3 <= line.length()) {
                        String placeholder = Helper.isBlockNumeric(line.substring(i, i+3));

                        if (placeholder != null) {
                            allValues.add(placeholder);
                        }
                    }

                    // Check for four letter words
                    if (i+4 <= line.length()) {
                        String placeholder = Helper.isBlockNumeric(line.substring(i, i+4));

                        if (placeholder != null) {
                            allValues.add(placeholder);
                        }
                    }

                    // Check for five letter words
                    if (i+5 <= line.length()) {
                        String placeholder = Helper.isBlockNumeric(line.substring(i, i+5));

                        if (placeholder != null) {
                            allValues.add(placeholder);
                        }
                    }
                }

                // Process the numbers
                String first = allValues.get(0);
                String second = allValues.get(allValues.size()-1);
                String together = first + second;

                // Add it to the total
                total += Integer.parseInt(together);

                // Record what happened
                System.out.println("total: " + total + " :: " + line + " --> " + first + " + " + second + " = " + together);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("FINAL TOTAL: " + total);
    }
}
