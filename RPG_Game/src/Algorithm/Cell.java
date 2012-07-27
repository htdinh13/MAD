package Algorithm;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class Cell {

    private int x, y;
    private boolean canMove;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        canMove = true;
    }

    public Cell(int x, int y, boolean isBlock) {
        this.x = x;
        this.y = y;
        this.canMove = isBlock;
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

    public boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean isBlock) {
        this.canMove = isBlock;
    }           
}
