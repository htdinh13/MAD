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

    public int getPreX();

    public int getPreY();

    public int getMoveSpace();

    public void move(int x, int y);
    
    public void unmove();
}
