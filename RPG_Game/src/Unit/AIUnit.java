package Unit;

import Attack.Attackable;
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class AIUnit extends UnitAbstract {

    public AIUnit(int colnum, int rownum, Image img, int moveSpace, Attackable attackType) {
        super(colnum, rownum, img, moveSpace,attackType);
    }
}
