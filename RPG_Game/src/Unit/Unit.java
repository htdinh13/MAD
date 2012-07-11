package Unit;

import javax.microedition.lcdui.game.Sprite;


/**
 *
 * @author HOANG TRUONG DINH
 */
public interface Unit {
    public Sprite getSprite();
    public int getX();
    public int getY();
    public int getMoveSpace();
}
