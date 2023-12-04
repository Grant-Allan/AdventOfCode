package helpers;

public class Helper {
    /* Numbers as words */
    private static final String ONE = "one";
    private static final String TWO = "two";
    private static final String THREE = "three";
    private static final String FOUR = "four";
    private static final String FIVE = "five";
    private static final String SIX = "six";
    private static final String SEVEN = "seven";
    private static final String EIGHT = "eight";
    private static final String NINE = "nine";

    /**
     * Checks to see if a given string is a number
     * @param str the string
     * @return true for if it is a number, false otherwise
     */
    public static boolean isNumeric(String str) {
        boolean numeric = false;

        // If the string is null or empty, exit immediately
        if ((str == null) || (str.length()==0)) {
            return false;
        }

        // Check to see if a char string is a number
        try {
            Integer.parseInt(str);
            numeric = true;
        } catch (NumberFormatException ignored) {}

        return numeric;
    }

    /**
     * Checks to see if a given string is a number
     * @param str the string
     * @return the number it is (or null, if it isn't one)
     */
    public static String isBlockNumeric(String str) {
        // Check to see if a word string is a number
        switch (str) {
            case ONE:
                return "1";
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case FIVE:
                return "5";
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            default:
                return null;
        }
    }
}
