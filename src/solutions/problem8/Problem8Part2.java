package solutions.problem8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Solution to problem eight, part two of Advent of Code.
 * https://adventofcode.com/2023/day/8
 *
 * Answer is
 */
public class Problem8Part2 {
    private final Map<String, Branches> nodes = new HashMap<>();
    private final ArrayList<String> currentNodes = new ArrayList<>();
    private char[] directions;

    private long totalSteps = 0;

    /** The different branching paths a node can take */
    public static class Branches {
        private final String leftPath;
        private final String rightPath;

        public Branches(String leftPath, String rightPath) {
            this.leftPath = leftPath;
            this.rightPath = rightPath;
        }

        public String getPath(char choice) {
            if (choice == 'L'){
                return leftPath;
            } else {
                return rightPath;
            }
        }
    }

    /** Constructor */
    public Problem8Part2() {
        try {
            File input = new File("resources/Problem8Input.txt");
            Scanner scanner = new Scanner(input);

            // Read the directions on what paths to take
            directions = scanner.nextLine().toCharArray();
            System.out.println("Directions:");
            System.out.println(directions);
            System.out.println();

            // Read all the nodes
            System.out.printf("%-10s | %-12s | %-12s%n", "Nodes", "Left Branch", "Right Branch");
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" = ");
                if (line.length == 1) {
                    continue;
                }

                // Add the starting nodes to the current node
                if (line[0].charAt(2) == 'A') {
                    currentNodes.add(line[0]);
                }

                // Put the node and its branches in the map
                String[] branches = line[1].split(", ");
                String leftBranch = branches[0].substring(1);
                String rightBranch = branches[1].substring(0, branches[1].length()-1);
                nodes.put(line[0], new Branches(leftBranch, rightBranch));
                System.out.printf("%-10s | %-12s | %-12s%n", line[0], leftBranch, rightBranch);
            }
            System.out.println();

            // Do the math to calculate how many steps are needed
            calculateSteps();

            // Go through the nodes according to the directions until reaching ZZZ
            //reachGoal();

            System.out.println("\n\nTotal Steps: " + totalSteps);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /** Calculate how many steps are needed for all nodes to simultaneously reach their target Z */
    private void calculateSteps() {
        // Find how many steps each starting node requires to reach their specific Z
        // Incidentally, they'll start back at the beginning after reaching their Z if you take another step.
        // Found that out while trying to see if I could use the loop values to help find my totalSteps.
        int[] stepsForEach = new int[currentNodes.size()];
        for (int i=0; i < currentNodes.size(); i++) {
            //System.out.printf("%-10s | %-10s | %-10s | %-10s%n", "Step", "Node", "Direction", "New Node");
            while (currentNodes.get(i).charAt(2) != 'Z') {
                char currentDirection = directions[stepsForEach[i] % directions.length];
                //System.out.printf("%-10s | %-10s | %-10s", stepsForEach[i], currentNodes.get(i), currentDirection);

                currentNodes.set(i, nodes.get(currentNodes.get(i)).getPath(currentDirection));
                //System.out.printf(" | %-10s%n", currentNodes.get(i));

                stepsForEach[i]++;
            }
            //System.out.println();
        }

        System.out.println("Steps For Each: " + Arrays.toString(stepsForEach));
        totalSteps = leastCommonMultiple(stepsForEach);
    }

    /**
     * Find the least common multiple of the numbers in a given array
     * @param input the given array of numbers
     * @return the least common multiple
     */
    private long leastCommonMultiple(int[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) {
            result = leastCommonMultiple(result, input[i]);
        }
        return result;
    }

    /**
     * Find the least common multiple between two numbers
     * @param n1 first number
     * @param n2 second number
     * @return the least common multiple between the two numbers
     */
    private long leastCommonMultiple(long n1, long n2) {
        return n1 * (n2 / greatestCommonDivisor(n1, n2));
    }

    /**
     * Find the greatest common divisor between two numbers
     * @param n1 first number
     * @param n2 second number
     * @return the greatest common divisor between the two numbers
     */
    public long greatestCommonDivisor(long n1, long n2) {
        return (n2 == 0) ? n1 : greatestCommonDivisor(n2, n1 % n2);
    }
}
