package main;

import solutions.problem1.Problem1Part1;
import solutions.problem1.Problem1Part2;
import solutions.problem1.Problem1Part2_SUPERIOR;
import solutions.problem10.Problem10Part1;
import solutions.problem10.Problem10Part2;
import solutions.problem10.Problem10Part2_REWORK;
import solutions.problem11.Problem11Part1;
import solutions.problem2.Problem2Part1;
import solutions.problem2.Problem2Part2;
import solutions.problem3.Problem3Part1;
import solutions.problem3.Problem3Part1_MESSY;
import solutions.problem3.Problem3Part2;
import solutions.problem4.Problem4Part1;
import solutions.problem4.Problem4Part2;
import solutions.problem4.Problem4Part2_EFFICIENT;
import solutions.problem5.Problem5Part1;
import solutions.problem5.Problem5Part2;
import solutions.problem5.Problem5Part2_EFFICIENT;
import solutions.problem6.Problem6Part1;
import solutions.problem6.Problem6Part2;
import solutions.problem7.Problem7Part1;
import solutions.problem7.Problem7Part2;
import solutions.problem8.Problem8Part1;
import solutions.problem8.Problem8Part2;
import solutions.problem9.Problem9Part1;
import solutions.problem9.Problem9Part2;

import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        // Get the time when the program starts
        long startTime = System.currentTimeMillis();

        /* RUN THE PROBLEMS */
        //new Problem1Part1();
        //new Problem1Part2();
        //new Problem1Part2_SUPERIOR();

        //new Problem2Part1();
        //new Problem2Part2();

        //new Problem3Part1();
        //new Problem3Part1_MESSY();
        //new Problem3Part2();

        //new Problem4Part1();
        //new Problem4Part2();
        //new Problem4Part2_EFFICIENT();

        //new Problem5Part1();
        //new Problem5Part2();
        //new Problem5Part2_EFFICIENT();

        //new Problem6Part1();
        //new Problem6Part2();

        //new Problem7Part1();
        //new Problem7Part2();

        //new Problem8Part1();
        //new Problem8Part2();

        //new Problem9Part1();
        //new Problem9Part2();

        //new Problem10Part1();
        //new Problem10Part2();
        new Problem10Part2_REWORK();

        //new Problem11Part1();
        //new Problem11Part2();

        // Get the time taken for the program to run
        long endTime = System.currentTimeMillis();
        timeToRun(startTime, endTime);
    }

    /**
     * Calculate the time taken for the program to run in the largest unit
     * that makes sense, going from milliseconds to days taken.
     *
     * @param startTime when the program started
     * @param endTime when the program ended
     */
    private static void timeToRun(long startTime, long endTime) {
        // Format for printing the time
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);

        // Get the time taken in milliseconds
        double time = (endTime - startTime);
        String unit = " milliseconds";

        // Give it in days
        if (time >= 86_400_000) {
            time /= 86_400_000;
            unit = " days";
        }
        // Give it in hours
        else if (time >= 3_600_000) {
            time /= 3_600_000;
            unit = " hours";
        }
        // Give it in minutes
        else if (time >= 60_000) {
            time /= 60_000;
            unit = " minutes";
        }
        // Give it in seconds
        else if (time >= 1000) {
            time /= 1000;
            unit = " seconds";
        }

        // Print out the final time
        System.out.println("\n\nFinished in " + decimalFormat.format(time) + unit);
    }
}