package Question_3_b;

import java.awt.Color;

/*
 * File: Cell.java
 * ---------------
 * This class represents an individual cell (or block unit) in the Tetris game.
 * Each cell has an x and y coordinate along with an associated color.
 *
 * Cells are used to represent the parts of a Tetris block as well as the occupied cells on the game board.
 *
 * Summary:
 * The Cell class encapsulates the position and appearance (color) of a single Tetris cell, 
 * providing basic getters and setters to support game rendering and logic.
 */

public class Cell {
    private int x;
    private int y;
    private Color color;

    /**
     * Constructor for Cell.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @param color The color of the cell.
     */
    public Cell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Gets the x-coordinate of the cell.
     *
     * @return The x-coordinate.
     */
    public int getX() { return x; }

    /**
     * Gets the y-coordinate of the cell.
     *
     * @return The y-coordinate.
     */
    public int getY() { return y; }

    /**
     * Gets the color of the cell.
     *
     * @return The cell's color.
     */
    public Color getColor() { return color; }

    /**
     * Sets the y-coordinate of the cell.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) { this.y = y; }
}

/*

Cell.java defines a simple Cell class that encapsulates the position (x, y) and color of an individual
Tetris cell. This class is used by Block.java and GameBoard.java to render Tetris blocks and track the game state.
*/
