package Question_3_b;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/*
 * File: TetrisGame.java
 * ---------------
 * This is the main class for the Tetris game. It extends JFrame and encapsulates the entire game logic,
 * including game state management, block movement, collision detection, scoring, and rendering updates.
 *
 * Key functionalities include:
 * - Managing the game grid, score, and current/next blocks.
 * - Handling user inputs for moving and rotating the current block.
 * - Implementing the game loop, which includes block movement, placement, line clearing, and checking for game over.
 * - Updating the game display using the GameBoard component.
 *
 * Summary:
 * TetrisGame.java integrates all components of the Tetris game. It sets up the game window,
 * initializes the game state (including a bag of blocks), handles user input, processes the game loop,
 * and updates the display. The game runs until a game over condition is met, at which point the player can choose to restart.
 */

public class TetrisGame extends JFrame {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    private final boolean[][] grid = new boolean[WIDTH][HEIGHT];
    private final Color[][] colors = new Color[WIDTH][HEIGHT];
    private final Queue<Block> blockQueue = new LinkedList<>();
    private Block currentBlock;
    private boolean gameOver = false;
    private int score = 0;
    private final GameBoard gameBoard;
    private final JLabel scoreLabel;
    private List<Block> blockBag = new ArrayList<>();

    /**
     * Constructor for TetrisGame.
     * Sets up the main window, initializes game components, and starts a new game.
     */
    public TetrisGame() {
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameBoard = new GameBoard(this);
        add(gameBoard, BorderLayout.CENTER);

        // Set up controls for moving and rotating blocks
        JPanel controls = new JPanel();
        JButton left = new JButton("←"), right = new JButton("→"), rotate = new JButton("↻");
        controls.add(left);
        controls.add(rotate);
        controls.add(right);

        left.addActionListener(e -> moveHorizontal(-1));
        right.addActionListener(e -> moveHorizontal(1));
        rotate.addActionListener(e -> rotateBlock());

        scoreLabel = new JLabel("Score: 0");
        add(scoreLabel, BorderLayout.NORTH);
        add(controls, BorderLayout.SOUTH);

        setSize(500, 700);
        setLocationRelativeTo(null);
        startNewGame();
    }

    // Getters for GameBoard access

    /**
     * Returns the game grid representing filled cells.
     *
     * @return A 2D boolean array of the grid.
     */
    public boolean[][] getGrid() { return grid; }

    /**
     * Returns the color grid representing the color of filled cells.
     *
     * @return A 2D Color array of the grid.
     */
    public Color[][] getColors() { return colors; }

    /**
     * Returns the current falling block.
     *
     * @return The current Block.
     */
    public Block getCurrentBlock() { return currentBlock; }

    /**
     * Returns the next block in the queue.
     *
     * @return The next Block.
     */
    public Block getNextBlock() { return blockQueue.peek(); }

    /**
     * Starts a new game by initializing the game state.
     */
    private void startNewGame() {
        gameOver = false;
        score = 0;
        clearGrid();
        blockQueue.clear();
        initializeBlockBag();
        blockQueue.add(getNextBlockFromBag());
        blockQueue.add(getNextBlockFromBag());
        currentBlock = blockQueue.poll();
        updateScore();
        new Thread(this::gameLoop).start();
    }

    /**
     * Clears the game grid.
     */
    private void clearGrid() {
        for (int x = 0; x < WIDTH; x++) {
            Arrays.fill(grid[x], false);
            Arrays.fill(colors[x], null);
        }
    }

    /**
     * Initializes the block bag with all Tetris block types and shuffles them.
     */
    private void initializeBlockBag() {
        blockBag.clear();
        int[][][] shapes = {
            {{1,1,1,1}}, 
            {{1,1}, {1,1}}, 
            {{1,1,1}, {0,1,0}},
            {{1,1,1}, {1,0,0}}, 
            {{1,1,1}, {0,0,1}},
            {{1,1,0}, {0,1,1}}, 
            {{0,1,1}, {1,1,0}}
        };
        Color[] blockColors = {
            Color.CYAN, Color.YELLOW, Color.MAGENTA,
            Color.ORANGE, Color.BLUE, Color.GREEN, Color.RED
        };
        
        for (int i = 0; i < shapes.length; i++) {
            blockBag.add(new Block(shapes[i], blockColors[i]));
        }
        Collections.shuffle(blockBag);
    }

    /**
     * Retrieves the next block from the block bag.
     *
     * @return The next Block.
     */
    private Block getNextBlockFromBag() {
        if (blockBag.isEmpty()) initializeBlockBag();
        return blockBag.remove(0);
    }

    /**
     * The main game loop that updates the game state, moves blocks, places blocks, and clears lines.
     */
    private void gameLoop() {
        while (!gameOver) {
            try {
                Thread.sleep(1000 - Math.min(score / 5 * 25, 700));
            } catch (InterruptedException ignored) {}

            if (!moveDown()) {
                placeBlock();
                checkLines();
                if (isGameOver()) {
                    gameOver = true;
                    showGameOver();
                } else {
                    currentBlock = blockQueue.poll();
                    blockQueue.add(getNextBlockFromBag());
                }
            }
            SwingUtilities.invokeLater(gameBoard::repaint);
        }
    }

    /**
     * Moves the current block horizontally.
     *
     * @param dx The horizontal displacement (negative for left, positive for right).
     * @return True if the move was successful, false if a collision occurred.
     */
    private boolean moveHorizontal(int dx) {
        int originalX = currentBlock.getX();
        currentBlock.move(dx, 0);
        if (collision()) {
            currentBlock.setX(originalX);
            return false;
        }
        return true;
    }

    /**
     * Moves the current block downward.
     *
     * @return True if the move was successful, false if a collision occurred.
     */
    private boolean moveDown() {
        int originalY = currentBlock.getY();
        currentBlock.move(0, 1);
        if (collision()) {
            currentBlock.setY(originalY);
            return false;
        }
        return true;
    }

    /**
     * Checks for a collision between the current block and the game boundaries or filled cells.
     *
     * @return True if a collision is detected, false otherwise.
     */
    private boolean collision() {
        for (Cell cell : currentBlock.getCells()) {
            int x = cell.getX();
            int y = cell.getY();
            if (x < 0 || x >= WIDTH || y >= HEIGHT || (y >= 0 && grid[x][y])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Places the current block on the grid and updates the color grid.
     */
    private void placeBlock() {
        for (Cell cell : currentBlock.getCells()) {
            int x = cell.getX();
            int y = cell.getY();
            if (y >= 0 && y < HEIGHT && x >= 0 && x < WIDTH) {
                grid[x][y] = true;
                colors[x][y] = currentBlock.getColor();
            }
        }
    }

    /**
     * Checks for and clears any full lines on the grid, applying gravity to drop the remaining cells.
     */
    private void checkLines() {
        List<Integer> fullRows = new ArrayList<>();
        for (int y = 0; y < HEIGHT; y++) {
            boolean full = true;
            for (int x = 0; x < WIDTH; x++) {
                if (!grid[x][y]) {
                    full = false;
                    break;
                }
            }
            if (full) fullRows.add(y);
        }

        score += fullRows.size() * 100;
        updateScore();

        // Apply gravity: shift cells down after clearing lines
        for (int x = 0; x < WIDTH; x++) {
            int writeY = HEIGHT - 1;
            for (int readY = HEIGHT - 1; readY >= 0; readY--) {
                if (!fullRows.contains(readY)) {
                    grid[x][writeY] = grid[x][readY];
                    colors[x][writeY] = colors[x][readY];
                    writeY--;
                }
            }
            while (writeY >= 0) {
                grid[x][writeY] = false;
                colors[x][writeY] = null;
                writeY--;
            }
        }
    }

    /**
     * Rotates the current block. If the rotation results in a collision, the change is reverted.
     */
    private void rotateBlock() {
        int[][] originalShape = currentBlock.getShape();
        int originalX = currentBlock.getX();
        int originalY = currentBlock.getY();

        currentBlock.rotate();
        currentBlock.centerAdjustment(originalShape);

        if (collision()) {
            currentBlock.setShape(originalShape);
            currentBlock.setX(originalX);
            currentBlock.setY(originalY);
        }
    }

    /**
     * Checks if the game is over by determining if any cells of the current block are above the board.
     *
     * @return True if the game is over, false otherwise.
     */
    private boolean isGameOver() {
        return currentBlock.getCells().stream()
                .anyMatch(c -> c.getY() < 0);
    }

    /**
     * Displays the game over dialog and offers the option to restart the game.
     */
    private void showGameOver() {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Game Over! Score: " + score + "\nPlay again?",
                    "Game Over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) startNewGame();
            else System.exit(0);
        });
    }

    /**
     * Updates the score label displayed at the top of the game window.
     */
    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * The main method to launch the Tetris game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TetrisGame().setVisible(true));
    }
}

/*

TetrisGame.java is the main class that integrates game state, rendering, user input, and game logic for the Tetris game.
It initializes the game window, manages the grid, score, and block queue, and handles block movement, rotation, collision detection,
and line clearing through a continuous game loop. User controls are mapped to actions (move left/right, rotate), and the game ends when a block
cannot be placed. The design leverages a block bag for random block generation and a separate GameBoard class for rendering, resulting in
a fully functional Tetris game.
*/
