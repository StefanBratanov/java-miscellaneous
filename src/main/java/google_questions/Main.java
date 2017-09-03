package google_questions;

public class Main {

    public static void main(String[] args) {
        System.out.println(greatestDifference(new int[]{5, 8, 6, 1}));
    }

    /**
     * Write a function that takes an array of numbers and
     * returns the greatest difference you can get by subtracting any two of those numbers.
     */
    public static int greatestDifference(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int min = arr[0];
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            min = arr[i] < min ? arr[i] : min;
            max = arr[i] > max ? arr[i] : max;
        }

        return max - min;
    }
}
