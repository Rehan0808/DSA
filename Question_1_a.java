/**
 * This program implements an algorithm to determine the minimal number of measurements
 * required to test a building (or analogous scenario) given a certain number of samples (k)
 * and intervals (n). The algorithm works by calculating the cumulative sum of binomial 
 * coefficients for an increasing number of measurements until this sum is at least n+1.
 *
 * The idea behind the algorithm is to find the smallest number of measurements (m) such that 
 * the sum of the combinations (representing the maximum number of floors or intervals that can 
 * be tested) is greater than or equal to the required number of outcomes (n+1).
 *
 * The algorithm uses a loop to increment m and, for each m, calculates the binomial coefficients 
 * for up to k samples. When the cumulative sum reaches the required threshold, the function returns m.
 *
 * Overall, the algorithm iteratively increases m and checks the maximum interval coverage until
 * it meets or exceeds the requirement. The test cases in the main method confirm that the algorithm 
 * works as expected for various inputs.
 */
public class Question_1_a {

    /**
     * The main method serves as the entry point for the program.
     * It runs several test cases to demonstrate the functionality of the minimalMeasurements function.
     *
     * @param args command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        System.out.println(minimalMeasurements(1, 2));
        System.out.println(minimalMeasurements(2, 6));
        System.out.println(minimalMeasurements(3, 14));
    }

    /**
     * Computes the minimal number of measurements required such that the sum of 
     * binomial coefficients (up to the lesser of m and k) is at least n+1.
     *
     * The function works by iterating over an increasing measurement count 'm'.
     * For each m, it calculates the sum of binomial coefficients, which represents
     * the maximum number of intervals that can be covered with m measurements and k samples.
     * When this sum is equal to or exceeds the required value (n+1), the function returns m.
     *
     * @param k the number of samples (or eggs, in a typical egg-drop problem)
     * @param n the number of intervals (or floors)
     * @return the minimal number of measurements required
     */
    public static int minimalMeasurements(int k, int n) {
        int required = n + 1;
        int m = 0;

        while (true) {
            int currentSum = 0;
            int currentTerm = 1;
            int maxSamplesUsed = Math.min(m, k);

            // Calculate the sum of binomial coefficients for the current m
            for (int i = 0; i <= maxSamplesUsed; i++) {
                currentSum += currentTerm;

                // Calculate next binomial coefficient using multiplicative formula
                if (i < maxSamplesUsed) {
                    currentTerm = currentTerm * (m - i) / (i + 1);
                }
            }

            // If the sum of coefficients is enough to cover required intervals, return m
            if (currentSum >= required) {
                return m;
            }
            m++;
        }
    }
}

/*

The algorithm in this program finds the minimal number of measurements required to cover 
n intervals given k samples. It does this by iteratively increasing the measurement count (m)
and computing the cumulative sum of binomial coefficients, which represent the maximum intervals 
that can be tested. Once the sum is equal to or exceeds n+1, the algorithm returns the minimal m.
Test cases in the main method confirm that the algorithm works as expected.
*/
