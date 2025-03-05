/*
 * This program solves the "Min Rewards" problem.
 * The goal is to determine the minimum total rewards required for children based on their ratings,
 * ensuring that each child receives at least one reward and children with higher ratings than their neighbors
 * receive more rewards than those neighbors.
 *
 * The algorithm works in two passes:
 * 1. Left-to-right pass: It ensures that each child with a higher rating than the previous child
 *    gets one more reward than that previous child.
 * 2. Right-to-left pass: It ensures that each child with a higher rating than the next child
 *    gets at least one more reward than that next child.
 *
 * Finally, the algorithm sums up all rewards assigned to obtain the minimum total rewards needed.
 *
 * Overall, the algorithm efficiently computes the solution with a linear time complexity,
 * and the provided test cases confirm that the solution works as expected.
 */

 public class Question_2_a {
    
    /**
     * Calculates the minimum total rewards required for the given ratings.
     *
     * The function first initializes an array with 1 reward for each child.
     * It then performs a left-to-right pass to adjust rewards where the current rating
     * is higher than the previous rating. Afterwards, a right-to-left pass adjusts rewards
     * where the current rating is higher than the next rating, taking the maximum between
     * the current reward and one more than the next child's reward.
     *
     * Finally, it sums up all rewards and returns the total.
     *
     * @param ratings an array representing the ratings of each child
     * @return the minimum total rewards required to satisfy the conditions
     */
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        if (n == 0) return 0;
        
        // Initialize rewards: each child gets at least one reward
        int[] rewards = new int[n];
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }
        
        // Left-to-right pass:
        // If current rating is greater than previous, increment reward from previous child's reward.
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Right-to-left pass:
        // If current rating is greater than next, ensure reward is at least one more than next child's reward.
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        // Sum up all the rewards to get the total minimum rewards required.
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }
    
    /**
     * The main method runs test cases to verify that the minRewards function works correctly.
     *
     * @param args command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        int[] ratings1 = {1, 0, 2};
        int result1 = minRewards(ratings1);
        System.out.println("Test Case 1 - Expected Output: 5, Actual Output: " + result1);

        int[] ratings2 = {1, 2, 2};
        int result2 = minRewards(ratings2);
        System.out.println("Test Case 2 - Expected Output: 4, Actual Output: " + result2);
    }
}

/*

In this algorithm, we determined the minimum rewards required for children based on their ratings.
We initialized rewards with a base value of 1 for each child, then used a left-to-right pass to assign
additional rewards when a child's rating exceeded the previous child's rating. A right-to-left pass was then
used to adjust rewards for cases where a child's rating exceeded the next child's rating. Finally, we summed
the rewards to compute the total minimum rewards. The test cases confirmed that the algorithm worked as expected.
*/
