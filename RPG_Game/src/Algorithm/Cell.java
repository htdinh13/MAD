package Algorithm;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class Cell {

    private int x, y;
    private int col, row;
    Cell[] neighbors;
    Cell pathParent;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
