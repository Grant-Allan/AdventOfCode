import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int total = 0;

        try {
            File input = new File("resources/input.txt");
            Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String first = "";
                String second = "";
                String together = "";

                // Get the first number
                for (int i=0; i < line.length(); i++) {
                    if (isNumeric(String.valueOf(line.charAt(i)))) {
                        first = String.valueOf(line.charAt(i));
                        break;
                    }
                }

                // Get the line in reverse
                StringBuilder reverseLine = new StringBuilder();
                for (int i=0; i < line.length(); i++) {
                    char ch= line.charAt(i);
                    reverseLine.insert(0, ch);
                }

                // Get the second number
                for (int i=0; i < reverseLine.length(); i++) {
                    if (isNumeric(String.valueOf(reverseLine.charAt(i)))) {
                        second = String.valueOf(reverseLine.charAt(i));
                        break;
                    }
                }

                // Process the numbers
                together += first;
                together += second;

                // Add it to the total
                total += Integer.parseInt(together);

                // Record what happened
                System.out.println("total: " + total + " :: " + line + " --> " + first + " + " + second + " = " + together);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("FINAL TOTAL: " + total);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}