package solutions.problem6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Solution to problem six, part two of Advent of Code.
 * https://adventofcode.com/2023/day/6
 *
 * Answer is
 */
public class Problem6Part2 {
    /** Constructor */
    public Problem6Part2() {
        try {
            File input = new File("resources/Problem6Input.txt");
            Scanner scanner = new Scanner(input);

            // Process the file
            String[] raceTimesLine = scanner.nextLine().split("\\s+");
            String[] maxDistancesLine = scanner.nextLine().split("\\s+");

            // Get the numbers
            StringBuilder raceTimeString = new StringBuilder();
            StringBuilder maxDistanceString = new StringBuilder();
            for (int i=1; i < raceTimesLine.length; i++) {
                raceTimeString.append(raceTimesLine[i]);
                maxDistanceString.append(maxDistancesLine[i]);
            }
            long raceTime = Long.parseLong(raceTimeString.toString());
            long maxDistance = Long.parseLong(maxDistanceString.toString());

            // Process each time and distance to find how many times for each race would beat the record
            long currentSpeed = 0;
            long timeLeftInRace = raceTime;
            for (long i = 0; i < raceTime; i++) {
                if ((currentSpeed*timeLeftInRace) > maxDistance) {
                    break;
                }
                currentSpeed++;
                timeLeftInRace--;
            }
            System.out.println("Escaped at current speed " + currentSpeed + ", time left " + timeLeftInRace);

            // Calculate the total number of times that can win
            long total = (raceTime - (2*currentSpeed))+1;
            System.out.println("TOTAL: " + total);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
