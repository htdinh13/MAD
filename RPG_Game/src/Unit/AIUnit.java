package Unit;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class AIUnit implements Unit {

    Sprite sprite;
    int x, y, moveSpace;

    public AIUnit(int colnum, int rownum, Image img,int moveSpace) {
        this.x = colnum * 24;
        this.y = rownum * 24;
        this.moveSpace = moveSpace;

        sprite = new Sprite(img, 24, 24);
        sprite.setVisible(true);
        sprite.setPosition(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMoveSpace() {
        return moveSpace;
    }
}
