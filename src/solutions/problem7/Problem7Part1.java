package solutions.problem7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Solution to problem seven, part one of Advent of Code.
 * https://adventofcode.com/2023/day/7
 *
 * Answer is
 */
public class Problem7Part1 {
    private final ArrayList<Hand> allHands = new ArrayList<>();
    private int total = 0;

    private enum TYPES {
        FIVE_KIND(1),
        FOUR_KIND(2),
        FULL_HOUSE(3),
        THREE_KIND(4),
        TWO_PAIR(5),
        ONE_PAIR(6),
        HIGH_CARD(7);

        private final int rank;
        TYPES(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }
    }

    private class Hand {
        private final String cards;
        private final int bid;
        private TYPES type = TYPES.HIGH_CARD;

        public Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;

            findHandType();
            System.out.printf("%-10s | %-10d | %-10s%n", cards, bid, type);
        }

        public String getCards() {
            return cards;
        }

        public int getBid() {
            return bid;
        }

        public TYPES getType() {
            return type;
        }

        private void findHandType() {
            // Find how many instances there are of each char
            Map<Character, Integer> tracker = new HashMap<>();
            for (Character c : cards.toCharArray()) {
                if (tracker.containsKey(c)) {
                    tracker.put(c, (tracker.get(c)+1));
                } else {
                    tracker.put(c, 1);
                }
            }

            // Find how many repeats there are in it
            int highest = 0;
            for (var occurrences : tracker.values()) {
                highest = (occurrences > highest) ? occurrences : highest;
            }

            // Figure out which type of hand it is
            switch (tracker.size()) {
                case 1:
                    type = TYPES.FIVE_KIND;
                    break;
                case 2:
                    type = (highest == 4) ? TYPES.FOUR_KIND : TYPES.FULL_HOUSE;
                    break;
                case 3:
                    type = (highest == 3) ? TYPES.THREE_KIND : TYPES.TWO_PAIR;
                    break;
                case 4:
                    type = TYPES.ONE_PAIR;
                    break;
                case 5:
                default:
                    type = TYPES.HIGH_CARD;
                    break;
            }
        }
    }

    /** Constructor */
    public Problem7Part1() {
        try {
            File input = new File("resources/Problem7Input.txt");
            Scanner scanner = new Scanner(input);

            System.out.printf("%n%-10s | %-10s | %-10s%n", "Hand", "Bid", "Type");
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                allHands.add(new Hand(line[0], Integer.parseInt(line[1])));
            }

            sortHands();

            System.out.println("\n\nTotal: " + total);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void sortHands() {
        // Group the different hand types
        Map<Integer, ArrayList<Hand>> handGroups = new HashMap<>();
        handGroups.put(1, new ArrayList<>()); // five kinds
        handGroups.put(2, new ArrayList<>()); // four kinds
        handGroups.put(3, new ArrayList<>()); // full houses
        handGroups.put(4, new ArrayList<>()); // three kinds
        handGroups.put(5, new ArrayList<>()); // two pairs
        handGroups.put(6, new ArrayList<>()); // one pairs
        handGroups.put(7, new ArrayList<>()); // high cards

        for (Hand hand : allHands) {
            handGroups.get(hand.getType().getRank()).add(hand);
        }

        // Sort each category from highest to lowest
        for (ArrayList<Hand> hands : handGroups.values()) {
            hands.sort(new Comparator() {
                @Override
                public int compare(Object h1, Object h2) {
                    char[] hc1 = ((Hand) h1).getCards().toCharArray();
                    char[] hc2 = ((Hand) h2).getCards().toCharArray();

                    for (int i = 0; i < hc1.length; i++) {
                        // Make sure to handle both char and int values correctly
                        int v1 = (Character.isDigit(hc1[i])) ? hc1[i] : ('Z'+'A'-hc1[i]);
                        int v2 = (Character.isDigit(hc2[i])) ? hc2[i] : ('Z'+'A'-hc2[i]);

                        // Compare them
                        int compare = v1 - v2;
                        if (compare > 0) {
                            return -1;
                        } else if (compare < 0) {
                            return 1;
                        }
                    }
                    return 0;
                }
            });
        }

        // Remove unused arrays
        for (int i=0; i < handGroups.size(); i++) {
            if (handGroups.get(i) == null) {
                handGroups.remove(i);
            }
        }

        // Add everything up
        System.out.printf("%n%-13s | %-10s | %-10s | %-10s%n", "Type", "Place", "Hand", "Bid");
        int place = 1;
        for (int i=handGroups.size(); i >0; i--) {
            ArrayList<Hand> hands = handGroups.get(i);
            if ((hands == null) || hands.isEmpty()) {
                continue;
            }

            for (int j=hands.size(); j > 0; j--) {
                Hand hand = hands.get(j-1);
                System.out.printf("%-13s | %-10d | %-10s | %-10d%n", hand.getType(), place, hand.getCards(), hand.getBid());
                total += (hand.getBid()*place);
                place++;
            }
        }
    }
}
