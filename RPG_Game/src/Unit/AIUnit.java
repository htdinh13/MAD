package Unit;

import Attack.Attackable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class AIUnit extends UnitAbstract {

    public AIUnit(int colnum, int rownum, Image img, int moveSpace, Attackable attackType) {
        super(colnum, rownum, img, moveSpace, attackType);
    }

    public AIUnit(int x, int y, Image img, int moveSpace, Attackable attackType, int health, int attack, int defence) {
        super(x, y, img, moveSpace, attackType, health, attack, defence);
    }

    public void live(LayerManager lManager, Image img) {
        super.endTurn(lManager, img);
    }
}
