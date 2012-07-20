package Unit;

import Attack.Attackable;
import javax.microedition.lcdui.Image;


/**
 *
 * @author HOANG TRUONG DINH
 */
public class PlayerUnit extends UnitAbstract {

    private int preX, preY;

    public PlayerUnit(int colnum, int rownum, Image img, int moveSpace, Attackable attackType) {
        super(colnum, rownum, img, moveSpace, attackType);
        this.preX = super.getX();
        this.preY = super.getY();
    }

    public void move(int x, int y) {
        this.preX = super.getX();
        this.preY = super.getY();
        super.setX(x);
        super.setY(y);
        (super.getSprite()).setPosition(x, y);
    }

    public void unmove() {
        super.setX(preX);
        super.setY(preY);
        (super.getSprite()).setPosition(super.getX(), super.getY());
    }
}
