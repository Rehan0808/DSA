/*
 * This program finds the kth lowest product from two arrays of returns.
 * The algorithm works by computing all possible products between the elements
 * of the two given arrays. It then sorts these products and returns the kth smallest one.
 *
 * The approach is a brute-force method that multiplies each element from the first array
 * with each element from the second array to form a list of all possible products.
 * After sorting the list, the kth element is selected as the kth lowest combined return.
 *
 * Overall, this algorithm is simple and effective for small input sizes.
 * The test cases in the main method verify that the algorithm produces the expected results.
 */

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
 
 public class Question_1_b {
     
     /**
      * Computes the kth lowest product from two arrays of returns.
      *
      * This function generates a list of products by multiplying each element of the
      * first array with each element of the second array. It then sorts the list and
      * returns the kth smallest product (1-indexed).
      *
      * @param returns1 the first array of return values
      * @param returns2 the second array of return values
      * @param k the kth position (1-indexed) to retrieve from the sorted list of products
      * @return the kth smallest product from the combined returns
      */
     public static int kthLowestCombinedReturn(int[] returns1, int[] returns2, int k) {
         List<Integer> combinedReturns = new ArrayList<>();
         
         // Multiply each element in returns1 with each element in returns2
         for (int r1 : returns1) {
             for (int r2 : returns2) {
                 combinedReturns.add(r1 * r2);
             }
         }
         
         // Sort the list of products in ascending order
         Collections.sort(combinedReturns);
         
         // Return the kth smallest element (k is 1-indexed)
         return combinedReturns.get(k - 1);
     }
     
     /**
      * The main method runs several test cases to verify the correctness of the
      * kthLowestCombinedReturn function. It prints the expected and actual outputs for each test case.
      *
      * @param args command-line arguments (not used in this program)
      */
     public static void main(String[] args) {
         int[] returns1_example1 = {2, 5};
         int[] returns2_example1 = {3, 4};
         int k1 = 2;
         int result1 = kthLowestCombinedReturn(returns1_example1, returns2_example1, k1);
         System.out.println("Test Case 1 - Expected Output: 8, Actual Output: " + result1);
         
         int[] returns1_example2 = {-4, -2, 0, 3};
         int[] returns2_example2 = {2, 4};
         int k2 = 6;
         int result2 = kthLowestCombinedReturn(returns1_example2, returns2_example2, k2);
         System.out.println("Test Case 2 - Expected Output: 0, Actual Output: " + result2);
         
         int[] returns1_example3 = {1, 3, 5, 7};
         int[] returns2_example3 = {2, 5};
         int k3 = 4;
         int result3 = kthLowestCombinedReturn(returns1_example3, returns2_example3, k3);
         System.out.println("Test Case 3 - Expected Output: 10, Actual Output: " + result3);
     }
 }
 
 /*

 In this algorithm, we computed the kth lowest product of two arrays by multiplying every pair of elements
 from the two arrays and storing these products in a list. The list was then sorted to allow easy selection of the
 kth smallest element. The test cases in the main method confirmed that the algorithm works as expected.
 */
 