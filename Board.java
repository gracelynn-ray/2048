public class Board {
    public static final int NUM_ROWS = 4;
    public static final int NUM_COLS = 4;
    public static final int EMPTY = 1;

    Tile[][] board;

    public Board() {
        createBoard();
        addTile();
        addTile();
    }

    private void createBoard() {
        board = new Tile[NUM_ROWS][NUM_COLS];
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                board[i][j] = new Tile();
            }
        }
    }

    public void swipeUp() {
        findMergingTilesUp();
        boolean merged = mergeTilesUp();
        boolean shifted = shiftTilesUp();
        if (merged || shifted) {
            addTile();
        }
    }

    private void findMergingTilesUp() {
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = 1; row < NUM_ROWS; row++) {
                Tile tile = board[row][col];
                if (tile.getValue() != EMPTY) {
                    Tile nearestTile = findNearestTileUp(row, col);
                    if (tile.getValue() == nearestTile.getValue() && !nearestTile.getMerge()) {
                        tile.setMerge(true);
                    }
                }
            }
        }
    }

    private Tile findNearestTileUp(int row, int col) {
        while (row != 0) {
            row--;
            if (board[row][col].getValue() != EMPTY) {
                return board[row][col];
            }
        }
        return board[row][col];
    }

    private boolean mergeTilesUp() {
        boolean merged = false;
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = 1; row < NUM_ROWS; row++) {
                Tile tile = board[row][col];
                if (tile.getMerge()) {
                    merged = true;
                    Tile nearestTile = findNearestTileUp(row, col);
                    nearestTile.doubleValue();
                    tile.emptyValue();
                    tile.setMerge(false);
                }
            }
        }
        return merged;
    }

    private boolean shiftTilesUp() {
        boolean shifted = false;
        for (int row = 1; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (board[row][col].getValue() != EMPTY) {
                    int newRow = row;
                    while (newRow != 0 && board[newRow - 1][col].getValue() == EMPTY) {
                        shifted = true;
                        board[newRow - 1][col].changeValue(board[newRow][col].getValue());
                        board[newRow][col].emptyValue();
                        newRow--;
                    }
                }
            }
        }
        return shifted;
    }

    public void swipeDown() {
        findMergingTilesDown();
        boolean merged = mergeTilesDown();
        boolean shifted = shiftTilesDown();
        if (merged || shifted) {
            addTile();
        }
    }

    private void findMergingTilesDown() {
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = NUM_ROWS - 2; row >= 0; row--) {
                Tile tile = board[row][col];
                if (tile.getValue() != EMPTY) {
                    Tile nearestTile = findNearestTileDown(row, col);
                    if (tile.getValue() == nearestTile.getValue() && !nearestTile.getMerge()) {
                        tile.setMerge(true);
                    }
                }
            }
        }
    }

    private Tile findNearestTileDown(int row, int col) {
        while (row != NUM_ROWS - 1) {
            row++;
            if (board[row][col].getValue() != EMPTY) {
                return board[row][col];
            }
        }
        return board[row][col];
    }

    private boolean mergeTilesDown() {
        boolean merged = false;
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = NUM_ROWS - 2; row >= 0; row--) {
                Tile tile = board[row][col];
                if (tile.getMerge()) {
                    merged = true;
                    Tile nearestTile = findNearestTileDown(row, col);
                    nearestTile.doubleValue();
                    tile.emptyValue();
                    tile.setMerge(false);
                }
            }
        }
        return merged;
    }

    private boolean shiftTilesDown() {
        boolean shifted = false;
        for (int row = NUM_ROWS - 2; row >= 0; row--) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (board[row][col].getValue() != EMPTY) {
                    int newRow = row;
                    while (newRow != NUM_ROWS - 1 && board[newRow + 1][col].getValue() == EMPTY) {
                        shifted = true;
                        board[newRow + 1][col].changeValue(board[newRow][col].getValue());
                        board[newRow][col].emptyValue();
                        newRow++;
                    }
                }
            }
        }
        return shifted;
    }

    public void swipeLeft() {
        findMergingTilesLeft();
        boolean merged = mergeTilesLeft();
        boolean shifted = shiftTilesLeft();
        if (merged || shifted) {
            addTile();
        }
    }

    private void findMergingTilesLeft() {
        for (int col = 1; col < NUM_COLS; col++) {
            for (int row = 0; row < NUM_ROWS; row++) {
                Tile tile = board[row][col];
                if (!tile.isEmpty()) {
                    Tile nearestTile = findNearestTileLeft(row, col);
                    if (tile.getValue() == nearestTile.getValue() && !nearestTile.getMerge()) {
                        tile.setMerge(true);
                    }
                }
            }
        }
    }
    
    private Tile findNearestTileLeft(int row, int col) {
        while (col != 0) {
            col--;
            if (!board[row][col].isEmpty()) {
                return board[row][col];
            }
        }
        return board[row][col];
    }

    private boolean mergeTilesLeft() {
        boolean merged = false;
        for (int col = 1; col < NUM_COLS; col++) {
            for (int row = 0; row < NUM_ROWS; row++) {
                Tile tile = board[row][col];
                if (tile.getMerge()) {
                    merged = true;
                    Tile nearestTile = findNearestTileLeft(row, col);
                    nearestTile.doubleValue();
                    tile.emptyValue();
                    tile.setMerge(false);
                }
            }
        }
        return merged;
    }

    private boolean shiftTilesLeft() {
        boolean shifted = false;
        for (int col = 1; col < NUM_COLS; col++) {
            for (int row = 0; row < NUM_ROWS; row++) {
                if (!board[row][col].isEmpty()) {
                    int newCol = col;
                    while (newCol != 0 && board[row][newCol - 1].isEmpty()) {
                        shifted = true;
                        board[row][newCol - 1].changeValue(board[row][newCol].getValue());
                        board[row][newCol].emptyValue();
                        newCol--;
                    }
                }
            }
        }
        return shifted;
    }

    public void swipeRight() {
        findMergingTilesRight();
        boolean merged = mergeTilesRight();
        boolean shifted = shiftTilesRight();
        if (merged || shifted) {
            addTile();
        }
    }

    private void findMergingTilesRight() {
        for (int col = NUM_ROWS - 2; col >= 0; col--) {
            for (int row = 0; row < NUM_COLS; row++) {
                Tile tile = board[row][col];
                if (tile.getValue() != EMPTY) {
                    Tile nearestTile = findNearestTileRight(row, col);
                    if (tile.getValue() == nearestTile.getValue() && !nearestTile.getMerge()) {
                        tile.setMerge(true);
                    }
                }
            }
        }
    }

    private Tile findNearestTileRight(int row, int col) {
        while (col != NUM_COLS - 1) {
            col++;
            if (board[row][col].getValue() != EMPTY) {
                return board[row][col];
            }
        }
        return board[row][col];
    }

    private boolean mergeTilesRight() {
        boolean merged = false;
        for (int col = NUM_ROWS - 2; col >= 0; col--) {
            for (int row = 0; row < NUM_COLS; row++) {
                Tile tile = board[row][col];
                if (tile.getMerge()) {
                    merged = true;
                    Tile nearestTile = findNearestTileRight(row, col);
                    nearestTile.doubleValue();
                    tile.emptyValue();
                    tile.setMerge(false);
                }
            }
        }
        return merged;
    }

    private boolean shiftTilesRight() {
        boolean shifted = false;
        for (int col = NUM_ROWS - 2; col >= 0; col--) {
            for (int row = 0; row < NUM_ROWS; row++) {
                if (board[row][col].getValue() != EMPTY) {
                    int newCol = col;
                    while (newCol != NUM_COLS - 1 && board[row][newCol + 1].getValue() == EMPTY) {
                        shifted = true;
                        board[row][newCol + 1].changeValue(board[row][newCol].getValue());
                        board[row][newCol].emptyValue();
                        newCol++;
                    }
                }
            }
        }
        return shifted;
    }

    public void addTile() {
        int testRow = (int) (Math.random() * NUM_ROWS);
        int testCol = (int) (Math.random() * NUM_COLS);
        while (board[testRow][testCol].getValue() != EMPTY) {
            testRow = (int) (Math.random() * NUM_ROWS);
            testCol = (int) (Math.random() * NUM_COLS);
        }
        int chance = (int) (Math.random() * 10);
        board[testRow][testCol].changeValue(chance == 1 ? 4 : 2);
    }

    public int[][] getBoardRepresentation() {
        int[][] boardRepresentation = new int[NUM_ROWS][NUM_COLS];
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                boardRepresentation[row][col] = board[row][col].getValue();
            }
        }
        return boardRepresentation;
    }

    public String toString() {
        String boardRepresentation = "";
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Tile tile = board[i][j];
                if (tile.getValue() != EMPTY) {
                    boardRepresentation += String.format("%5s", "" + tile.getValue());
                } else {
                    boardRepresentation += String.format("%5s", "-");
                }
                if (j == NUM_COLS - 1 && i != NUM_ROWS - 1) {
                    boardRepresentation += "\n";
                } else if (!(j == NUM_COLS - 1 && i == NUM_ROWS - 1)) {
                    boardRepresentation += " ";
                }
            }
        }
        return boardRepresentation;
    }
}
