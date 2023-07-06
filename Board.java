// Represents a board of tiles for a 2048 game.
public class Board {
    // Initialization of constants.
    public static final int NUM_ROWS_AND_COLS = 4; // number of rows and columns in board
    public static final int EMPTY = 1; // represents an empty tile

    // Represent directions to swipe tiles based on row and column change.
    public static final int[] UP = { -1, 0 };
    public static final int[] DOWN = { 1, 0 };
    public static final int[] LEFT = { 0, -1 };
    public static final int[] RIGHT = { 0, 1 };

    // Used to index direction arrays.
    public static final int ROW = 0;
    public static final int COL = 1;

    // Declaration of field.
    Tile[][] board; // array of tiles that represents game board

    // Board constructor creates a board with two randomly placed tiles.
    public Board() {
        createBoard();
        addTile();
        addTile();
    }

    // Initializes board with empty tiles.
    private void createBoard() {
        board = new Tile[NUM_ROWS_AND_COLS][NUM_ROWS_AND_COLS];
        for (int i = 0; i < NUM_ROWS_AND_COLS; i++) {
            for (int j = 0; j < NUM_ROWS_AND_COLS; j++) {
                board[i][j] = new Tile();
            }
        }
    }

    // Takes in a direction represented by an array and swipes in that direction.
    private void swipe(int[] direction) {
        findMergingTiles(direction);
        boolean merged = mergeTiles(direction);
        boolean shifted = shiftTiles(direction);
        if (merged || shifted) {
            addTile();
        }
    }

    // Swipes tiles upward.
    public void swipeUp() {
        swipe(UP);
    }

    // Swipes tiles downward.
    public void swipeDown() {
        swipe(DOWN);
    }

    // Swipes tiles to the left.
    public void swipeLeft() {
        swipe(LEFT);
    }

    // Swipes tiles to the right.
    public void swipeRight() {
        swipe(RIGHT);
    }

    // Iterates through board and finds tiles that need to merge, given swiping direction.
    private void findMergingTiles(int[] direction) {
        // The row and column that iteration starts at depends on direction.
        int rowIncrement = direction[ROW] <= 0 ? 1 : -1;
        int colIncrement = direction[COL] <= 0 ? 1 : -1;
        int startingRow = direction[ROW] <= 0 ? -direction[ROW] : NUM_ROWS_AND_COLS - 2;
        int startingCol = direction[COL] <= 0 ? -direction[COL] : NUM_ROWS_AND_COLS - 2;
        for (int row = startingRow; row >= 0 && row < NUM_ROWS_AND_COLS; row += rowIncrement) {
            for (int col = startingCol; col >= 0 && col < NUM_ROWS_AND_COLS; col += colIncrement) {
                Tile tile = board[row][col];
                if (!tile.isEmpty()) {
                    Tile nearestTile = findNearestTile(direction, row, col);
                    // Marks that this tile needs to merge if there is another tile of the same value in
                    // swiping direction.
                    if (tile.getValue() == nearestTile.getValue() && !nearestTile.getMerge()) {
                        tile.setMerge(true);
                    }
                }
            }
        }

    }

    // Finds the nearest non-empty tile given a starting tile position and direction.
    private Tile findNearestTile(int[] direction, int row, int col) {
        int rowIncrement = direction[ROW];
        int colIncrement = direction[COL];
        while (row + rowIncrement >= 0 && row + rowIncrement < NUM_ROWS_AND_COLS &&
                col + colIncrement >= 0 && col + colIncrement < NUM_ROWS_AND_COLS) {
            row += rowIncrement;
            col += colIncrement;
            if (board[row][col].getValue() != EMPTY) {
                return board[row][col];
            }
        }
        // Returns empty tile if no non-empty tile are near given tile.
        return board[row][col];
    }

    // Iterates through tiles and merges those that have been marked as needing to merge.
    private boolean mergeTiles(int[] direction) {
        boolean merged = false;
        // The row and column that iteration starts at depends on direction.
        int rowIncrement = direction[ROW] <= 0 ? 1 : -1;
        int colIncrement = direction[COL] <= 0 ? 1 : -1;
        int startingRow = direction[ROW] <= 0 ? -direction[ROW] : NUM_ROWS_AND_COLS - 2;
        int startingCol = direction[COL] <= 0 ? -direction[COL] : NUM_ROWS_AND_COLS - 2;
        for (int row = startingRow; row >= 0 && row < NUM_ROWS_AND_COLS; row += rowIncrement) {
            for (int col = startingCol; col >= 0 && col < NUM_ROWS_AND_COLS; col += colIncrement) {
                Tile tile = board[row][col];
                if (tile.getMerge()) {
                    merged = true;
                    // Doubling the nearest tile and emptying the merging tile is a merge.
                    findNearestTile(direction, row, col).doubleValue();
                    tile.emptyValue();
                    tile.setMerge(false);
                }
            }
        }
        return merged;
    }

    // Iterates through tiles and shifts tiles with empty spaces to the nearest non-empty tile in
    // given direction.
    private boolean shiftTiles(int[] direction) {
        boolean shifted = false;
        // The row and column that iteration starts at depends on direction.
        int rowIncrement = direction[ROW] <= 0 ? 1 : -1;
        int colIncrement = direction[COL] <= 0 ? 1 : -1;
        int startingRow = direction[ROW] <= 0 ? -direction[ROW] : NUM_ROWS_AND_COLS - 2;
        int startingCol = direction[COL] <= 0 ? -direction[COL] : NUM_ROWS_AND_COLS - 2;
        for (int row = startingRow; row >= 0 && row < NUM_ROWS_AND_COLS; row += rowIncrement) {
            for (int col = startingCol; col >= 0 && col < NUM_ROWS_AND_COLS; col += colIncrement) {
                if (!board[row][col].isEmpty()) {
                    int newRow = row;
                    int newCol = col;
                    int newRowIncrement = direction[ROW];
                    int newColIncrement = direction[COL];
                    // Keeps moving the tile until it is not next to an empty tile.
                    while (newRow + newRowIncrement >= 0 && newRow + newRowIncrement < NUM_ROWS_AND_COLS &&
                            newCol + newColIncrement >= 0 && newCol + newColIncrement < NUM_ROWS_AND_COLS &&
                            board[newRow + newRowIncrement][newCol + newColIncrement].isEmpty()) {
                        shifted = true;
                        board[newRow + newRowIncrement][newCol + newColIncrement].changeValue(board[newRow][newCol].getValue());
                        board[newRow][newCol].emptyValue();
                        newRow += newRowIncrement;
                        newCol += newColIncrement;
                    }
                }
            }
        }
        return shifted;
    }

    // Randomly places a tile on the board where there is currently no tile.
    private void addTile() {
        int testRow = (int) (Math.random() * NUM_ROWS_AND_COLS);
        int testCol = (int) (Math.random() * NUM_ROWS_AND_COLS);
        while (!board[testRow][testCol].isEmpty()) {
            testRow = (int) (Math.random() * NUM_ROWS_AND_COLS);
            testCol = (int) (Math.random() * NUM_ROWS_AND_COLS);
        }
        int chance = (int) (Math.random() * 10);
        board[testRow][testCol].changeValue(chance == 1 ? 4 : 2);
    }

    // Returns a multidimensional array representing the board in integers.
    public int[][] getBoardRepresentation() {
        int[][] boardRepresentation = new int[NUM_ROWS_AND_COLS][NUM_ROWS_AND_COLS];
        for (int row = 0; row < NUM_ROWS_AND_COLS; row++) {
            for (int col = 0; col < NUM_ROWS_AND_COLS; col++) {
                boardRepresentation[row][col] = board[row][col].getValue();
            }
        }
        return boardRepresentation;
    }

    // Returns a string representation of the board.
    public String toString() {
        String boardRepresentation = "";
        for (int i = 0; i < NUM_ROWS_AND_COLS; i++) {
            for (int j = 0; j < NUM_ROWS_AND_COLS; j++) {
                Tile tile = board[i][j];
                if (tile.getValue() != EMPTY) {
                    boardRepresentation += String.format("%5s", "" + tile.getValue());
                } else {
                    boardRepresentation += String.format("%5s", "-");
                }
                if (j == NUM_ROWS_AND_COLS - 1 && i != NUM_ROWS_AND_COLS - 1) {
                    boardRepresentation += "\n";
                } else if (!(j == NUM_ROWS_AND_COLS - 1 && i == NUM_ROWS_AND_COLS - 1)) {
                    boardRepresentation += " ";
                }
            }
        }
        return boardRepresentation;
    }
}