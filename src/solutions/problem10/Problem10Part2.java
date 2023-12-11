package solutions.problem10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solution to problem ten, part two of Advent of Code.
 * https://adventofcode.com/2023/day/10
 *
 * Answer is
 */
public class Problem10Part2 {
    private char[][] lines;
    private final ArrayList<int[]> pathHistory = new ArrayList<>();
    private final ArrayList<int[]> allEnclosedCoords = new ArrayList<>();

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
    public Problem10Part2() {
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

            findFurthestPoint();
            findEnclosedAreas();

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void findFurthestPoint() {
        // Find the location of S
        Pipe traverse = null;
        for (int x=0; x < lines.length; x++) {
            for (int y=0; y < lines[0].length; y++) {
                if (lines[x][y] == 'S') {
                    traverse = new Pipe(lines[x][y], x, y);
                }
            }
        }
        assert traverse != null;
        pathHistory.add(traverse.getCurCoords());

        // Find all the pipe locations
        do {
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

    private void findEnclosedAreas() {
        for (int i = 1; i < pathHistory.size()-1; i++) {
            int[] enclosedSpace = isAdjacent(pathHistory.get(i-1), pathHistory.get(i), pathHistory.get(i+1));

            if (enclosedSpace.length > 0) {
                findEnclosedSpace(enclosedSpace);
            }
        }
        System.out.println("\nTOTAL: " + allEnclosedCoords.size());
    }

    /**
     * Check to see if there's an enclosed space (or potential one)
     * @param prevLoc the location in the path you just came from
     * @param curLoc the current location in the path
     * @param nextLoc the location in the path you'll move to next
     * @return the first enclosed space location
     */
    private int[] isAdjacent(int[] prevLoc, int[] curLoc, int[] nextLoc) {
        for (int[] loc : pathHistory) {
            // Make sure it isn't the previous or next location in the pipe
            boolean isPrevLoc = ((loc[0] == prevLoc[0]) && (loc[1] == prevLoc[1]));
            boolean isNextLoc = ((loc[0] == nextLoc[0]) && (loc[1] == nextLoc[1]));

            // Each side
            boolean northAdj = ((loc[0] == curLoc[0]-1) && (loc[1] == curLoc[1]))   && !isPrevLoc && !isNextLoc;
            boolean southAdj = ((loc[0] == curLoc[0]+1) && (loc[1] == curLoc[1]))   && !isPrevLoc && !isNextLoc;
            boolean eastAdj  = ((loc[0] == curLoc[0])   && (loc[1] == curLoc[1]+1)) && !isPrevLoc && !isNextLoc;
            boolean westAdj  = ((loc[0] == curLoc[0])   && (loc[1] == curLoc[1]-1)) && !isPrevLoc && !isNextLoc;

            // Diagonals
            boolean northEastAdj = ((loc[0] == curLoc[0]-1) && (loc[1] == curLoc[1]+1)) && !isPrevLoc && !isNextLoc;
            boolean northWestAdj = ((loc[0] == curLoc[0]-1) && (loc[1] == curLoc[1]-1)) && !isPrevLoc && !isNextLoc;
            boolean southEastAdj = ((loc[0] == curLoc[0]+1) && (loc[1] == curLoc[1]+1)) && !isPrevLoc && !isNextLoc;
            boolean southWestAdj = ((loc[0] == curLoc[0]+1) && (loc[1] == curLoc[1]-1)) && !isPrevLoc && !isNextLoc;

            if (northAdj) {
                return new int[] { curLoc[0]+1, curLoc[1] };
            } else if (southAdj) {
                return new int[] { curLoc[0]-1, curLoc[1] };
            } else if (eastAdj) {
                return new int[] { curLoc[0], curLoc[1]-1 };
            } else if (westAdj) {
                return new int[] { curLoc[0], curLoc[1]+1 };
            } else if (northEastAdj) {
                return new int[] { curLoc[0]-1, curLoc[1]+1 };
            } else if (northWestAdj) {
                return new int[] { curLoc[0]-1, curLoc[1]-1 };
            } else if (southEastAdj) {
                return new int[] { curLoc[0]+1, curLoc[1]+1 };
            } else if (southWestAdj) {
                return new int[] { curLoc[0]+1, curLoc[1]-1 };
            }
        }
        return new int[0];
    }

    private void findEnclosedSpace(int[] firstLoc) {
        if (hasCoord(allEnclosedCoords, firstLoc) ||
                (firstLoc[0] <= 0) ||
                (firstLoc[1] <= 0) ||
                (firstLoc[0] >= lines.length-1) ||
                (firstLoc[1] >= lines[0].length-1) ||
                (lines[firstLoc[0]][firstLoc[1]] == '|') ||
                (lines[firstLoc[0]][firstLoc[1]] == '-')) {
            return;
        }

        // Create an arraylist of coords for the enclosed space
        ArrayList<int[]> enclosedCoords = new ArrayList<>();
        enclosedCoords.add(firstLoc);

        // Can do some sort of check by finding all the path values surrounding the space
        // Get whichever is sooner in the path between the two adjacent spots you found,
        // then run through the path while recording locations until you reach the later one.
        // Sort by most south coords. Only save ones that have the most south value.
        // If it's just one, it can't spill. If it's two or more, check to see if a
        // ['7','F'] or ['J','L'] set exists. If it does, it's not an enclosed space.

        // Find all coords in the enclosed space
        while (true) {
            // Get the locations in each direction
            int[] curLoc = enclosedCoords.get(enclosedCoords.size()-1);
            int[] north = new int[] { curLoc[0] - 1, curLoc[1] };
            int[] south = new int[] { curLoc[0] + 1, curLoc[1] };
            int[] east  = new int[] { curLoc[0], curLoc[1] + 1 };
            int[] west  = new int[] { curLoc[0], curLoc[1] - 1 };
            System.out.println(Arrays.toString(curLoc));
            System.out.println(Arrays.toString(north));
            System.out.println(Arrays.toString(south));
            System.out.println(Arrays.toString(east));
            System.out.println(Arrays.toString(west));

            boolean noEnclosedSpace = true;
            if (!hasCoord(pathHistory, north) &&
                !hasCoord(allEnclosedCoords, north) &&
                !hasCoord(enclosedCoords, north) &&
                (curLoc[0]-1 > 0)) {
                System.out.println("North - adding " + Arrays.toString(north));
                enclosedCoords.add(north);
                noEnclosedSpace = false;
            }
            if (!hasCoord(pathHistory, south) &&
                !hasCoord(allEnclosedCoords, south) &&
                !hasCoord(enclosedCoords, south) &&
                (curLoc[0]+1 < lines.length-1)) {
                System.out.println("South - adding " + Arrays.toString(south));
                enclosedCoords.add(south);
                noEnclosedSpace = false;
            }
            if (!hasCoord(pathHistory, east) &&
                !hasCoord(allEnclosedCoords, east) &&
                !hasCoord(enclosedCoords, east) &&
                (curLoc[1]+1 < lines.length-1)) {
                System.out.println("East  - adding " + Arrays.toString(east));
                enclosedCoords.add(east);
                noEnclosedSpace = false;
            }
            if (!hasCoord(pathHistory, west) &&
                !hasCoord(allEnclosedCoords, west) &&
                !hasCoord(enclosedCoords, west) &&
                (curLoc[1]-1 > 0)) {
                System.out.println("West  - adding " + Arrays.toString(west));
                enclosedCoords.add(west);
                noEnclosedSpace = false;
            }
            if (noEnclosedSpace) {
                System.out.println("No enclosed space found!");
                break;
            }
            System.out.println();
        }

        // Make sure none are outside the bounds. If any is, don't add the set.
        boolean valid = true;
        for (var coord : enclosedCoords) {
            // Make sure it's in bounds
            if ((coord[0] <= 0) ||
                (coord[1] <= 0) ||
                (coord[0] >= lines.length-1) ||
                (coord[1] >= lines[0].length-1)) {
                //System.out.println("Invalid location found! Removing previous location set!");
                valid = false;
                break;
            }
        }
        if (valid) {
            allEnclosedCoords.addAll(enclosedCoords);
        }
        System.out.println();
    }

    private boolean hasCoord(ArrayList<int[]> listOfCoords, int[] incomingCoords) {
        for (int[] coords : listOfCoords) {
            if ((incomingCoords[0] == coords[0]) && (incomingCoords[1] == coords[1])) {
                return true;
            }
        }
        return false;
    }
}
