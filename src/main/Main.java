package main;

import solutions.Problem4Part2_EFFICIENT;

import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            //new Problem1Part1();
            //new Problem1Part2();
            //new Problem2Part1();
            //new Problem2Part2();
            //new Problem3Part1_MESSY();
            //new Problem3Part1();
            //new Problem3Part2();
            //new Problem4Part1();
            //new Problem4Part2();
            new Problem4Part2_EFFICIENT();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        timeToRun(startTime, endTime);
    }

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
        if (time >= 3_600_000) {
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