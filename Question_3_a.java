/*
 * This program calculates the minimum total cost to connect all devices by leveraging a Minimum Spanning Tree (MST)
 * algorithm (Kruskal's algorithm) on an augmented graph that includes a virtual node. The virtual node (node 0) is
 * connected to every device with an edge whose cost is equal to the module installation cost for that device.
 *
 * The algorithm works as follows:
 * 1. It creates a list of all edges, including those from the virtual node to each device (representing module costs)
 *    and the direct connections between devices.
 * 2. The edges are sorted by cost in ascending order.
 * 3. Using a Disjoint Set Union (DSU) structure, it iteratively adds the smallest edge that connects two separate components,
 *    effectively building an MST.
 * 4. The process continues until all devices are connected (i.e., when n edges have been added for n+1 nodes).
 *
 * The main method contains test cases that verify the correctness of the algorithm.
 */

 import java.util.*;

 class DSU {
     int[] parent;
     int[] rank;
 
     /**
      * Initializes the Disjoint Set Union (DSU) structure.
      *
      * Each node is initially its own parent, and the rank is set to 0.
      *
      * @param size the total number of nodes in the DSU.
      */
     public DSU(int size) {
         parent = new int[size];
         rank = new int[size];
         for (int i = 0; i < size; i++) {
             parent[i] = i; // Each node is its own parent initially
         }
     }
 
     /**
      * Finds the root of the node 'x' with path compression.
      *
      * Path compression flattens the structure, making future queries faster.
      *
      * @param x the node for which to find the root.
      * @return the root of node 'x'.
      */
     public int find(int x) {
         if (parent[x] != x) {
             parent[x] = find(parent[x]); // Path compression
         }
         return parent[x];
     }
 
     /**
      * Unions two sets containing nodes x and y by rank.
      *
      * The union operation connects the sets if they are separate and uses rank to keep the tree shallow.
      *
      * @param x first node.
      * @param y second node.
      * @return true if the nodes were in separate sets and the union was performed; false otherwise.
      */
     public boolean union(int x, int y) {
         int xRoot = find(x);
         int yRoot = find(y);
         if (xRoot == yRoot) return false; // Already connected
 
         // Union by rank to balance tree height
         if (rank[xRoot] < rank[yRoot]) {
             parent[xRoot] = yRoot;
         } else {
             parent[yRoot] = xRoot;
             if (rank[xRoot] == rank[yRoot]) {
                 rank[xRoot]++;
             }
         }
         return true;
     }
 }
 
 public class Question_3_a {
 
     /**
      * Calculates the minimum total cost to connect all devices using MST with a virtual node.
      *
      * The function builds an extended graph where a virtual node (0) is connected to each device with an edge
      * whose cost is the module installation cost. It then adds the direct connections between devices and applies
      * Kruskal's algorithm to find the MST. The total cost of the MST is the minimum total cost required.
      *
      * @param n           Number of devices.
      * @param modules     Array where modules[i] is the cost to install a module on device (i+1).
      * @param connections Array of connections between devices in the format [device1, device2, cost].
      * @return Minimum total cost to connect all devices.
      */
     public static int minTotalCost(int n, int[] modules, int[][] connections) {
         List<int[]> edges = new ArrayList<>();
 
         // Add edges from virtual node (0) to each device (cost = module cost)
         for (int i = 1; i <= n; i++) {
             edges.add(new int[]{0, i, modules[i - 1]});
         }
 
         // Add all direct connections between devices (device1 and device2 are 1-based)
         for (int[] conn : connections) {
             edges.add(new int[]{conn[0], conn[1], conn[2]});
         }
 
         // Sort edges by cost in ascending order for Kruskal's algorithm
         Collections.sort(edges, (a, b) -> a[2] - b[2]);
 
         DSU dsu = new DSU(n + 1); // Nodes 0 to n (virtual node is 0)
         int totalCost = 0;
         int edgesUsed = 0;
 
         // Process each edge in sorted order
         for (int[] edge : edges) {
             int u = edge[0];
             int v = edge[1];
             int cost = edge[2];
 
             // If the nodes are not already connected, include this edge in the MST
             if (dsu.find(u) != dsu.find(v)) {
                 dsu.union(u, v);
                 totalCost += cost;
                 edgesUsed++;
                 // MST for (n+1 nodes) requires n edges
                 if (edgesUsed == n) break;
             }
         }
 
         return totalCost;
     }
 
     /**
      * The main method runs test cases to verify the correctness of the minTotalCost function.
      *
      * It defines several scenarios to test module installation costs and direct connections between devices.
      *
      * @param args command-line arguments (not used in this program)
      */
     public static void main(String[] args) {
         // Test Case 1 (Sample Input)
         int n1 = 3;
         int[] modules1 = {1, 2, 2};
         int[][] connections1 = {{1, 2, 1}, {2, 3, 1}};
         System.out.println("Test Case 1 - Expected: 3, Actual: " + minTotalCost(n1, modules1, connections1));
 
         // Test Case 2 (All devices use modules)
         int n2 = 1;
         int[] modules2 = {5};
         int[][] connections2 = {};
         System.out.println("Test Case 2 - Expected: 5, Actual: " + minTotalCost(n2, modules2, connections2));
 
         // Test Case 3 (Mix of modules and connections)
         int n3 = 2;
         int[] modules3 = {3, 4};
         int[][] connections3 = {{1, 2, 5}};
         System.out.println("Test Case 3 - Expected: 7, Actual: " + minTotalCost(n3, modules3, connections3));
     }
 }
 
 /*

 In this algorithm, we approached the problem of connecting all devices at the minimum cost by extending the graph
 to include a virtual node representing module installation costs. By adding edges from the virtual node to every device,
 and then incorporating direct connections between devices, we formed a complete set of potential connections.
 We then applied Kruskal's algorithm using a Disjoint Set Union (DSU) structure to build a Minimum Spanning Tree (MST)
 of the extended graph. The MST's total cost corresponds to the minimum cost needed to connect all devices.
 The test cases demonstrate that the algorithm produces the expected results.
 */
 