import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener {

    public static final int NUM_ROWS_AND_COLS = 4;
    public static final int TILE_SIZE = 112;
    public static final int ROW_COL_SIZE = 122;
    public static final int BORDER_SIZE = 11;

    public static final int[] UP = { -1, 0 };
    public static final int[] DOWN = { 1, 0 };
    public static final int[] LEFT = { 0, -1 };
    public static final int[] RIGHT = { 0, 1 };
    public static final int ROW = 0;
    public static final int COL = 1;

    private int[] currentDirection;
    private boolean spawnTile;
    private Timer timer;

    // array of colors; array position corresponds to power of 2 that tile value is
    public static final Color[] TILE_COLORS = { new Color(204, 192, 178), new Color(236, 224, 215),
        new Color(237, 223, 199), new Color(241, 177, 120), new Color(245, 149, 98),
        new Color(245, 124, 94), new Color(246, 94, 60) , new Color(238, 208, 113),
        new Color(238, 205, 99), new Color(236, 199, 79), new Color(239, 197, 63),
        new Color(238, 194, 46), new Color(62, 57, 51) } ;

    private ArrayList<GameTile> existingTiles;
    private GameTile[][] boardRepresentation;

    public GameBoard() {
        currentDirection = new int[] {0, 0};
        timer = new Timer(1, this);
        setUpBoardBase();
        boardRepresentation = getBoardRepresentation(false);
    }

    private void setUpBoardBase() {
        setBorder(BorderFactory.createLineBorder(new Color(188, 172, 158), 10));
        setLayout(new GridLayout(NUM_ROWS_AND_COLS, NUM_ROWS_AND_COLS, 10, 10));
        setBackground(new Color(188, 172, 158));
        setPreferredSize(new Dimension(500, 500));
        JPanel[] panels = new JPanel[NUM_ROWS_AND_COLS * NUM_ROWS_AND_COLS];
        for (int i = 0; i < NUM_ROWS_AND_COLS * NUM_ROWS_AND_COLS; i++) {
            panels[i] = new JPanel();
            panels[i].setBackground(new Color(204, 192, 178));
            add(panels[i]);
        }
        existingTiles = new ArrayList<>();
        existingTiles.add(new GameTile(existingTiles));
        existingTiles.add(new GameTile(existingTiles));
    }

    private GameTile[][] getBoardRepresentation(boolean merged) {
        GameTile[][] boardRepresentation = new GameTile[NUM_ROWS_AND_COLS][NUM_ROWS_AND_COLS];
        for (GameTile existingTile : existingTiles) {
            if (!existingTile.initialMerge()) {
                int row = convertCoordinate(!merged ? existingTile.getY() : existingTile.getTargetY());
                int col = convertCoordinate(!merged ? existingTile.getX() : existingTile.getTargetX());
                boardRepresentation[row][col] = existingTile;
            }
        }
        return boardRepresentation;
    }

    private void swipe(int[] direction) {
        GameLauncher.inProgress = true;
        currentDirection = direction;
        boolean merged = mergeTiles(direction);
        boolean shifted = shiftTiles(direction);
        if (timer.isRunning()) {
                timer.stop();
        }
        if (merged || shifted) {
            timer.start();
        } else {
            GameLauncher.inProgress = false;
        }
    }

    private boolean mergeTiles(int[] direction) {
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
                    if (nearestTile != null && tile.getValue() == nearestTile.getValue() && !nearestTile.initialMerge()) {
                        merged = true;
                        tile.changeInitialMerge(true);
                        nearestTile.changeFinalMerge(true);
                        tile.setTargetX(nearestTile.getX());
                        tile.setTargetY(nearestTile.getY());
                    }
                }
            }
        }
        return merged;
    }

    private boolean shiftTiles(int[] direction) {
        boolean shifted = false;
        boardRepresentation = getBoardRepresentation(true);
        int rowIncrement = direction[ROW] <= 0 ? 1 : -1;
        int colIncrement = direction[COL] <= 0 ? 1 : -1;
        int startingRow = direction[ROW] <= 0 ? -direction[ROW] : NUM_ROWS_AND_COLS - 2;
        int startingCol = direction[COL] <= 0 ? -direction[COL] : NUM_ROWS_AND_COLS - 2;
        for (int row = startingRow; row >= 0 && row < NUM_ROWS_AND_COLS; row += rowIncrement) {
            for (int col = startingCol; col >= 0 && col < NUM_ROWS_AND_COLS; col += colIncrement) {
                if (boardRepresentation[row][col] != null) {
                    int initialRow = row;
                    int initialCol = col;
                    int rowChange = direction[ROW];
                    int colChange = direction[COL];
                    while (initialRow + rowChange >= 0 && initialRow + rowChange < NUM_ROWS_AND_COLS &&
                            initialCol + colChange >= 0 && initialCol + colChange < NUM_ROWS_AND_COLS &&
                            boardRepresentation[initialRow + rowChange][initialCol + colChange] == null) {
                        shifted = true;
                        initialRow += rowChange;
                        initialCol += colChange;
                    }
                    boardRepresentation[row][col].setTargetX(convertRowAndCol(initialCol));
                    boardRepresentation[row][col].setTargetY(convertRowAndCol(initialRow));
                    boardRepresentation = getBoardRepresentation(true);
                }
            }
        }
        return shifted;
    }

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
        // Returns empty tile if no non-empty tile are near given tile.
        return null;
    }

    public void swipeUp() {
        swipe(UP);
    }

    public void swipeDown() {
        swipe(DOWN);
    }

    public void swipeLeft() {
        swipe(LEFT);
    }

    public void swipeRight() {
        swipe(RIGHT);
    }

    private int convertCoordinate(int coordinate) {
        return (coordinate - BORDER_SIZE) / ROW_COL_SIZE;
    }

    private int convertRowAndCol(int rowAndCol) {
        return rowAndCol * ROW_COL_SIZE + BORDER_SIZE;
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (GameTile existingTile : existingTiles) {
            int tileValue = existingTile.getValue();
            String tileValueDisplay = "" + tileValue;

            int xValue = existingTile.getX();
            int yValue = existingTile.getY();
            int fontSize = tileValue < 128 ? 50 : tileValue < 1024 ? 45 : 30;
            Color fontColor = tileValue < 8 ? new Color(118, 111, 100) : new Color(250, 247, 241);
            g.setColor((TILE_COLORS[(int) (Math.log10(tileValue) / Math.log10(2))]));
            g.fillRect(xValue, yValue, TILE_SIZE, TILE_SIZE);

            g.setFont(new Font(null, Font.BOLD, fontSize));
            g.setColor(fontColor);

            Graphics2D g2d = (Graphics2D) g;
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(tileValueDisplay, g2d);
            
            int fontXValue = (xValue + TILE_SIZE / 2) - (int) (r.getWidth() / 2);
            int fontYValue = (yValue + TILE_SIZE / 2) + (int) (r.getHeight() / 3);
            g.drawString(tileValueDisplay, fontXValue, fontYValue);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean moved = false;
        for (GameTile existingTile : existingTiles) {
            if ((existingTile.getX() != existingTile.getTargetX() || existingTile.getY() != existingTile.getTargetY()) && !existingTile.finalMerge()) {
                existingTile.move(currentDirection);
                moved = true;
                spawnTile = true;
            }
        }
        repaint();
        if (!moved) {
            boolean finalMerges = false;
            for (int i = existingTiles.size() - 1; i >= 0; i--) {
                GameTile existingTile = existingTiles.get(i);
                if (existingTile.initialMerge()) {
                    existingTiles.remove(existingTile);
                }
                if (existingTile.finalMerge()) {
                    finalMerges = true;
                    if (existingTile.getX() != existingTile.getTargetX() || existingTile.getY() != existingTile.getTargetY()) {
                        existingTile.move(currentDirection);
                    }
                    existingTile.doubleValue();
                    existingTile.changeFinalMerge(false);
                }
            }
            if (spawnTile && !finalMerges) {
                existingTiles.add(new GameTile(existingTiles));
                spawnTile = false;
                GameLauncher.inProgress = false;
            }
            boardRepresentation = getBoardRepresentation(false);
            repaint();
        }
    }
}