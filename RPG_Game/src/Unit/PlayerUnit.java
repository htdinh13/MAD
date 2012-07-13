package Unit;

import Attack.Attackable;
import Unit.Unit;
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.Image2D;

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
