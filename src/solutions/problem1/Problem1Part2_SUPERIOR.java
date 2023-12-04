package solutions.problem1;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Solution to problem one, part two of Advent of Code.
 * https://adventofcode.com/2023/day/1
 *
 * Answer is 53221
 */
public class Problem1Part2_SUPERIOR {
    public Problem1Part2_SUPERIOR() {
        int total = 0;

        try {
            File input = new File("resources/Problem1Input.txt");
            Scanner scanner = new Scanner(input);

            // Process the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Replace written numbers with digits
                line = line.replace("one", "one1one");
                line = line.replace("two", "two2two");
                line = line.replace("three", "three3three");
                line = line.replace("four", "four4four");
                line = line.replace("five", "five5five");
                line = line.replace("six", "six6six");
                line = line.replace("seven", "seven7seven");
                line = line.replace("eight", "eight8eight");
                line = line.replace("nine", "nine9nine");

                // Get all digits
                List<Character> digits = new ArrayList<>();
                for (char unit : line.toCharArray()) {
                    if (Character.isDigit(unit)) {
                        digits.add(unit);
                    }
                }

                // Add it to the total
                String first = String.valueOf(digits.get(0));
                String last = String.valueOf(digits.get(digits.size()-1));
                String together = (first + last);
                total += Integer.parseInt(together);

                // Record what happened
                System.out.println(line);
                System.out.println(first + " + " + last + " = " + together);
                System.out.println("total: " + total);
                System.out.println();
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("\nFINAL TOTAL: " + total);
    }
}
