import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

// Displays and spawns tiles of 2048 game.
public class GameBoard extends JPanel implements ActionListener {

    // Initialization of constants.
    public static final int NUM_ROWS_AND_COLS = 4;
    public static final int TILE_SIZE = 112;
    public static final int ROW_COL_SIZE = 122;
    public static final int BORDER_SIZE = 11;
    public static final int ROW = 0;
    public static final int COL = 1;

    public static final int[] UP = { -1, 0 };
    public static final int[] DOWN = { 1, 0 };
    public static final int[] LEFT = { 0, -1 };
    public static final int[] RIGHT = { 0, 1 };
    public static final int[][] DIRECTIONS = { UP, DOWN, LEFT, RIGHT };

    // Colors of tiles. Array position corresponds to power of 2 that tile value is.
    public static final Color[] TILE_COLORS = { new Color(204, 192, 178), new Color(236, 224, 215),
        new Color(237, 223, 199), new Color(241, 177, 120), new Color(245, 149, 98),
        new Color(245, 124, 94), new Color(246, 94, 60) , new Color(238, 208, 113),
        new Color(238, 205, 99), new Color(236, 199, 79), new Color(239, 197, 63),
        new Color(238, 194, 46), new Color(62, 57, 51) } ;

    private ArrayList<GameTile> existingTiles;
    private GameTile[][] boardRepresentation;
    private ScoreBoard scoreBoard;
    private int[] currentDirection;
    private Timer timer;
    private Font gameFont;
    private boolean moveInProgress;
    private boolean spawnTile;
    
    // Constructs GameBoard connected to given ScoreBoard.
    public GameBoard(ScoreBoard scoreBoard, Font gameFont) {
        this.scoreBoard = scoreBoard;
        this.gameFont = gameFont;
        this.currentDirection = new int[] {0, 0};
        this.timer = new Timer(1, this);
        setUpBoardBase();
    }

    // Sets up GameBoard and spawns two tiles to begin with.
    private void setUpBoardBase() {
        setPreferredSize(new Dimension(500, 500));
        existingTiles = new ArrayList<>();
        existingTiles.add(new GameTile(true));
        existingTiles.add(new GameTile(true));
        boardRepresentation = getBoardRepresentation(false);
    }

    // Returns an multidimensional array of GameTile's to represent tiles on GameBoard.
    private GameTile[][] getBoardRepresentation(boolean postMerge) {
        GameTile[][] boardRepresentation = new GameTile[NUM_ROWS_AND_COLS][NUM_ROWS_AND_COLS];
        for (GameTile existingTile : existingTiles) {
            if (!existingTile.initialMerge) {
                int row = convertCoordinate(!postMerge ? existingTile.y : existingTile.targetY);
                int col = convertCoordinate(!postMerge ? existingTile.x : existingTile.targetX);
                boardRepresentation[row][col] = existingTile;
            }
        }
        return boardRepresentation;
    }

    // Moves tiles upwards.
    public void swipeUp() {
        swipe(UP);
    }

    // Moves tiles downwards.
    public void swipeDown() {
        swipe(DOWN);
    }

    // Moves tiles leftwards.
    public void swipeLeft() {
        swipe(LEFT);
    }

    // Moves tiles rightwards.
    public void swipeRight() {
        swipe(RIGHT);
    }

    // Moves tiles in given direction.
    private void swipe(int[] direction) {
        moveInProgress = true;
        currentDirection = direction;
        if (timer.isRunning()) {
            timer.stop();
        }
        boolean merged = mergeTiles(direction, true);
        boolean shifted = shiftTiles(direction, true);
        if (merged || shifted) {
            timer.start();
        } else {
            moveInProgress = false;
        }
    }

    // Returns whether there is a move in progress.
    public boolean moveInProgress() {
        return moveInProgress;
    }

    // Searches for tiles that need to merge and updates their target positions. Returns if a merge occurs.
    private boolean mergeTiles(int[] direction, boolean makeMove) {
        // The row and column that iteration starts at depends on direction.
        boolean merged = false;
        int rowIncrement = direction[ROW] <= 0 ? 1 : -1;
        int colIncrement = direction[COL] <= 0 ? 1 : -1;
        int startingRow = direction[ROW] <= 0 ? -direction[ROW] : NUM_ROWS_AND_COLS - 2;
        int startingCol = direction[COL] <= 0 ? -direction[COL] : NUM_ROWS_AND_COLS - 2;
        for (int row = startingRow; row >= 0 && row < NUM_ROWS_AND_COLS; row += rowIncrement) {
            for (int col = startingCol; col >= 0 && col < NUM_ROWS_AND_COLS; col += colIncrement) {
                GameTile tile = boardRepresentation[row][col];
                if (tile != null) {
                    GameTile nearestTile = findNearestTile(direction, row, col);
                    // Marks that this tile needs to merge if there is another tile of the same value in
                    // swiping direction.
                    if (nearestTile != null && tile.value == nearestTile.value && !nearestTile.initialMerge) {
                        if (!makeMove) {
                            return true;
                        }
                        merged = true;
                        tile.initialMerge = true;
                        nearestTile.finalMerge = true;
                        tile.targetX = nearestTile.x;
                        tile.targetY = nearestTile.y;
                        scoreBoard.updateScore(tile.value * 2);
                    }
                }
            }
        }
        return merged;
    }

    // Finds the tile closest to tile at given row and col in given direction.
    private GameTile findNearestTile(int[] direction, int row, int col) {
        int rowIncrement = direction[ROW];
        int colIncrement = direction[COL];
        while (row + rowIncrement >= 0 && row + rowIncrement < NUM_ROWS_AND_COLS &&
                col + colIncrement >= 0 && col + colIncrement < NUM_ROWS_AND_COLS) {
            row += rowIncrement;
            col += colIncrement;
            if (boardRepresentation[row][col] != null) {
                return boardRepresentation[row][col];
            }
        }
        // Returns null if no non-empty tile are near given tile.
        return null;
    }

    // Searches for tiles that need to shift and updates their target positions. Returns if a shift occurs.
    private boolean shiftTiles(int[] direction, boolean makeMove) {
        boolean shifted = false;
        // Bases shifts off positions of tiles after their merge.
        boardRepresentation = getBoardRepresentation(true);
        // The row and column that iteration starts at depends on direction.
        int rowIncrement = direction[ROW] <= 0 ? 1 : -1;
        int colIncrement = direction[COL] <= 0 ? 1 : -1;
        int startingRow = direction[ROW] <= 0 ? -direction[ROW] : NUM_ROWS_AND_COLS - 2;
        int startingCol = direction[COL] <= 0 ? -direction[COL] : NUM_ROWS_AND_COLS - 2;
        for (int row = startingRow; row >= 0 && row < NUM_ROWS_AND_COLS; row += rowIncrement) {
            for (int col = startingCol; col >= 0 && col < NUM_ROWS_AND_COLS; col += colIncrement) {
                GameTile tile = boardRepresentation[row][col];
                if (tile != null) {
                    int initialRow = row;
                    int initialCol = col;
                    int rowChange = direction[ROW];
                    int colChange = direction[COL];
                    while (initialRow + rowChange >= 0 && initialRow + rowChange < NUM_ROWS_AND_COLS &&
                            initialCol + colChange >= 0 && initialCol + colChange < NUM_ROWS_AND_COLS &&
                            boardRepresentation[initialRow + rowChange][initialCol + colChange] == null) {
                        if (!makeMove) {
                            return true;
                        }
                        shifted = true;
                        initialRow += rowChange;
                        initialCol += colChange;
                    }
                    tile.targetX = convertRowAndCol(initialCol);
                    tile.targetY = convertRowAndCol(initialRow);
                    boardRepresentation = getBoardRepresentation(true);
                }
            }
        }
        return shifted;
    }

    public boolean gameOver() {
        for (int[] direction : DIRECTIONS) {
            if (mergeTiles(direction, false) || shiftTiles(direction, false)) {
                return false;
            }
        }
        return true;
    }

    // Converts coordinate to corresponding row or column.
    private int convertCoordinate(int coordinate) {
        return (coordinate - BORDER_SIZE) / ROW_COL_SIZE;
    }

    // Converts row or column to corresponding coordinate.
    private int convertRowAndCol(int rowAndCol) {
        return rowAndCol * ROW_COL_SIZE + BORDER_SIZE;
    }

    // Draws the game board design with updated tile values and positions.
    public void paint(Graphics g) {
        super.paint(g);
        
        // Initialization of constants used to draw game board.
        final Color BACKGROUND_COLOR = new Color(188, 172, 158);
        final Color EMPTY_TILE_COLOR = new Color(204, 192, 178);
        final Color FONT_COLOR_LARGE = new Color(250, 247, 241);
        final Color FONT_COLOR_SMALL = new Color(118, 111, 100);

        final int ARC = 10;
        final int BOARD_LENGTH = 500;
        final int FONT_SIZE_LARGE = 39;
        final int FONT_SIZE_MEDIUM = 50;
        final int FONT_SIZE_SMALL = 60;

        // Sets up the base of game board.
        g.setColor(BACKGROUND_COLOR);
        g.fillRoundRect(0, 0, BOARD_LENGTH, BOARD_LENGTH, ARC, ARC);

        // Draws empty tiles at every tile position.
        g.setColor(EMPTY_TILE_COLOR);
        for (int row = 0; row < NUM_ROWS_AND_COLS; row++) {
            for (int col = 0; col < NUM_ROWS_AND_COLS; col++) {
                g.fillRoundRect(convertRowAndCol(col), convertRowAndCol(row), TILE_SIZE, TILE_SIZE, ARC, ARC);
            }
        }

        // Draws every tile at their current x and y position.
        for (GameTile existingTile : existingTiles) {
            int tileValue = existingTile.value;
            String tileValueDisplay = "" + tileValue;
            int xValue = existingTile.x;
            int yValue = existingTile.y;
            int fontSize = tileValue < 128 ? FONT_SIZE_SMALL : tileValue < 1024 ? FONT_SIZE_MEDIUM : FONT_SIZE_LARGE;
            Color fontColor = tileValue < 8 ? FONT_COLOR_SMALL : FONT_COLOR_LARGE;

            // Draws tile with correct color and at correct position.
            g.setColor((TILE_COLORS[(int) (Math.log10(tileValue) / Math.log10(2))]));
            g.fillRoundRect(xValue, yValue, existingTile.size, existingTile.size, ARC, ARC);

            // Draws tile value at the center of drawn tile.
            if (!existingTile.newTile) {
                g.setFont(gameFont.deriveFont((float) fontSize));
                g.setColor(fontColor);
                drawCenteredText(g, tileValueDisplay, xValue, yValue);
            }
        }
    }

    // Draws given text centered in a rectangle at given x value and y value.
    private void drawCenteredText(Graphics g, String textDisplay, int rectXValue, int rectYValue) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int fontXValue = (rectXValue + TILE_SIZE / 2) - (int) (r.getWidth() / 2);
        int fontYValue = (rectYValue + TILE_SIZE / 2) - (int) (fm.getHeight() / 2) + fm.getAscent();
        g.drawString(textDisplay, fontXValue, fontYValue);
    }

    // When timer has started, updates positions of tiles every one millisecond. Gives tiles a sliding animation.
    public void actionPerformed(ActionEvent e) {
        boolean moved = false;
        // Iterates through each tile and searches for a tile that needs to move.
        for (GameTile existingTile : existingTiles) {
            if ((existingTile.x != existingTile.targetX || existingTile.y != existingTile.targetY) && !existingTile.newTile) {
                existingTile.move(currentDirection);
                moved = true;
                // Indicates that move made a change in board, and a tile can be spawned now.
                spawnTile = true;
            }
        }
        // Iterated through all tiles and none changed positions. Turn is complete.
        if (!moved) {
            for (int i = existingTiles.size() - 1; i >= 0; i--) {
                GameTile existingTile = existingTiles.get(i);
                if (existingTile.initialMerge) {
                    existingTiles.remove(existingTile);
                }
                if (existingTile.finalMerge) {
                    existingTile.doubleValue();
                    existingTile.finalMerge = false;
                }
            }
            if (spawnTile) {
                existingTiles.add(new GameTile());
                spawnTile = false;
            }
            // Grows new tiles until they are the full tile size.
            for (GameTile existingTile : existingTiles) {
                if (existingTile.newTile) {
                    existingTile.grow();
                    if (existingTile.x == existingTile.targetX && existingTile.y == existingTile.targetY) {
                        existingTile.newTile = false;
                        moveInProgress = false;
                    }
                }
            }
            boardRepresentation = getBoardRepresentation(false);
        }
        repaint();
    }

    // Nested class that represents a tile.
    private class GameTile {
        
        // Initialization of constants.
        private static final int COMMON_STARTING_VALUE = 2;
        private static final int RARE_STARTING_VALUE = 4;
        
        private int x;
        private int y;
        private int targetX;
        private int targetY;
        private int value;
        private int size;
        private boolean initialMerge;
        private boolean finalMerge;
        private boolean newTile;
    
        // Constructor that gives tile a random x and y position where there is no current tile.
        private GameTile() {
            boolean duplicate = false;
            do {
                duplicate = false;
                targetX = convertRowAndCol((int) (Math.random() * NUM_ROWS_AND_COLS));
                targetY = convertRowAndCol((int) (Math.random() * NUM_ROWS_AND_COLS));
                for (GameTile existingTile : existingTiles) {
                    if (existingTile.x == targetX && existingTile.y == targetY) {
                        duplicate = true;
                    }
                }
            } while (duplicate);
            size = 4;
            x = targetX + (TILE_SIZE / 2) - 2;
            y = targetY + (TILE_SIZE / 2) - 2;
            newTile = true;
            // Tile has a one in ten chance of spawning with a 4 value.
            int chance = (int) (Math.random() * 10);
            value =  chance == 0 ? RARE_STARTING_VALUE : COMMON_STARTING_VALUE;
        }

        // Constructor for starting tiles. Starting tiles do not need to grow.
        private GameTile(boolean startingTile) {
            boolean duplicate = false;
            do {
                duplicate = false;
                x = convertRowAndCol((int) (Math.random() * NUM_ROWS_AND_COLS));
                y = convertRowAndCol((int) (Math.random() * NUM_ROWS_AND_COLS));
                for (GameTile existingTile : existingTiles) {
                    if (existingTile.x == x && existingTile.y == y) {
                        duplicate = true;
                    }
                }
            } while (duplicate);
            size = TILE_SIZE;
            targetX = x;
            targetY = y;
            int chance = (int) (Math.random() * 10);
            value =  chance == 0 ? RARE_STARTING_VALUE : COMMON_STARTING_VALUE;
        }
     
        // Moves tile in given direction.
        private void move(int[] direction) {
            final int SPEED = 9;
            if (Math.abs(x - targetX) >= SPEED || Math.abs(y - targetY) >= SPEED) {
                x += direction[COL] * SPEED;
                y += direction[ROW] * SPEED;
            } else {
                x += direction[COL];
                y += direction[ROW];
            }
        }

        // Grows tile and makes tile centered based on target position.
        private void grow() {
            final int SPEED = 3;
            if (TILE_SIZE - size >= SPEED) {
                size += SPEED;
            } else {
                size++;
            }
            x = (targetX + TILE_SIZE / 2) - (int) (size / 2);
            y = (targetY + TILE_SIZE / 2) - (int) (size / 2);
        }

        // Doubles tile value.
        private void doubleValue() {
            value *= 2;
        }
    }
}