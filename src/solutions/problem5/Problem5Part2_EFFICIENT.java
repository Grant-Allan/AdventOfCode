package solutions.problem5;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Solution to problem five, part two of Advent of Code.
 * https://adventofcode.com/2023/day/5
 *
 * Answer is 148041808
 */
public class Problem5Part2_EFFICIENT {
    /** The current smallest location value */
    private long smallest = Long.MAX_VALUE;

    /** Conversions list */
    private final ArrayList<ArrayList<CONVERTER>> allConversions = new ArrayList<>();

    /** Convert source values to destination values */
    public static class CONVERTER {
        private long source = -1;
        private long dest = -1;
        private long range = -1;

        /**
         * Set the source value
         * @param source the source value
         */
        public void setSource(long source) {
            this.source = source;
        }

        /**
         * Set the destination value
         * @param dest the destination value
         */
        public void setDest(long dest) {
            this.dest = dest;
        }

        /**
         * Set the range of the conversion
         * @param range conversion range
         */
        public void setRange(long range) {
            this.range = --range;
        }

        /**
         * Get the minimum value of the source range
         * @return the minimum source value
         */
        public long getMinSource() {
            return source;
        }

        /**
         * Get the upper end of the source range
         * @return the maximum source value
         */
        public long getMaxSource() {
            return (source+range);
        }

        /**
         * Check to see if a value is in the source range
         * @param value value being checked
         * @return true for yes, false for no
         */
        public boolean inRange(long value) {
            return (value >= getMinSource()) && (value <= getMaxSource());
        }

        /**
         * Convert a given source value longo a destination value
         * @param value the value being converted
         * @return the converted value
         */
        public long convertValue(long value) {
            if (inRange(value)) {
                long diff = (value - getMinSource());
                return (this.dest + diff);
            } else {
                return value;
            }
        }
    }

    /** Constructor for Problem5Part1 */
    public Problem5Part2_EFFICIENT() {
        try {
            File input = new File("resources/Problem5Input.txt");
            Scanner scanner = new Scanner(input);

            // Process the first line
            String[] firstLine = scanner.nextLine().split(" ");
            ArrayList<Long> seeds = new ArrayList<>();
            for (String part : firstLine) {
                if (Helper.isNumeric(part)) {
                    seeds.add(Long.parseLong(part));
                }
            }

            // Process the rest of the file
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }

            // Process each seed, one at a time
            System.out.printf("%-10s | %-10s | %-25s | %-25s%n", "Seed", "Range", "Current Smallest Value", "New Smallest Value");
            for (int i = 0; i < seeds.size(); i+=2) {
                System.out.printf("%-10d | %-10d | %-25d", seeds.get(i), seeds.get(i+1), smallest);

                // Build the values array
                for (int j = 0; j < seeds.get(i+1); j++) {
                    processValue((seeds.get(i) + j));
                }
                System.out.printf(" | %-25d%n", smallest);
            }

            // Sort in ascending order and prlong the first element
            System.out.println("\nSMALLEST LOCATION VALUE: " + smallest);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Process the input line.
     * @param line line from the text file being processed
     */
    private void processLine(String line) {
        // Make sure it's not an empty string
        if (line.isEmpty() || line.isBlank()) {
            return;
        }

        // Check to see if it's the start of a new map
        // If it is, create a new converter for it
        if (line.contains("map")) {
            allConversions.add(new ArrayList<>());
            return;
        }

        // Process the line
        String[] mapValues = line.split(" ");

        CONVERTER converter = new CONVERTER();
        converter.setDest(Long.parseLong(mapValues[0]));
        converter.setSource(Long.parseLong(mapValues[1]));
        converter.setRange(Long.parseLong(mapValues[2]));

        allConversions.get(allConversions.size()-1).add(converter);
    }

    private void processValue(long value) {
        // Convert the values
        for (ArrayList<CONVERTER> conversions : allConversions) {
            // Convert the value
            for (CONVERTER converter : conversions) {
                if (converter.inRange(value)) {
                    value = converter.convertValue(value);
                    break;
                }
            }
        }

        // Check to see if it's smaller than the current smallest
        smallest = Math.min(value, smallest);
    }
}
