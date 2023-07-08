import java.util.ArrayList;

public class GameTile {

    public static final int NUM_ROWS_AND_COLS = 4;
    public static final int ROW = 0;
    public static final int COL = 1;

    public static final int ROW_COL_SIZE = 122;
    public static final int BORDER_SIZE = 11;

    private static final int COMMON_STARTING_VALUE = 2;
    private static final int RARE_STARTING_VALUE = 4;
    
    private int x;
    private int y;
    private int targetX;
    private int targetY;
    private int value;
    private boolean finalMerge;
    private boolean initialMerge;

    public GameTile(ArrayList<GameTile> existingTiles) {
        boolean duplicate = false;
        do {
            duplicate = false;
            x = (int) (Math.random() * NUM_ROWS_AND_COLS) * ROW_COL_SIZE + BORDER_SIZE;
            y = (int) (Math.random() * NUM_ROWS_AND_COLS) * ROW_COL_SIZE + BORDER_SIZE;
            for (GameTile existingTile : existingTiles) {
                if (existingTile.getX() == x && existingTile.getY() == y) {
                    duplicate = true;
            }
        }
        } while (duplicate);
        targetX = x;
        targetY = y;
        int chance = (int) (Math.random() * 10);
        value = chance == 0 ? RARE_STARTING_VALUE : COMMON_STARTING_VALUE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void move(int[] direction) {
        if (Math.abs(x - targetX) >= 5) {
            x += direction[COL] * 5;
        } else {
            x += direction[COL];
        }
        if (Math.abs(y - targetY) >= 5) {
            y += direction[ROW] * 5;
        } else {
            y += direction[ROW];
        }
    }

    public int getValue() {
        return value;
    }

    public void doubleValue() {
        value *= 2;
    }

    public boolean initialMerge() {
        return initialMerge;
    }

    public boolean finalMerge() {
        return finalMerge;
    }

    public void changeInitialMerge(boolean initialMerge) {
        this.initialMerge = initialMerge;
    }

    public void changeFinalMerge(boolean finalMerge) {
        this.finalMerge = finalMerge;
    }

}