package Unit;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class AIUnit implements Unit {

    private Sprite sprite, endSprite;
    private int x, y, moveSpace;
    private boolean endTurn;

    public AIUnit(int colnum, int rownum, Image img, int moveSpace) {
        this.x = colnum * 24;
        this.y = rownum * 24;
        this.moveSpace = moveSpace;

        sprite = new Sprite(img, 24, 24);
        sprite.setVisible(true);
        sprite.setPosition(x, y);
        endTurn = false;
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

    public void move(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPreX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPreY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unmove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void endTurn(LayerManager lManager, Image image) {
        endSprite = new Sprite(image);
        endSprite.setVisible(true);
        endSprite.setPosition(x, y);
        lManager.append(endSprite);
    }

    public boolean getEndTurn() {
        return endTurn;
    }

    public void setEndTurn() {
        endTurn = !endTurn;
    }
}
