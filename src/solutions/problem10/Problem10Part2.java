package solutions.problem10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Solution to problem ten, part two of Advent of Code.
 * https://adventofcode.com/2023/day/10
 *
 * Answer is 477
 */
public class Problem10Part2 {
    private char[][] lines;
    private ArrayList<int[]> pathHistory = new ArrayList<>();
    private final ArrayList<int[]> allEnclosedCoordinates = new ArrayList<>();
    private final ArrayList<int[]> rejectedCoordinates = new ArrayList<>();

    private class Pipe {
        private char pipeType;
        private int[] curCoordinates;
        private int[] prevCoordinates;

        public Pipe(char pipeType, int[] coordinates) {
            this.pipeType = pipeType;
            this.curCoordinates = coordinates;
        }

        public Pipe(char pipeType, int x, int y) {
            this.pipeType = pipeType;
            this.curCoordinates = new int[] { x, y };
        }

        public void setCurCoordinates(int[] curCoordinates) {
            this.prevCoordinates = this.curCoordinates;
            this.curCoordinates = curCoordinates;
            this.pipeType = lines[curCoordinates[0]][curCoordinates[1]];
        }

        public char getPipeType() {
            return pipeType;
        }

        public int[] getCurCoordinates() {
            return curCoordinates;
        }

        public int[] getPrevCoordinates() {
            return prevCoordinates;
        }
    }

    /** Constructor */
    public Problem10Part2() {
        try {
            File input = new File("resources/Problem10Input.txt");
            Scanner scanner = new Scanner(input);

            // Read all the lines
            ArrayList<char[]> linesOccurences = new ArrayList<>();
            while (scanner.hasNextLine()) {
                char[] line = scanner.nextLine().toCharArray();
                linesOccurences.add(line);
            }
            lines = linesOccurences.toArray(new char[0][0]);

            // Find the pipe locations
            findPipe();

            // Expand the lines so cracks are easier to find
            char[][] newLines = new char[lines.length*2][lines[0].length*2];
            for (int i=0; i < newLines.length-1; i++) {
                for (int j=0; j < newLines[0].length-1; j++) {
                    // Is previously empty space
                    if ((i%2 == 0) && (j%2 == 0)) {
                        newLines[i][j] = '.';
                    }
                    // Is newly added
                    else {
                        newLines[i][j] = '*';
                    }
                }
            }

            // Put the path in
            for (int i=0; i < pathHistory.size()-1; i++) {
                int[] coord1 = pathHistory.get(i);
                int[] coord2 = pathHistory.get(i+1);

                // Find the direction of movement
                int[] direction = new int[] { coord2[0]-coord1[0], coord2[1]-coord1[1] };

                // Place pipeline
                newLines[(coord1[0]*2)][(coord1[1]*2)] = lines[coord1[0]][coord1[1]];

                if (direction[0] != 0) {
                    newLines[(coord1[0]*2)+direction[0]][(coord1[1]*2)+direction[1]] = '|';
                } else {
                    newLines[(coord1[0]*2)+direction[0]][(coord1[1]*2)+direction[1]] = '-';
                }
            }
            lines = newLines;


            for (int i=0; i < lines.length-1; i++) {
                for (int j = 0; j < lines[0].length - 1; j++) {
                    System.out.print(lines[i][j]);
                }
                System.out.println();
            }

            // Find the pipe locations
            findPipe();

            // Find anything that isn't a pipe and check for if it's an enclosed area
            for (int i=lines.length-1; i > 0; i--) {
                for (int j=lines[0].length-1; j > 0; j--) {
                    // If the coordinate isn't a pipe location or previously rejected or already accepted,
                    // find the entire area it's connected to
                    int[] coordinates = new int[] { i, j };
                    if (!hasCoord(pathHistory, coordinates) &&
                            !hasCoord(rejectedCoordinates, coordinates) &&
                            !hasCoord(allEnclosedCoordinates, coordinates)) {
                        findEnclosedArea(coordinates);
                    }
                }
            }

            // Sort the coordinates by their x value, lowest to highest
            allEnclosedCoordinates.sort((Comparator<? super int[]>) (coordinates1, coordinates2) -> {
                // Compare y
                if (coordinates1[0] > coordinates2[0]) {
                    return 1;
                } else if (coordinates1[0] < coordinates2[0]) {
                    return -1;
                }

                // Compare x if y are the same
                if (coordinates1[1] > coordinates2[1]) {
                    return 1;
                } else if (coordinates1[1] < coordinates2[1]) {
                    return -1;
                }

                return 0;
            });

            System.out.println("\n\nTotal: " + allEnclosedCoordinates.size());

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /** Find the entire path from start to start */
    private void findPipe() {
        pathHistory.clear();

        // Find the location of S
        Pipe traverse = null;
        for (int x=0; x < lines.length; x++) {
            for (int y=0; y < lines[0].length; y++) {
                if (lines[x][y] == 'S') {
                    traverse = new Pipe(lines[x][y], x, y);
                }
            }
        }

        // Add S
        assert traverse != null;
        pathHistory.add(traverse.getCurCoordinates());

        // Find all the pipe locations
        do {
            findNextPipe(traverse);
            pathHistory.add(traverse.getCurCoordinates());
        } while (traverse.getPipeType() != 'S');
    }

    private void findNextPipe(Pipe pipe1) {
        // Given the pipe type, find the connection pipes
        int[] northCoordinates = new int[] { pipe1.getCurCoordinates()[0]-1, pipe1.getCurCoordinates()[1] };
        Pipe north = null;
        if ((northCoordinates[0] >= 0) && (northCoordinates[1] >= 0) &&
                (northCoordinates[0] < lines.length) && (northCoordinates[1] < lines[0].length)) {
            north = new Pipe(lines[northCoordinates[0]][northCoordinates[1]], northCoordinates);
        }

        int[] southCoordinates = new int[] { pipe1.getCurCoordinates()[0]+1, pipe1.getCurCoordinates()[1] };
        Pipe south = null;
        if ((southCoordinates[0] >= 0) && (southCoordinates[1] >= 0) &&
                (southCoordinates[0] < lines.length) && (southCoordinates[1] < lines[0].length)) {
            south = new Pipe(lines[southCoordinates[0]][southCoordinates[1]], southCoordinates);
        }

        int[] eastCoordinates = new int[] { pipe1.getCurCoordinates()[0], pipe1.getCurCoordinates()[1]+1 };
        Pipe east = null;
        if ((eastCoordinates[0] >= 0) && (eastCoordinates[1] >= 0) &&
                (eastCoordinates[0] < lines.length) && (eastCoordinates[1] < lines[0].length)) {
            east = new Pipe(lines[eastCoordinates[0]][eastCoordinates[1]], eastCoordinates);
        }

        int[] westCoordinates = new int[] { pipe1.getCurCoordinates()[0], pipe1.getCurCoordinates()[1]-1 };
        Pipe west = null;
        if ((westCoordinates[0] >= 0) && (westCoordinates[1] >= 0) &&
                (westCoordinates[0] < lines.length) && (westCoordinates[1] < lines[0].length)) {
            west = new Pipe(lines[westCoordinates[0]][westCoordinates[1]], westCoordinates);
        }

        switch (pipe1.getPipeType()) {
            case '|':
                if ((south != null) && ((north == null) ||
                        ((pipe1.getPrevCoordinates()[0] == north.getCurCoordinates()[0]) &&
                                (pipe1.getPrevCoordinates()[1] == north.getCurCoordinates()[1])))) {
                    pipe1.setCurCoordinates(south.getCurCoordinates());
                } else if (north != null) {
                    pipe1.setCurCoordinates(north.getCurCoordinates());
                }
                break;

            case '-':
                if ((west != null) && ((east == null) ||
                        ((pipe1.getPrevCoordinates()[0] == east.getCurCoordinates()[0]) &&
                                (pipe1.getPrevCoordinates()[1] == east.getCurCoordinates()[1])))) {
                    pipe1.setCurCoordinates(west.getCurCoordinates());
                } else if (east != null) {
                    pipe1.setCurCoordinates(east.getCurCoordinates());
                }
                break;

            case 'L':
                if ((north != null) && ((east == null) ||
                        ((pipe1.getPrevCoordinates()[0] == east.getCurCoordinates()[0]) &&
                                (pipe1.getPrevCoordinates()[1] == east.getCurCoordinates()[1])))) {
                    pipe1.setCurCoordinates(north.getCurCoordinates());
                } else if (east != null) {
                    pipe1.setCurCoordinates(east.getCurCoordinates());
                }
                break;

            case 'J':
                if ((north != null) && ((west == null) ||
                        ((pipe1.getPrevCoordinates()[0] == west.getCurCoordinates()[0]) &&
                                (pipe1.getPrevCoordinates()[1] == west.getCurCoordinates()[1])))) {
                    pipe1.setCurCoordinates(north.getCurCoordinates());
                } else if (west != null) {
                    pipe1.setCurCoordinates(west.getCurCoordinates());
                }
                break;

            case '7':
                if ((south != null) && ((west == null) ||
                        ((pipe1.getPrevCoordinates()[0] == west.getCurCoordinates()[0]) &&
                                (pipe1.getPrevCoordinates()[1] == west.getCurCoordinates()[1])))) {
                    pipe1.setCurCoordinates(south.getCurCoordinates());
                } else if (west != null) {
                    pipe1.setCurCoordinates(west.getCurCoordinates());
                }
                break;

            case 'F':
                if ((south != null) && ((east == null) ||
                        ((pipe1.getPrevCoordinates()[0] == east.getCurCoordinates()[0]) &&
                                (pipe1.getPrevCoordinates()[1] == east.getCurCoordinates()[1])))) {
                    pipe1.setCurCoordinates(south.getCurCoordinates());
                } else if (east != null) {
                    pipe1.setCurCoordinates(east.getCurCoordinates());
                }
                break;

            case 'S':
                if ((west != null) &&
                        ((west.getPipeType() == 'F') ||
                                (west.getPipeType() == '-') ||
                                (west.getPipeType() == 'L'))) {
                    pipe1.setCurCoordinates(west.getCurCoordinates());
                }
                else if ((east != null) &&
                        ((east.getPipeType() == '7') ||
                                (east.getPipeType() == '-') ||
                                (east.getPipeType() == 'J'))) {
                    pipe1.setCurCoordinates(east.getCurCoordinates());
                }
                else if ((north != null) &&
                        ((north.getPipeType() == '7') ||
                                (north.getPipeType() == '|') ||
                                (north.getPipeType() == 'F'))) {
                    pipe1.setCurCoordinates(north.getCurCoordinates());
                }
                else if (south != null) {
                    pipe1.setCurCoordinates(south.getCurCoordinates());
                }
                break;

            default:
                break;
        }
    }

    /**
     * Check to see if the given coordinate is in the list of coordinates
     * @param listOfCoordinates the coordinates being checked against
     * @param incomingCoordinates the coordinates being checked
     * @return true for yes, false for no
     */
    private boolean hasCoord(ArrayList<int[]> listOfCoordinates, int[] incomingCoordinates) {
        for (int[] coordinates : listOfCoordinates) {
            if ((incomingCoordinates[0] == coordinates[0]) && (incomingCoordinates[1] == coordinates[1])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find all locations in the space contained by the pipe.
     * If it's an invalid location, add it to the rejects.
     * Otherwise, add it to allEnclosedSpaces
     * @param firstLoc the first location in the enclosed area
     */
    private void findEnclosedArea(int[] firstLoc) {
        // Create an arraylist of coordinates for the enclosed space
        ArrayList<int[]> enclosedCoordinates = new ArrayList<>();
        enclosedCoordinates.add(firstLoc);

        // Find all coordinates in the enclosed space
        for (int i=0; i < enclosedCoordinates.size(); i++) {
            checkSurroundings(enclosedCoordinates, enclosedCoordinates.get(i));
        }

        // Sort the coordinates by their x value, lowest to highest
        enclosedCoordinates.sort((Comparator<? super int[]>) (coordinates1, coordinates2) -> {
            // Compare y
            if (coordinates1[0] > coordinates2[0]) {
                return 1;
            } else if (coordinates1[0] < coordinates2[0]) {
                return -1;
            }

            // Compare x if y are the same
            if (coordinates1[1] > coordinates2[1]) {
                return 1;
            } else if (coordinates1[1] < coordinates2[1]) {
                return -1;
            }

            // Same location (not possible outcome)
            return 0;
        });

        if (preventEdges(enclosedCoordinates)) {
            enclosedCoordinates.removeIf(coordinates -> (lines[coordinates[0]][coordinates[1]] == '*'));
            allEnclosedCoordinates.addAll(enclosedCoordinates);
        } else {
            rejectedCoordinates.addAll(enclosedCoordinates);
        }
    }

    /**
     * Check the up, down, left, right spots around a coordinate
     * @param enclosedCoordinates the area the coordinates are added to
     * @param curLoc the current location being checked for space around it
     */
    private void checkSurroundings(ArrayList<int[]> enclosedCoordinates, int[] curLoc) {
        int[] north = new int[] { curLoc[0] - 1, curLoc[1] };
        if (!hasCoord(pathHistory, north) &&
                !hasCoord(enclosedCoordinates, north) &&
                (curLoc[0]-1 > 0)) {
            enclosedCoordinates.add(north);
        }

        int[] south = new int[] { curLoc[0] + 1, curLoc[1] };
        if (!hasCoord(pathHistory, south) &&
                !hasCoord(enclosedCoordinates, south) &&
                (curLoc[0]+1 < lines.length)) {
            enclosedCoordinates.add(south);
        }

        int[] east  = new int[] { curLoc[0], curLoc[1] + 1 };
        if (!hasCoord(pathHistory, east) &&
                !hasCoord(enclosedCoordinates, east) &&
                (curLoc[1]+1 < lines[0].length)) {
            enclosedCoordinates.add(east);
        }

        int[] west  = new int[] { curLoc[0], curLoc[1] - 1 };
        if (!hasCoord(pathHistory, west) &&
                !hasCoord(enclosedCoordinates, west) &&
                (curLoc[1]-1 > 0)) {
            enclosedCoordinates.add(west);
        }
    }

    /**
     * Make sure nothing reaches an edge
     * @param enclosedCoordinates the list of coordinates being checked for edge values
     * @return true for if there's no edges found, false for if there's at least one
     */
    private boolean preventEdges(ArrayList<int[]> enclosedCoordinates) {
        for (var coord : enclosedCoordinates) {
            if ((coord[0] <= 0) ||
                    (coord[1] <= 0) ||
                    (coord[0] >= lines.length-1) ||
                    (coord[1] >= lines[0].length-1) ||
                    hasCoord(pathHistory, coord) ||
                    hasCoord(allEnclosedCoordinates, coord)) {
                return false;
            }
        }
        return true;
    }
}
