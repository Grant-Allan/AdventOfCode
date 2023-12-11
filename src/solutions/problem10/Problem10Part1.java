package solutions.problem10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Solution to problem ten, part one of Advent of Code.
 * https://adventofcode.com/2023/day/10
 *
 * Answer is 6599
 */
public class Problem10Part1 {
    private char[][] lines;

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
    public Problem10Part1() {
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

            findFurthestPoint();

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void findFurthestPoint() {
        // Find the location of S
        Pipe traverse1 = null;
        Pipe traverse2 = null;
        for (int x=0; x < lines.length; x++) {
            for (int y=0; y < lines[0].length; y++) {
                if (lines[x][y] == 'S') {
                    traverse1 = new Pipe(lines[x][y], x, y);
                    traverse2 = new Pipe(lines[x][y], x, y);
                }
            }
        }

        // Follow the pipes from it, following both ends until they meet
        int steps = 0;
        do {
            assert traverse1 != null;
            findNextPipe(traverse1, traverse2);
            findNextPipe(traverse2, traverse1);
            steps++;
            System.out.println("\ntraverse1: " + traverse1.getPipeType() + " " + Arrays.toString(traverse1.getCurCoords()));
            System.out.println("traverse2: " + traverse2.getPipeType() + " " + Arrays.toString(traverse2.getCurCoords()));
        } while (!((traverse1.getCurCoords()[0] == traverse2.getCurCoords()[0]) &&
                   (traverse1.getCurCoords()[1] == traverse2.getCurCoords()[1])));
        System.out.println("\nSteps: " + steps);
    }

    private void findNextPipe(Pipe pipe1, Pipe pipe2) {
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
                    (((west.getPipeType() == 'F') ||
                      (west.getPipeType() == '-') ||
                      (west.getPipeType() == 'L')) &&
                      (!((westCoords[0] == pipe2.getCurCoords()[0]) &&
                         (westCoords[1] == pipe2.getCurCoords()[1]))))) {
                    pipe1.setCurCoords(west.getCurCoords());
                }
                else if ((east != null) &&
                        (((east.getPipeType() == '7') ||
                          (east.getPipeType() == '-') ||
                          (east.getPipeType() == 'J')) &&
                          (!((eastCoords[0] == pipe2.getCurCoords()[0]) &&
                             (eastCoords[1] == pipe2.getCurCoords()[1]))))) {
                    pipe1.setCurCoords(east.getCurCoords());
                }
                else if ((north != null) &&
                         (((north.getPipeType() == '7') ||
                           (north.getPipeType() == '|') ||
                           (north.getPipeType() == 'F')) &&
                           (!((northCoords[0] == pipe2.getCurCoords()[0]) &&
                              (northCoords[1] == pipe2.getCurCoords()[1]))))) {
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
}
