package Unit;

import Attack.Attackable;
import javax.microedition.lcdui.Image;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class AIUnit extends UnitAbstract {

    public AIUnit(int colnum, int rownum, Image img, int moveSpace, Attackable attackType) {
        super(colnum, rownum, img, moveSpace,attackType);
    }
}
