package Question_3_b;

import java.awt.*;
import javax.swing.*;

/*
 * File: GameBoard.java
 * ---------------
 * This class extends JPanel and serves as the main drawing canvas for the Tetris game.
 * It is responsible for rendering the game grid, placed blocks, the current falling block, 
 * and a preview of the next block.
 *
 * Key functionalities include:
 * - Drawing the game grid.
 * - Rendering placed blocks on the grid.
 * - Rendering the current falling block.
 * - Rendering a preview area for the next block.
 *
 * Summary:
 * GameBoard is the view component for the Tetris game, handling all graphical rendering using Swing.
 * It draws the game state based on the data provided by the TetrisGame instance.
 */

public class GameBoard extends JPanel {
    private final TetrisGame game;

    /**
     * Constructor for GameBoard.
     *
     * @param game The TetrisGame instance that provides the game state to be rendered.
     */
    public GameBoard(TetrisGame game) {
        this.game = game;
        setPreferredSize(new Dimension(450, 600));
    }

    /**
     * Overridden method to paint the component.
     * It draws the grid, blocks, the current falling block, and the next block preview.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawBlocks(g);
        drawCurrentBlock(g);
        drawPreview(g);
    }

    /**
     * Draws the grid lines for the Tetris board.
     *
     * @param g The Graphics object used for drawing.
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x < TetrisGame.WIDTH; x++) {
            for (int y = 0; y < TetrisGame.HEIGHT; y++) {
                g.drawRect(x * 30, y * 30, 30, 30);
            }
        }
    }

    /**
     * Draws the blocks that have been placed on the grid.
     *
     * @param g The Graphics object used for drawing.
     */
    private void drawBlocks(Graphics g) {
        boolean[][] grid = game.getGrid();
        Color[][] colors = game.getColors();
        for (int x = 0; x < TetrisGame.WIDTH; x++) {
            for (int y = 0; y < TetrisGame.HEIGHT; y++) {
                if (grid[x][y]) {
                    g.setColor(colors[x][y]);
                    g.fillRect(x * 30 + 1, y * 30 + 1, 28, 28);
                }
            }
        }
    }

    /**
     * Draws the current falling block.
     *
     * @param g The Graphics object used for drawing.
     */
    private void drawCurrentBlock(Graphics g) {
        Block current = game.getCurrentBlock();
        if (current != null) {
            g.setColor(current.getColor());
            for (Cell cell : current.getCells()) {
                int x = cell.getX();
                int y = cell.getY();
                if (y >= 0) {
                    g.fillRect(x * 30 + 1, y * 30 + 1, 28, 28);
                }
            }
        }
    }

    /**
     * Draws the preview of the next block in a designated preview area.
     *
     * @param g The Graphics object used for drawing.
     */
    private void drawPreview(Graphics g) {
        int previewX = 330;
        int previewY = 50;
        int previewSize = 100;

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(previewX, previewY, previewSize, previewSize);
        g.setColor(Color.BLACK);
        g.drawString("Next Block:", previewX + 10, previewY - 10);

        Block next = game.getNextBlock();
        if (next != null) {
            int[][] shape = next.getShape();
            int startX = previewX + (previewSize - shape[0].length * 20) / 2;
            int startY = previewY + (previewSize - shape.length * 20) / 2;

            g.setColor(next.getColor());
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        g.fillRect(startX + j * 20, startY + i * 20, 19, 19);
                    }
                }
            }
        }
    }
}

/*

GameBoard.java defines the view component of the Tetris game using Swing. It is responsible for drawing:
- The grid that forms the Tetris board.
- The blocks that have been placed.
- The current falling block.
- A preview of the next block.
The GameBoard class uses the state provided by the TetrisGame instance to render the game appropriately.
*/
