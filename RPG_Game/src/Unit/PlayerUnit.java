package Unit;

import Unit.Unit;
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.Image2D;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class PlayerUnit implements Unit {

    private Sprite sprite, endSprite;
    private int x, y, moveSpace;
    private int preX, preY;
    private boolean endTurn;

    public PlayerUnit(int colnum, int rownum, Image img, int moveSpace) {
        this.x = colnum * 24;
        this.y = rownum * 24;
        this.preX = x;
        this.preY = y;
        this.moveSpace = moveSpace;
        sprite = new Sprite(img, 24, 24);
        sprite.setVisible(true);
        sprite.setPosition(x, y);
        endTurn = false;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getMoveSpace() {
        return moveSpace;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPreX() {
        return preX;
    }

    public int getPreY() {
        return preY;
    }

    public void move(int x, int y) {
        this.preX = this.x;
        this.preY = this.y;
        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);
    }

    public void unmove() {
        this.x = preX;
        this.y = preY;
        sprite.setPosition(x, y);
    }

    public void endTurn(LayerManager lManager, Image image) {
        endSprite = new Sprite(image);
        endSprite.setVisible(true);
        endSprite.setPosition(x, y);
        lManager.insert(endSprite, 0);
        endTurn = true;
    }

    public void setEndTurn() {
        endTurn = !endTurn;
    }

    public boolean getEndTurn() {
        return endTurn;
    }
}
