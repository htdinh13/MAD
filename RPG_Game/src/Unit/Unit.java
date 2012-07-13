package Unit;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
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

    public void endTurn(LayerManager lManager, Image image);

    public boolean getEndTurn();

    public void setEndTurn();
}
