package Test;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class test1 {

    public static Integer[] attackCells;

    public static void main(String[] args) {
        attackCells = new Integer[10];
        attackCells[0] = new Integer(0);
        attackCells[1] = new Integer(1);
        attackCells[2] = new Integer(2);
        attackCells[3] = new Integer(3);

        System.out.println(nextAttackCell(8).intValue());
        System.out.println(prevAttackCell(8).intValue());
    }

    public static Integer nextAttackCell(int index) {
        if (attackCells[0] == null) {
            return null;
        }
        if (index == attackCells.length - 1) {
            return attackCells[0];
        } else {
            index++;
        }
        return (attackCells[index++] != null) ? attackCells[index] : nextAttackCell(index);
    }

    public static Integer prevAttackCell(int index) {
        if (attackCells[0] == null) {
            return null;
        }
        if (index == 0) {
            index = attackCells.length - 1;
        } else {
            index--;
        }
        return (attackCells[index] != null) ? attackCells[index] : prevAttackCell(index);
    }
}
