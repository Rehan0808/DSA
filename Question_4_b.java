/*
 * This program calculates the minimum number of roads required to traverse a graph such that starting from a node,
 * you can cover (or "collect") all packages within two steps from the nodes you visit and return to the start node.
 *
 * The algorithm works as follows:
 * 1. Identify the nodes that contain packages.
 * 2. Build an adjacency list for the graph based on the given roads.
 * 3. For each node, calculate a "coverage mask" that represents which package nodes are reachable within two steps.
 * 4. For every starting node, perform a breadth-first search (BFS) on states that include the current node, the set of
 *    packages collected so far (represented as a bitmask), and the number of steps taken.
 * 5. The BFS continues until the starting node is reached again with all packages covered.
 *
 * Overall, the algorithm efficiently explores possible paths using BFS combined with bitmasking to track collected packages,
 * and returns the minimum number of roads required to satisfy the conditions.
 */

 import java.util.*;

 public class Question_4_b {
 
     /**
      * A helper class representing a state in the BFS.
      * Each state holds:
      * - the current node,
      * - the bitmask representing the set of packages collected so far,
      * - the number of steps (roads traversed) taken to reach this state.
      */
     private static class State {
         int node;
         int mask;
         int steps;
 
         /**
          * Constructor for State.
          *
          * @param node  The current node index.
          * @param mask  The bitmask representing packages collected.
          * @param steps The number of steps taken so far.
          */
         public State(int node, int mask, int steps) {
             this.node = node;
             this.mask = mask;
             this.steps = steps;
         }
     }
 
     /**
      * Builds the adjacency list for the graph.
      *
      * @param n     The number of nodes in the graph.
      * @param roads A 2D array representing roads, where each road is given as [u, v].
      * @return An adjacency list where each list at index i contains the neighbors of node i.
      */
     private static List<List<Integer>> buildAdjacencyList(int n, int[][] roads) {
         List<List<Integer>> adj = new ArrayList<>();
         for (int i = 0; i < n; i++) {
             adj.add(new ArrayList<>());
         }
         for (int[] road : roads) {
             int u = road[0], v = road[1];
             adj.get(u).add(v);
             adj.get(v).add(u);
         }
         return adj;
     }
 
     /**
      * Returns the set of nodes that are within two steps from node u.
      *
      * @param u   The starting node.
      * @param adj The adjacency list of the graph.
      * @return A set of node indices that can be reached from u in at most two steps.
      */
     private static Set<Integer> getNodesWithinTwoSteps(int u, List<List<Integer>> adj) {
         Set<Integer> visited = new HashSet<>();
         Queue<Integer> queue = new LinkedList<>();
         queue.add(u);
         visited.add(u);
         int steps = 0;
 
         while (!queue.isEmpty() && steps < 2) {
             int size = queue.size();
             for (int i = 0; i < size; i++) {
                 int current = queue.poll();
                 for (int neighbor : adj.get(current)) {
                     if (!visited.contains(neighbor)) {
                         visited.add(neighbor);
                         queue.add(neighbor);
                     }
                 }
             }
             steps++;
         }
         return visited;
     }
 
     /**
      * Calculates the minimum number of roads required to start at a node, collect all packages (within two steps), and return to the start.
      *
      * @param packages An array where packages[i] == 1 indicates that node i has a package.
      * @param roads    A 2D array where each element represents a road between two nodes.
      * @return The minimum number of roads required, or Integer.MAX_VALUE if no valid route is found.
      */
     public static int minRoads(int[] packages, int[][] roads) {
         int n = packages.length;
         // Identify indices of nodes that contain packages.
         List<Integer> packageIndices = new ArrayList<>();
         for (int i = 0; i < n; i++) {
             if (packages[i] == 1)
                 packageIndices.add(i);
         }
         if (packageIndices.isEmpty()) return 0; // No packages to collect.
 
         int totalPackages = packageIndices.size();
         int fullMask = (1 << totalPackages) - 1; // Bitmask with all packages collected.
         List<List<Integer>> adj = buildAdjacencyList(n, roads);
 
         // Precompute for each node the coverage mask: which package nodes are reachable within two steps.
         int[] coverageMasks = new int[n];
         for (int u = 0; u < n; u++) {
             Set<Integer> withinTwoSteps = getNodesWithinTwoSteps(u, adj);
             int mask = 0;
             for (int i = 0; i < totalPackages; i++) {
                 if (withinTwoSteps.contains(packageIndices.get(i))) {
                     mask |= (1 << i);
                 }
             }
             coverageMasks[u] = mask;
         }
 
         int minSteps = Integer.MAX_VALUE;
         // Try each node as a potential starting point.
         for (int start = 0; start < n; start++) {
             int initialMask = coverageMasks[start];
             if (initialMask == fullMask) {
                 // Already covers all packages without moving.
                 return 0;
             }
 
             // Distance matrix to track minimum steps for (node, mask) state.
             int[][] dist = new int[n][1 << totalPackages];
             for (int[] row : dist)
                 Arrays.fill(row, Integer.MAX_VALUE);
             Queue<State> queue = new LinkedList<>();
             queue.add(new State(start, initialMask, 0));
             dist[start][initialMask] = 0;
 
             // BFS over state space.
             while (!queue.isEmpty()) {
                 State current = queue.poll();
                 // Check if returned to start with all packages collected.
                 if (current.node == start && current.mask == fullMask) {
                     minSteps = Math.min(minSteps, current.steps);
                     break;
                 }
                 // Explore neighboring nodes.
                 for (int neighbor : adj.get(current.node)) {
                     int newMask = current.mask | coverageMasks[neighbor];
                     int newSteps = current.steps + 1;
                     if (newSteps < dist[neighbor][newMask]) {
                         dist[neighbor][newMask] = newSteps;
                         queue.add(new State(neighbor, newMask, newSteps));
                     }
                 }
             }
         }
         return minSteps;
     }
 
     /**
      * The main method serves as the entry point for the program.
      * It runs test cases to verify the correctness of the minRoads function.
      *
      * @param args Command-line arguments (not used).
      */
     public static void main(String[] args) {
         int[] packages1 = {1, 0, 0, 0, 0, 1};
         int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
         System.out.println("Test Case 1 - Expected: 2, Actual: " + minRoads(packages1, roads1));
 
         int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
         int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
         System.out.println("Test Case 2 - Expected: 2, Actual: " + minRoads(packages2, roads2));
     }
 }
 
 /*

 In Question_4_b, we determine the minimum number of roads required to collect all packages from nodes (where a node with a package is indicated by a 1)
 by starting at a node, traversing roads, and returning to the start with coverage of all packages. The algorithm employs a BFS on a state space that
 tracks the current node, a bitmask of collected packages (based on reachability within two steps), and the number of steps taken.
 Precomputation of "coverage masks" for each node simplifies the state transitions. The BFS search returns the minimal steps required,
 with test cases validating the expected results.
 */
 