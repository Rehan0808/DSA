/*
 * This program finds the closest pair of points (by Manhattan distance) given their x and y coordinates.
 * The algorithm iterates over all pairs of points, calculates the Manhattan distance for each pair, and keeps
 * track of the pair with the smallest distance. In case of a tie, it selects the pair with the smallest indices.
 *
 * The approach is a brute-force method that examines every combination of points.
 * The main method provides a test case to verify the correctness of the findClosestPair function.
 *
 * Overall, the algorithm efficiently finds the closest pair for small to moderate input sizes and the test case confirms 
 * that it works as expected.
 */

 public class Question_2_b {

    /**
     * Finds the pair of indices corresponding to the closest points based on Manhattan distance.
     *
     * This function iterates over all unique pairs of points defined by their x and y coordinates.
     * It calculates the Manhattan distance for each pair and updates the result if a smaller distance
     * is found. In case of a tie (equal distance), it prefers the pair with the smaller first index,
     * and if those are equal, the pair with the smaller second index.
     *
     * @param x_coords an array of x coordinates for the points
     * @param y_coords an array of y coordinates for the points
     * @return an array of two integers representing the indices of the closest pair of points
     */
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[]{-1, -1};

        // Iterate through each pair of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Compute the Manhattan distance between points i and j
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Update result if a new minimum distance is found or if tie-break condition is met
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }

    /**
     * The main method tests the findClosestPair function using a sample set of points.
     *
     * It defines arrays of x and y coordinates and calls findClosestPair to determine the closest pair.
     * The result is then printed to the console.
     *
     * @param args command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        int[] closestPair = findClosestPair(x_coords, y_coords);
        System.out.println("Closest Pair: [" + closestPair[0] + ", " + closestPair[1] + "]");
    }
}

/*

This algorithm finds the closest pair of points based on Manhattan distance by checking every pair of points.
It calculates the distance, and updates the closest pair using tie-breaking rules if necessary.
Finally, the main method demonstrates the functionality with a test case, confirming that the algorithm works as expected.
*/
