package Attack;

import Model.GameHandler;
import View.RPGMap;
import javax.microedition.lcdui.Image;

public class KnightAttack extends AttackAbstract {

    public KnightAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public KnightAttack(Image img, RPGMap map) {
        super(img, map);
    }
}
