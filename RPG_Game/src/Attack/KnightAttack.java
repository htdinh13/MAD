package Attack;

import Unit.Unit;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class KnightAttack extends AttackAbstract {

    public KnightAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public KnightAttack(Image img,LayerManager lManager) {
        super(img,lManager);
    }    
}
