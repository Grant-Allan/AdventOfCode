package solutions.problem8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Solution to problem eight, part one of Advent of Code.
 * https://adventofcode.com/2023/day/8
 *
 * Answer is 13771
 */
public class Problem8Part1 {
    private final Map<String, Branches> nodes = new HashMap<>();
    private int totalSteps = 0;
    private static final String GOAL = "ZZZ";

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
    public Problem8Part1() {
        try {
            File input = new File("resources/Problem8Input.txt");
            Scanner scanner = new Scanner(input);

            // Read the directions on what paths to take
            char[] directions = scanner.nextLine().toCharArray();
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

                // Put the node and its branches in the map
                String[] branches = line[1].split(", ");
                String leftBranch = branches[0].substring(1);
                String rightBranch = branches[1].substring(0, branches[1].length()-1);
                nodes.put(line[0], new Branches(leftBranch, rightBranch));
                System.out.printf("%-10s | %-12s | %-12s%n", line[0], leftBranch, rightBranch);
            }
            System.out.println();

            // Go through the nodes according to the directions until reaching ZZZ
            reachGoal(directions);

            System.out.println("\n\nTotal Steps: " + totalSteps);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Walk through the steps needed to reach the goal
     * @param directions the directions for each step
     */
    private void reachGoal(char[] directions) {
        System.out.printf("%-10s | %-10s | %-10s | %-10s%n", "Step", "Node", "Direction", "New Node");

        String currentNode = "AAA";
        while (!currentNode.equals(GOAL)) {
            char currentDirection = directions[totalSteps % directions.length];
            System.out.printf("%-10s | %-10s | %-10s", totalSteps, currentNode, currentDirection);

            currentNode = nodes.get(currentNode).getPath(currentDirection);
            System.out.printf(" | %-10s%n", currentNode);

            totalSteps++;
        }
    }
}
