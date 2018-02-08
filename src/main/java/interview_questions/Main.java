package interview_questions;

public class Main {

    public static void main(String[] args) {
        System.out.println(convertToInt("123"));
        System.out.println(convertToInt("-45"));
    }

    /**
     * convert string to integer without using
     * the inbuilt parsing java methods
     */
    private static int convertToInt(String input) {
        if (input == null || input.length() < 1) {
            throwNumberFormatException(input);
        }

        boolean isNegative = input.charAt(0) == '-';

        int result = 0;
        int multiplier = 1;
        for (int i = input.length() - 1; i >= (isNegative ? 1 : 0); i--) {
            int ch = input.charAt(i) - '0';
            result += ch * multiplier;
            multiplier *= 10;
        }

        return isNegative ? -result : result;

    }

    private static void throwNumberFormatException(String input) {
        throw new NumberFormatException("For input string: " + input);
    }
}
