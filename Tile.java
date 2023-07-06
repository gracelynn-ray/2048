public class Tile {
    public static final int EMPTY = 1;

    private int value;
    private boolean needsToMerge;

    public Tile() {
        value = EMPTY;
        needsToMerge = false;
    }

    public void changeValue(int newValue) {
        value = newValue;
    }

    public void doubleValue() {
        changeValue(value * 2);
    }

    public void emptyValue() {
        changeValue(EMPTY);
    }

    public void setMerge(boolean needsToMerge) {
        this.needsToMerge = needsToMerge;
    }

    public int getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == EMPTY;
    }

    public boolean getMerge() {
        return needsToMerge;
    }
}
