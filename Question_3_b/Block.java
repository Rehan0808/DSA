package Question_3_b; 

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/*
 * File: Block.java
 * ---------------
 * This class represents a Tetris block. A block consists of a 2D shape (represented as a 2D array),
 * a color, and a position (x, y) on the game board.
 *
 * Key functionalities include:
 * - Moving the block by a given offset.
 * - Adjusting its center based on a given original shape.
 * - Rotating the block (90 degrees clockwise).
 * - Converting the block's shape into individual cells (used for rendering and collision detection).
 *
 * The methods provided allow the Tetris game to manipulate the block's position and orientation.
 *
 * Summary:
 * The Block class encapsulates the state and behavior of a Tetris block. It allows moving, rotating,
 * and adjusting the block, as well as obtaining a list of its active cells (where the shape equals 1).
 * These features enable the game logic to display and manage Tetris blocks properly.
 */

public class Block {
    private int[][] shape;
    private Color color;
    private int x;
    private int y;

    /**
     * Constructor for Block.
     *
     * @param shape A 2D array representing the shape of the block.
     * @param color The color of the block.
     */
    public Block(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 4;  // Start position on the board
        this.y = 0;
    }

    /**
     * Moves the block by the specified offsets.
     *
     * @param dx The change in the x-coordinate.
     * @param dy The change in the y-coordinate.
     */
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Adjusts the block's x-coordinate to center it based on the original shape's width.
     *
     * @param originalShape The original shape before any rotations or adjustments.
     */
    public void centerAdjustment(int[][] originalShape) {
        int xOffset = (originalShape[0].length - shape[0].length) / 2;
        x += xOffset;
    }

    /**
     * Rotates the block 90 degrees clockwise.
     */
    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                rotated[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }

    /**
     * Retrieves the list of cells occupied by the block.
     *
     * @return A list of Cell objects representing the block's occupied cells.
     */
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    cells.add(new Cell(x + j, y + i, color));
                }
            }
        }
        return cells;
    }

    // Getters and setters for block attributes

    /**
     * Returns the current shape of the block.
     *
     * @return A 2D array representing the block's shape.
     */
    public int[][] getShape() { return shape; }

    /**
     * Sets the shape of the block.
     *
     * @param shape The new shape of the block.
     */
    public void setShape(int[][] shape) { this.shape = shape; }

    /**
     * Returns the color of the block.
     *
     * @return The block's color.
     */
    public Color getColor() { return color; }

    /**
     * Returns the current x-coordinate of the block.
     *
     * @return The x-coordinate.
     */
    public int getX() { return x; }

    /**
     * Returns the current y-coordinate of the block.
     *
     * @return The y-coordinate.
     */
    public int getY() { return y; }

    /**
     * Sets the x-coordinate of the block.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) { this.x = x; }

    /**
     * Sets the y-coordinate of the block.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) { this.y = y; }
}

/*

In Block.java, we implemented the Block class for a Tetris game. The class defines how a block is represented
(using a shape, color, and position), and provides methods for moving, rotating, and centering the block.
Additionally, it allows retrieving the block's cells, which is essential for rendering and collision detection.
*/
