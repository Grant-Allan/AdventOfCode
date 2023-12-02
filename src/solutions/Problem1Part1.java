package solutions;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Solve problem one, part one of Advent of Code.
 * https://adventofcode.com/2023/day/1
 *
 * Answer is 55834
 */
public class Problem1Part1 {
    public Problem1Part1() {
        int total = 0;

        try {
            File input = new File("resources/Problem1Input.txt");
            Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String first = "";
                String second = "";
                String together = "";

                // Get the first number
                for (int i=0; i < line.length(); i++) {
                    if (Helper.isNumeric(String.valueOf(line.charAt(i)))) {
                        first = String.valueOf(line.charAt(i));
                        break;
                    }
                }

                // Get the second number
                for (int i=line.length()-1; i >= 0; i--) {
                    if (Helper.isNumeric(String.valueOf(line.charAt(i)))) {
                        second = String.valueOf(line.charAt(i));
                        break;
                    }
                }

                // Process the numbers
                together += first;
                together += second;

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
