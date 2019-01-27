package game;

/**
 * The GridNumber class is used to represent a location.
 */
class GridNumber {
    int row;
    int column;

    public GridNumber(int r, int c) {
        row = r;
        column = c;
    }

    public boolean isValid() {
        return row >=0 && column >=0;
    }
}