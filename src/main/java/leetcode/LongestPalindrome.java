package leetcode;

public class LongestPalindrome {

    public static void main(String[] args) {
        LongestPalindrome solution = new LongestPalindrome();

        String answer = solution.longestPalindrome("sqbdbs");

        System.out.println(answer);
    }

    public String longestPalindrome(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s;
    }

    public int reverseIndex(int index, int length) {
        return Math.abs(index - length);
    }
}
