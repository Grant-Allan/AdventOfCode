package solutions.problem6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Solution to problem six, part one of Advent of Code.
 * https://adventofcode.com/2023/day/6
 *
 * Answer is 512295
 */
public class Problem6Part1 {
    /** Constructor */
    public Problem6Part1() {
        try {
            File input = new File("resources/Problem6Input.txt");
            Scanner scanner = new Scanner(input);

            // Process the file
            String[] raceTimesLine = scanner.nextLine().split("\\s+");
            String[] maxDistancesLine = scanner.nextLine().split("\\s+");

            // Read each into int arrays
            int[] raceTimes = new int[raceTimesLine.length-1];
            int[] maxDistances = new int[maxDistancesLine.length-1];
            for (int i=1; i < raceTimesLine.length; i++) {
                raceTimes[i-1] = Integer.parseInt(raceTimesLine[i]);
                maxDistances[i-1] = Integer.parseInt(maxDistancesLine[i]);
            }

            // Process each time and distance to find how many times for each race would beat the record
            int[] numWinningTimes = new int[raceTimes.length];
            for (int i=0; i < raceTimes.length; i++) {
                System.out.println("Winning distances for " + raceTimes[i]);

                int currentSpeed = 1;
                int timeLeftInRace = raceTimes[i]-currentSpeed;

                // Find those times
                for (int j=0; j < raceTimes[i]; j++) {
                    // Formula to see how far you'll go given your current speed and remaining time
                    int distance = currentSpeed*timeLeftInRace;

                    if (distance > maxDistances[i]) {
                        System.out.print(distance + ", ");
                        numWinningTimes[i]++;
                    }

                    currentSpeed++;
                    timeLeftInRace--;
                }
                System.out.println("\n");
            }

            int total = 1;
            for (int time : numWinningTimes) {
                total *= time;
            }
            System.out.println("TOTAL: " + total);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
