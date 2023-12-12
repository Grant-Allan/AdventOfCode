package solutions.problem10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Solution to problem ten, part two of Advent of Code.
 * https://adventofcode.com/2023/day/10
 *
 * Answer is
 */
public class Problem10Part2_REWORK {
    private char[][] lines;
    private final ArrayList<int[]> pathHistory = new ArrayList<>();
    private final ArrayList<int[]> allEnclosedCoords = new ArrayList<>();
    private final ArrayList<int[]> rejectedCoords = new ArrayList<>();

    private class Pipe {
        private char pipeType;
        private int[] curCoords;
        private int[] prevCoords;

        public Pipe(char pipeType, int[] coords) {
            this.pipeType = pipeType;
            this.curCoords = coords;
        }

        public Pipe(char pipeType, int x, int y) {
            this.pipeType = pipeType;
            this.curCoords = new int[] { x, y };
        }

        public void setCurCoords(int[] curCoords) {
            this.prevCoords = this.curCoords;
            this.curCoords = curCoords;
            this.pipeType = lines[curCoords[0]][curCoords[1]];
        }

        public char getPipeType() {
            return pipeType;
        }

        public int[] getCurCoords() {
            return curCoords;
        }

        public int[] getPrevCoords() {
            return prevCoords;
        }
    }

    /** Constructor */
    public Problem10Part2_REWORK() {
        try {
            File input = new File("resources/Problem10Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            ArrayList<char[]> linesOccurences = new ArrayList<>();
            while (scanner.hasNextLine()) {
                char[] line = scanner.nextLine().toCharArray();
                linesOccurences.add(line);
                System.out.println(line);
            }
            lines = linesOccurences.toArray(new char[0][0]);
            System.out.println();

            // Find the pipe locations
            findPipe();

            // Find anything that isn't a pipe and check for if it's an enclosed area
            for (int i=0; i < lines.length; i++) {
                for (int j=0; j < lines[0].length; j++) {
                    // If the coordinate isn't a pipe location or previously rejected or already accepted,
                    // find the entire area it's connected to
                    int[] coords = new int[] { i, j };
                    if (!hasCoord(pathHistory, coords) &&
                        !hasCoord(rejectedCoords, coords) &&
                        !hasCoord(allEnclosedCoords, coords)) {
                        findEnclosedArea(coords);
                    }
                }
            }

            System.out.println("\n\nTotal: " + allEnclosedCoords.size());

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /** Find the entire path from start to start */
    private void findPipe() {
        // Find the location of S
        Pipe traverse = null;
        for (int x=0; x < lines.length; x++) {
            for (int y=0; y < lines[0].length; y++) {
                if (lines[x][y] == 'S') {
                    traverse = new Pipe(lines[x][y], x, y);
                }
            }
        }

        // Find all the pipe locations
        do {
            assert traverse != null;
            findNextPipe(traverse);
            pathHistory.add(traverse.getCurCoords());
        } while (traverse.getPipeType() != 'S');

        System.out.println();

        // Find the areas enclosed by the pipes
        // Find values one apart from each other on a single axis? Or one on both?
    }

    private void findNextPipe(Pipe pipe1) {
        // Given the pipe type, find the connection pipes
        int[] northCoords = new int[] { pipe1.getCurCoords()[0]-1, pipe1.getCurCoords()[1] };
        Pipe north = null;
        if ((northCoords[0] >= 0) && (northCoords[1] >= 0) &&
                (northCoords[0] < lines.length) && (northCoords[1] < lines[0].length)) {
            north = new Pipe(lines[northCoords[0]][northCoords[1]], northCoords);
        }

        int[] southCoords = new int[] { pipe1.getCurCoords()[0]+1, pipe1.getCurCoords()[1] };
        Pipe south = null;
        if ((southCoords[0] >= 0) && (southCoords[1] >= 0) &&
                (southCoords[0] < lines.length) && (southCoords[1] < lines[0].length)) {
            south = new Pipe(lines[southCoords[0]][southCoords[1]], southCoords);
        }

        int[] eastCoords = new int[] { pipe1.getCurCoords()[0], pipe1.getCurCoords()[1]+1 };
        Pipe east = null;
        if ((eastCoords[0] >= 0) && (eastCoords[1] >= 0) &&
                (eastCoords[0] < lines.length) && (eastCoords[1] < lines[0].length)) {
            east = new Pipe(lines[eastCoords[0]][eastCoords[1]], eastCoords);
        }

        int[] westCoords = new int[] { pipe1.getCurCoords()[0], pipe1.getCurCoords()[1]-1 };
        Pipe west = null;
        if ((westCoords[0] >= 0) && (westCoords[1] >= 0) &&
                (westCoords[0] < lines.length) && (westCoords[1] < lines[0].length)) {
            west = new Pipe(lines[westCoords[0]][westCoords[1]], westCoords);
        }

        switch (pipe1.getPipeType()) {
            case '|':
                if ((south != null) && ((north == null) ||
                        ((pipe1.getPrevCoords()[0] == north.getCurCoords()[0]) &&
                                (pipe1.getPrevCoords()[1] == north.getCurCoords()[1])))) {
                    pipe1.setCurCoords(south.getCurCoords());
                } else if (north != null) {
                    pipe1.setCurCoords(north.getCurCoords());
                }
                break;

            case '-':
                if ((west != null) && ((east == null) ||
                        ((pipe1.getPrevCoords()[0] == east.getCurCoords()[0]) &&
                                (pipe1.getPrevCoords()[1] == east.getCurCoords()[1])))) {
                    pipe1.setCurCoords(west.getCurCoords());
                } else if (east != null) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                break;

            case 'L':
                if ((north != null) && ((east == null) ||
                        ((pipe1.getPrevCoords()[0] == east.getCurCoords()[0]) &&
                                (pipe1.getPrevCoords()[1] == east.getCurCoords()[1])))) {
                    pipe1.setCurCoords(north.getCurCoords());
                } else if (east != null) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                break;

            case 'J':
                if ((north != null) && ((west == null) ||
                        ((pipe1.getPrevCoords()[0] == west.getCurCoords()[0]) &&
                                (pipe1.getPrevCoords()[1] == west.getCurCoords()[1])))) {
                    pipe1.setCurCoords(north.getCurCoords());
                } else if (west != null) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                break;

            case '7':
                if ((south != null) && ((west == null) ||
                        ((pipe1.getPrevCoords()[0] == west.getCurCoords()[0]) &&
                                (pipe1.getPrevCoords()[1] == west.getCurCoords()[1])))) {
                    pipe1.setCurCoords(south.getCurCoords());
                } else if (west != null) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                break;

            case 'F':
                if ((south != null) && ((east == null) ||
                        ((pipe1.getPrevCoords()[0] == east.getCurCoords()[0]) &&
                                (pipe1.getPrevCoords()[1] == east.getCurCoords()[1])))) {
                    pipe1.setCurCoords(south.getCurCoords());
                } else if (east != null) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                break;

            case 'S':
                if ((west != null) &&
                        ((west.getPipeType() == 'F') ||
                                (west.getPipeType() == '-') ||
                                (west.getPipeType() == 'L'))) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                else if ((east != null) &&
                        ((east.getPipeType() == '7') ||
                                (east.getPipeType() == '-') ||
                                (east.getPipeType() == 'J'))) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                else if ((north != null) &&
                        ((north.getPipeType() == '7') ||
                                (north.getPipeType() == '|') ||
                                (north.getPipeType() == 'F'))) {
                    pipe1.setCurCoords(north.getCurCoords());
                }
                else if (south != null) {
                    pipe1.setCurCoords(south.getCurCoords());
                }
                break;

            default:
                break;
        }
    }

    /**
     * Check to see if the given coordinate is a the list of coordinates
     * @param listOfCoords
     * @param incomingCoords
     * @return
     */
    private boolean hasCoord(ArrayList<int[]> listOfCoords, int[] incomingCoords) {
        for (int[] coords : listOfCoords) {
            if ((incomingCoords[0] == coords[0]) && (incomingCoords[1] == coords[1])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find all locations in the space contained by the pipe.
     * If it's an invalid location, add it to the rejects.
     * Otherwise, add it to allEnclosedSpaces
     * @param firstLoc
     */
    private void findEnclosedArea(int[] firstLoc) {
        // Create an arraylist of coords for the enclosed space
        ArrayList<int[]> enclosedCoords = new ArrayList<>();
        enclosedCoords.add(firstLoc);

        // Find all coords in the enclosed space
        for (int i=0; i < enclosedCoords.size(); i++) {
            checkSurroundings(enclosedCoords, enclosedCoords.get(i));
        }

        // Sort the coordinates by their x value, lowest to highest
        enclosedCoords.sort((Comparator<? super int[]>) (coords1, coords2) -> {
            // Compare y
            if (coords1[0] > coords2[0]) {
                return 1;
            } else if (coords1[0] < coords2[0]) {
                return -1;
            }

            // Compare x if y are the same
            if (coords1[1] > coords2[1]) {
                return 1;
            } else if (coords1[1] < coords2[1]) {
                return -1;
            }

            // Same location (not possible outcome)
            return 0;
        });

        // Print enclosed coords
        System.out.println("Enclosed Coords:");
        for (var coords : enclosedCoords) {
            System.out.println(Arrays.toString(coords));
        }

        if (preventEdges(enclosedCoords) && preventSpillage(enclosedCoords)) {
            System.out.println("Accepted!");
            allEnclosedCoords.addAll(enclosedCoords);
        } else {
            System.out.println("Rejected!");
            rejectedCoords.addAll(enclosedCoords);
        }
        System.out.println();
    }

    /**
     * Check the up, down, left, right spots around a coordinate
     * @param enclosedCoords
     * @param curLoc
     */
    private void checkSurroundings(ArrayList<int[]> enclosedCoords, int[] curLoc) {
        int[] north = new int[] { curLoc[0] - 1, curLoc[1] };
        if (!hasCoord(pathHistory, north) &&
            !hasCoord(enclosedCoords, north) &&
            (curLoc[0]-1 > 0)) {
            //System.out.printf("%-12s | %s%n", "North", Arrays.toString(north));
            enclosedCoords.add(north);
        }

        int[] south = new int[] { curLoc[0] + 1, curLoc[1] };
        if (!hasCoord(pathHistory, south) &&
            !hasCoord(enclosedCoords, south) &&
            (curLoc[0]+1 < lines.length)) {
            //System.out.printf("%-12s | %s%n", "South", Arrays.toString(south));
            enclosedCoords.add(south);
        }

        int[] east  = new int[] { curLoc[0], curLoc[1] + 1 };
        if (!hasCoord(pathHistory, east) &&
            !hasCoord(enclosedCoords, east) &&
            (curLoc[1]+1 < lines[0].length)) {
            //System.out.printf("%-12s | %s%n", "East", Arrays.toString(east));
            enclosedCoords.add(east);
        }

        int[] west  = new int[] { curLoc[0], curLoc[1] - 1 };
        if (!hasCoord(pathHistory, west) &&
            !hasCoord(enclosedCoords, west) &&
            (curLoc[1]-1 > 0)) {
            //System.out.printf("%-12s | %s%n", "West", Arrays.toString(west));
            enclosedCoords.add(west);
        }
    }

    /**
     * Make sure nothing reaches an edge
     * @param enclosedCoords
     * @return
     */
    private boolean preventEdges(ArrayList<int[]> enclosedCoords) {
        //System.out.println();
        for (var coord : enclosedCoords) {
            //System.out.println(Arrays.toString(coord));
            if ((coord[0] <= 0) ||
                (coord[1] <= 0) ||
                (coord[0] >= lines.length-1) ||
                (coord[1] >= lines[0].length-1) ||
                hasCoord(pathHistory, coord) ||
                hasCoord(allEnclosedCoords, coord)) {
                //System.out.println("Invalid location found! Removing this location set!");
                return false;
            }
        }
        //System.out.println("No invalid locations found!");
        return true;
    }

    /**
     * Check to make sure the enclosed space can't "leak"
     * @param enclosedCoords
     * @return
     */
    private boolean preventSpillage(ArrayList<int[]> enclosedCoords) {
        // Find the coords at the bottom of the enclosed space
        ArrayList<int[]> highestCoords = new ArrayList<>();
        for (var coords : enclosedCoords) {
            // If it's higher, remove previous ones and add these
            if (!highestCoords.isEmpty() && (highestCoords.get(0)[0] < coords[0])) {
                highestCoords.clear();
                highestCoords.add(coords);
            }
            // If it's the same, add it
            else if (!highestCoords.isEmpty() && highestCoords.get(0)[0] == coords[0]) {
                highestCoords.add(coords);
            }
            // If it's empty, just add
            else if (highestCoords.isEmpty()) {
                highestCoords.add(coords);
            }
        }

        // Sort the coordinates by their x value, lowest to highest
        highestCoords.sort((Comparator<? super int[]>) (coords1, coords2) -> {
            if (coords1[1] > coords2[1]) {
                return 1;
            } else if (coords1[1] < coords2[1]) {
                return -1;
            }
            return 0;
        });

        //System.out.println("Sorted coords");
        //for (var coords : highestCoords) {
        //    System.out.println(Arrays.toString(coords));
        //}
        //System.out.println("End sorted coords\n");

        // Check those coords to make sure there's no set of values where they could spill out
        for (int i=0; i < highestCoords.size(); i++) {
            int[] pipe0Coords = new int[] { highestCoords.get(i)[0]+1, highestCoords.get(i)[1]-1 };
            int[] pipe1Coords = new int[] { highestCoords.get(i)[0]+1, highestCoords.get(i)[1]   };
            int[] pipe2Coords = new int[] { highestCoords.get(i)[0]+1, highestCoords.get(i)[1]+1 };

            char pipe0 = 'o';
            char pipe1 = 'o';
            char pipe2 = 'o';

            if (pipe0Coords[0] < lines.length) {
                if (pipe0Coords[1] < lines[0].length) {
                    pipe0 = lines[pipe0Coords[0]][pipe0Coords[1]];
                }
                if (pipe1Coords[1] < lines[0].length) {
                    pipe1 = lines[pipe1Coords[0]][pipe1Coords[1]];
                }
                if (pipe2Coords[1] < lines[0].length) {
                    pipe2 = lines[pipe2Coords[0]][pipe2Coords[1]];
                }
            } else {
                return false;
            }

            if (!((hasCoord(pathHistory, pipe0Coords) || hasCoord(allEnclosedCoords, pipe0Coords)) &&
                  (hasCoord(pathHistory, pipe1Coords) || hasCoord(allEnclosedCoords, pipe1Coords)) &&
                  (hasCoord(pathHistory, pipe2Coords) || hasCoord(allEnclosedCoords, pipe2Coords)))) {
                return false;
            }

            if ((pipe0 == '7' && pipe1 == 'F') ||
                (pipe1 == '7' && pipe2 == 'F') &&
                    (!tracePipe(pipe0Coords, pipe1Coords) || !tracePipe(pipe1Coords, pipe2Coords))) {
                return false;
            }

            if ((pipe0 == '7' && pipe1 == '|') ||
                (pipe1 == '7' && pipe2 == '|') &&
                    (!tracePipe(pipe0Coords, pipe1Coords) || !tracePipe(pipe1Coords, pipe2Coords))) {
                return false;
            }

            if ((pipe0 == '7' && pipe1 == 'L') ||
                (pipe1 == '7' && pipe2 == 'L') &&
                    (!tracePipe(pipe0Coords, pipe1Coords) || !tracePipe(pipe1Coords, pipe2Coords))) {
                return false;
            }

            if ((pipe0 == '|' && pipe1 == 'F') ||
                (pipe1 == '|' && pipe2 == 'F') &&
                    (!tracePipe(pipe0Coords, pipe1Coords) || !tracePipe(pipe1Coords, pipe2Coords))) {
                return false;
            }

            if ((pipe0 == 'J' && pipe2 == 'F') ||
                (pipe1 == 'J' && pipe2 == 'F') &&
                    (!tracePipe(pipe0Coords, pipe1Coords) || !tracePipe(pipe1Coords, pipe2Coords))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trace a potential leak to see if it catches itself
     * @param loc1
     * @param loc2
     * @return returns true for if it does, false for if not
     */
    private boolean tracePipe(int[] loc1, int[] loc2) {
        do {
            char pipe1 = lines[loc1[0]][loc1[1]];
            char pipe2 = lines[loc2[0]][loc2[1]];

            if ((pipe1 == 'L' && pipe2 == 'J') ||
                (pipe1 == 'L' && pipe2 == '-') ||
                (pipe1 == '_' && pipe2 == '-') ||
                (pipe1 == '_' && pipe2 == 'J')) {
                return true;
            }

            loc1[0]++;
            loc1[1]++;
        } while (loc1[0] < lines.length);
        return false;
    }
}
