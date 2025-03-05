/*
 * Network Topology Optimizer
 *
 * This application implements a network topology optimizer where users can interactively add nodes and edges
 * to a graphical panel. The application then provides two main features:
 * 1. Finding a Minimum Spanning Tree (MST) based on edge cost using a Kruskal-style algorithm with a Disjoint Set Union.
 * 2. Finding the shortest path between two nodes based on bandwidth (by converting bandwidth into a weight) using a variant of Dijkstra’s algorithm.
 *
 * The algorithm works by:
 * - Allowing user inputs via a Swing GUI to add network nodes and edges.
 * - Sorting edges based on cost and using a union-find structure to compute the MST.
 * - Applying a shortest path algorithm with a priority queue to compute paths that optimize for a derived weight from bandwidth.
 *
 * In the end, the GUI updates to display the MST and the shortest path over the network graph.
 */

 import java.awt.*;
 import java.awt.event.*;
 import java.util.*;
 import java.util.List;
 import javax.swing.*;
 
 public class Question_5 extends JFrame {
 
     private GraphPanel graphPanel;
     private JLabel costLabel, latencyLabel;
 
     /**
      * Constructor for the main application frame.
      * Initializes the frame and sets up the UI components.
      */
     public Question_5() {
         setTitle("Network Topology Optimizer");
         setSize(800, 600);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         initComponents();
     }
 
     /**
      * Initializes and lays out all components including the graph panel and control panel.
      * Sets up buttons and their action listeners for adding nodes, edges, computing MST, and selecting the shortest path.
      */
     private void initComponents() {
         graphPanel = new GraphPanel();
         JPanel controlPanel = new JPanel(new GridLayout(0, 1));
 
         JButton addNodeBtn = new JButton("Add Node");
         JButton addEdgeBtn = new JButton("Add Edge");
         JButton mstBtn = new JButton("Find MST (Cost)");
         JButton shortestPathBtn = new JButton("Find Shortest Path (Bandwidth)");
 
         costLabel = new JLabel("Total Cost: 0");
         latencyLabel = new JLabel("Latency: N/A");
 
         controlPanel.add(addNodeBtn);
         controlPanel.add(addEdgeBtn);
         controlPanel.add(mstBtn);
         controlPanel.add(shortestPathBtn);
         controlPanel.add(costLabel);
         controlPanel.add(latencyLabel);
 
         add(graphPanel, BorderLayout.CENTER);
         add(controlPanel, BorderLayout.EAST);
 
         // Set the action for the "Add Node" button to change mode to ADD_NODE
         addNodeBtn.addActionListener(e -> graphPanel.setMode(GraphPanel.Mode.ADD_NODE));
         // Set the action for the "Add Edge" button to change mode to ADD_EDGE
         addEdgeBtn.addActionListener(e -> graphPanel.setMode(GraphPanel.Mode.ADD_EDGE));
         // When "Find MST" is clicked, compute MST and update the cost label
         mstBtn.addActionListener(e -> {
             graphPanel.findMST();
             updateStats();
         });
         // Set the action to start shortest path selection mode
         shortestPathBtn.addActionListener(e -> graphPanel.startShortestPathSelection());
     }
 
     /**
      * Updates the cost label with the total cost computed from the MST.
      */
     private void updateStats() {
         int totalCost = graphPanel.getGraphModel().calculateTotalCost();
         costLabel.setText("Total Cost: " + totalCost);
     }
 
     /**
      * Main method to launch the application.
      *
      * @param args Command-line arguments (not used).
      */
     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new Question_5().setVisible(true));
     }
 }
 
 /**
  * GraphPanel class represents the drawing area where nodes and edges are drawn.
  * It handles mouse events and updates the graph based on user interactions.
  */
 class GraphPanel extends JPanel {
 
     /**
      * Enumeration for the different modes of user interaction.
      */
     enum Mode { ADD_NODE, ADD_EDGE, SELECT_PATH }
 
     private Mode currentMode = Mode.ADD_NODE;
     private GraphModel graphModel = new GraphModel();
     private GraphModel.GraphNode selectedNode;
     private GraphModel.GraphNode pathStartNode;
 
     /**
      * Constructor for the GraphPanel.
      * Sets up the panel background and mouse listener for interactive operations.
      */
     public GraphPanel() {
         setBackground(Color.WHITE);
         addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 handleClick(e.getX(), e.getY());
             }
         });
     }
 
     /**
      * Handles mouse click events based on the current mode.
      * - In ADD_NODE mode: Adds a new node at the clicked location.
      * - In ADD_EDGE mode: Selects nodes to create an edge with user-specified cost and bandwidth.
      * - In SELECT_PATH mode: Allows the user to select two nodes to compute the shortest path.
      *
      * @param x The x-coordinate of the mouse click.
      * @param y The y-coordinate of the mouse click.
      */
     private void handleClick(int x, int y) {
         switch (currentMode) {
             case ADD_NODE:
                 graphModel.addNode(new GraphModel.GraphNode(x, y));
                 repaint();
                 break;
             case ADD_EDGE:
                 GraphModel.GraphNode node = graphModel.getNodeAt(x, y);
                 if (node != null) {
                     if (selectedNode == null) {
                         selectedNode = node;
                     } else {
                         try {
                             String costStr = JOptionPane.showInputDialog("Enter cost:");
                             String bwStr = JOptionPane.showInputDialog("Enter bandwidth:");
                             int cost = Integer.parseInt(costStr);
                             int bandwidth = Integer.parseInt(bwStr);
                             graphModel.addEdge(selectedNode, node, cost, bandwidth);
                         } catch (NumberFormatException ex) {
                             JOptionPane.showMessageDialog(null, "Invalid number format!");
                         }
                         selectedNode = null;
                         repaint();
                     }
                 }
                 break;
             case SELECT_PATH:
                 GraphModel.GraphNode clicked = graphModel.getNodeAt(x, y);
                 if (clicked != null) {
                     if (pathStartNode == null) {
                         pathStartNode = clicked;
                     } else {
                         graphModel.computeShortestPath(pathStartNode, clicked);
                         pathStartNode = null;
                         repaint();
                     }
                 }
                 break;
         }
     }
 
     /**
      * Initiates the mode for selecting nodes for computing the shortest path.
      * Resets any previous path start node.
      */
     public void startShortestPathSelection() {
         setMode(Mode.SELECT_PATH);
         pathStartNode = null;
     }
 
     /**
      * Invokes the computation of the Minimum Spanning Tree (MST) on the graph model.
      */
     public void findMST() {
         graphModel.computeMST();
         repaint();
     }
 
     /**
      * Getter for the GraphModel used in this panel.
      *
      * @return The current graph model.
      */
     public GraphModel getGraphModel() {
         return graphModel;
     }
 
     /**
      * Overridden paintComponent to draw the graph.
      *
      * @param g The graphics context used for drawing.
      */
     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         graphModel.draw(g);
     }
 
     /**
      * Sets the current interaction mode for the panel.
      *
      * @param mode The mode to set.
      */
     public void setMode(Mode mode) {
         currentMode = mode;
     }
 }
 
 /**
  * GraphModel class encapsulates the underlying data structure for nodes and edges in the network.
  * It provides methods to add nodes/edges, compute the MST, compute the shortest path, and draw the graph.
  */
 class GraphModel {
     private List<GraphNode> nodes = new ArrayList<>();
     private List<GraphEdge> edges = new ArrayList<>();
     private List<GraphEdge> mstEdges = new ArrayList<>();
     private List<GraphEdge> shortestPath = new ArrayList<>();
 
     /**
      * GraphNode represents a node in the network graph.
      */
     static class GraphNode {
         public int x, y;
 
         /**
          * Constructor to initialize a graph node at the given coordinates.
          *
          * @param x The x-coordinate.
          * @param y The y-coordinate.
          */
         public GraphNode(int x, int y) {
             this.x = x;
             this.y = y;
         }
         
         /**
          * Draws the node as a blue circle on the provided graphics context.
          *
          * @param g The graphics context used for drawing.
          */
         public void draw(Graphics g) {
             g.setColor(Color.BLUE);
             g.fillOval(x - 10, y - 10, 20, 20);
         }
     }
 
     /**
      * GraphEdge represents an edge connecting two nodes with associated cost and bandwidth.
      */
     static class GraphEdge {
         public GraphNode from, to;
         public int cost, bandwidth;
         
         /**
          * Constructor to initialize an edge between two nodes with cost and bandwidth.
          *
          * @param from The starting node.
          * @param to The ending node.
          * @param cost The cost associated with the edge.
          * @param bandwidth The bandwidth available on the edge.
          */
         public GraphEdge(GraphNode from, GraphNode to, int cost, int bandwidth) {
             this.from = from;
             this.to = to;
             this.cost = cost;
             this.bandwidth = bandwidth;
         }
         
         /**
          * Draws the edge on the graphics context.
          * If highlight is true, the edge is drawn in the current color (to denote MST or shortest path).
          *
          * @param g The graphics context used for drawing.
          * @param highlight Flag indicating whether the edge should be highlighted.
          */
         public void draw(Graphics g, boolean highlight) {
             // Draw the line with current color (if highlighted, it uses the caller's set color)
             g.drawLine(from.x, from.y, to.x, to.y);
             // Draw the cost and bandwidth label in the middle of the edge
             g.drawString(cost + "/" + bandwidth, (from.x + to.x) / 2, (from.y + to.y) / 2);
         }
     }
 
     /**
      * Adds a new node to the graph.
      *
      * @param node The GraphNode to add.
      */
     public void addNode(GraphNode node) { 
         nodes.add(node); 
     }
 
     /**
      * Adds a new edge between two nodes with the specified cost and bandwidth.
      *
      * @param from The starting node.
      * @param to The ending node.
      * @param cost The cost associated with the edge.
      * @param bandwidth The bandwidth of the edge.
      */
     public void addEdge(GraphNode from, GraphNode to, int cost, int bandwidth) {
         edges.add(new GraphEdge(from, to, cost, bandwidth));
     }
 
     /**
      * Returns the node at the given coordinates if one exists within a threshold distance.
      *
      * @param x The x-coordinate.
      * @param y The y-coordinate.
      * @return The GraphNode at the location, or null if no node is close enough.
      */
     public GraphNode getNodeAt(int x, int y) {
         for (GraphNode node : nodes) {
             if (Math.hypot(node.x - x, node.y - y) < 15) {
                 return node;
             }
         }
         return null;
     }
 
     /**
      * Computes the Minimum Spanning Tree (MST) of the graph using a union-find (Disjoint Set Union) approach.
      * It sorts all edges by cost and then adds them to the MST if they do not form a cycle.
      */
     public void computeMST() {
         mstEdges.clear();
         List<GraphEdge> sortedEdges = new ArrayList<>(edges);
         sortedEdges.sort(Comparator.comparingInt(e -> e.cost));
         DisjointSetUnion dsu = new DisjointSetUnion(nodes.size());
 
         for (GraphEdge edge : sortedEdges) {
             int u = nodes.indexOf(edge.from);
             int v = nodes.indexOf(edge.to);
             if (dsu.find(u) != dsu.find(v)) {
                 mstEdges.add(edge);
                 dsu.union(u, v);
             }
         }
     }
 
     /**
      * Computes the shortest path between the start and end nodes using a variant of Dijkstra's algorithm.
      * The weight for each edge is derived from its bandwidth (with higher bandwidth leading to lower weight).
      *
      * @param start The starting node.
      * @param end The destination node.
      */
     public void computeShortestPath(GraphNode start, GraphNode end) {
         Map<GraphNode, Integer> dist = new HashMap<>();
         Map<GraphNode, GraphNode> prev = new HashMap<>();
         PriorityQueue<GraphNode> queue = new PriorityQueue<>(
             Comparator.comparingInt(n -> dist.getOrDefault(n, Integer.MAX_VALUE))
         );
 
         // Initialize distances and previous nodes
         for (GraphNode node : nodes) {
             dist.put(node, Integer.MAX_VALUE);
             prev.put(node, null);
         }
         dist.put(start, 0);
         queue.add(start);
 
         // Process nodes in the priority queue
         while (!queue.isEmpty()) {
             GraphNode u = queue.poll();
             if (u == end) break;
 
             for (GraphEdge edge : getAdjacentEdges(u)) {
                 GraphNode v = (edge.from == u) ? edge.to : edge.from;
                 int weight = 1000 / edge.bandwidth; // Derived weight based on bandwidth
                 int alt = dist.get(u) + weight;
                 
                 if (alt < dist.get(v)) {
                     dist.put(v, alt);
                     prev.put(v, u);
                     if (!queue.contains(v)) {
                         queue.add(v);
                     }
                 }
             }
         }
 
         // Reconstruct the shortest path by backtracking from the destination
         shortestPath.clear();
         GraphNode current = end;
         while (prev.get(current) != null) {
             GraphNode predecessor = prev.get(current);
             GraphEdge edge = getEdgeBetween(predecessor, current);
             if (edge != null) {
                 shortestPath.add(edge);
                 current = predecessor;
             } else {
                 break;
             }
         }
         Collections.reverse(shortestPath);
     }
 
     /**
      * Retrieves a list of all edges adjacent to a given node.
      *
      * @param node The node for which adjacent edges are sought.
      * @return A list of edges connected to the node.
      */
     private List<GraphEdge> getAdjacentEdges(GraphNode node) {
         List<GraphEdge> adjacent = new ArrayList<>();
         for (GraphEdge edge : edges) {
             if (edge.from == node || edge.to == node) {
                 adjacent.add(edge);
             }
         }
         return adjacent;
     }
 
     /**
      * Finds and returns the edge connecting two given nodes, if it exists.
      *
      * @param a The first node.
      * @param b The second node.
      * @return The GraphEdge connecting a and b, or null if none exists.
      */
     private GraphEdge getEdgeBetween(GraphNode a, GraphNode b) {
         for (GraphEdge edge : edges) {
             if ((edge.from == a && edge.to == b) || (edge.from == b && edge.to == a)) {
                 return edge;
             }
         }
         return null;
     }
 
     /**
      * Calculates the total cost of all edges included in the MST.
      *
      * @return The sum of the costs of the MST edges.
      */
     public int calculateTotalCost() {
         return mstEdges.stream().mapToInt(e -> e.cost).sum();
     }
 
     /**
      * Draws the entire graph on the provided graphics context.
      * This includes all edges, highlighted MST edges in red, highlighted shortest path edges in green,
      * and all nodes.
      *
      * @param g The graphics context used for drawing.
      */
     public void draw(Graphics g) {
         // Draw all edges normally
         for (GraphEdge edge : edges) {
             edge.draw(g, false);
         }
         // Highlight MST edges in red
         g.setColor(Color.RED);
         for (GraphEdge edge : mstEdges) {
             edge.draw(g, true);
         }
         // Highlight shortest path edges in green
         g.setColor(Color.GREEN);
         for (GraphEdge edge : shortestPath) {
             edge.draw(g, true);
         }
         // Draw all nodes on top of the edges
         for (GraphNode node : nodes) {
             node.draw(g);
         }
     }
 
     /**
      * DisjointSetUnion class implements the union-find data structure for cycle detection in MST computation.
      */
     static class DisjointSetUnion {
         int[] parent;
 
         /**
          * Initializes the DSU with each element as its own parent.
          *
          * @param size The number of elements.
          */
         public DisjointSetUnion(int size) { 
             parent = new int[size]; 
             Arrays.setAll(parent, i -> i); 
         }
 
         /**
          * Finds the representative (root) of the set that contains x with path compression.
          *
          * @param x The element for which to find the set representative.
          * @return The representative of the set.
          */
         public int find(int x) { 
             return parent[x] == x ? x : (parent[x] = find(parent[x])); 
         }
 
         /**
          * Unites the sets that contain x and y.
          *
          * @param x An element in the first set.
          * @param y An element in the second set.
          */
         public void union(int x, int y) { 
             parent[find(x)] = find(y); 
         }
     }
 }
 
 /*
 
  * This network topology optimizer allowed us to build a dynamic graph through user interactions.
  * We could add nodes and edges, compute the MST using a union-find approach to minimize cost,
  * and determine the shortest path based on a bandwidth-derived weight using Dijkstra's algorithm.
  * The algorithm worked as expected, providing visual feedback on the MST (in red) and the shortest path (in green),
  * and updating statistics such as total cost accordingly.
  */
 