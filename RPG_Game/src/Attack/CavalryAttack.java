package Attack;

import Model.GameHandler;
import View.RPGMap;
import javax.microedition.lcdui.Image;

public class CavalryAttack extends AttackAbstract {

    public CavalryAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public CavalryAttack(Image img, RPGMap map) {
        super(img, map);
    }
}
