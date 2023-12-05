package solutions.problem5;

import helpers.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solution to problem five, part 1 of Advent of Code.
 * https://adventofcode.com/2023/day/5
 *
 * Answer is 214922730
 */
public class Problem5Part1 {
    /** The initial seeds found */
    private final ArrayList<Long> SEEDS = new ArrayList<>();

    /** Conversions list */
    private final ArrayList<ArrayList<CONVERTER>> ALL_CONVERSIONS = new ArrayList<>();

    /** Convert source values to destination values */
    public class CONVERTER {
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
         * Get the minimum value of the destination range
         * @return the minimum destination value
         */
        public long getMinDest() {
            return dest;
        }

        /**
         * Get the upper end of the destination range
         * @return the maximum destination value
         */
        public long getMaxDest() {
            return (dest+range);
        }

        // Get the range value
        public long getRange() {
            return range;
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
    public Problem5Part1() {
        try {
            File input = new File("resources/Problem5Input.txt");
            Scanner scanner = new Scanner(input);

            // Process the first line
            String[] firstLine = scanner.nextLine().split(" ");
            for (String part : firstLine) {
                if (Helper.isNumeric(part)) {
                    SEEDS.add(Long.parseLong(part));
                }
            }

            // Process the rest of the file
            System.out.println("CREATING CONVERSIONS");
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }

            System.out.println("\n\n");
            System.out.println("CONVERTED VALUES");

            // Convert the seed values longo location values
            Long[] values = SEEDS.toArray(new Long[0]);
            System.out.println(Arrays.toString(values));
            for (ArrayList<CONVERTER> conversions : ALL_CONVERSIONS) {
                // Convert all the values
                for (int i=0; i < values.length; i++) {
                    for (CONVERTER converter : conversions) {
                        if (converter.inRange(values[i])) {
                            //System.out.println("    " + values[i] + ": " + converter.getMinSource() + " to " + converter.getMaxSource() + ", " + converter.getMinDest() + " to "  + getMaxDest());
                            values[i] = converter.convertValue(values[i]);
                            break;
                        }
                    }
                }
                System.out.println(Arrays.toString(values));
            }

            // Sort in ascending order and prlong the first element
            Arrays.sort(values);
            System.out.println("\n\nSMALLEST LOCATION VALUE: " + values[0]);

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
            ALL_CONVERSIONS.add(new ArrayList<>());
            return;
        }

        // Process the line
        String[] mapValues = line.split(" ");

        CONVERTER converter = new CONVERTER();
        converter.setDest(Long.parseLong(mapValues[0]));
        converter.setSource(Long.parseLong(mapValues[1]));
        converter.setRange(Long.parseLong(mapValues[2]));

        ALL_CONVERSIONS.get(ALL_CONVERSIONS.size()-1).add(converter);

        System.out.println(Arrays.toString(mapValues));

        //for (var p : ALL_CONVERSIONS.get(ALL_CONVERSIONS.size()-1)) {
        //    System.out.println("    " + p.getMinSource() + " " + p.getDest() + " " + p.getRange());
        //}
    }
}
