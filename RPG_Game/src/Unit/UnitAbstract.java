package Unit;

import Attack.Attackable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public abstract class UnitAbstract implements Unit {

    private Sprite sprite, endSprite, deadSprite;
    private int x, y, moveSpace;
    private boolean endTurn;
    private Attackable attackType;
    private LayerManager lManager;

    public UnitAbstract(int colnum, int rownum, Image img, int moveSpace, Attackable attackType) {
        this.x = colnum * 24;
        this.y = rownum * 24;
        this.moveSpace = moveSpace;
        sprite = new Sprite(img, 24, 24);
        sprite.setVisible(true);
        sprite.setPosition(x, y);
        endTurn = false;
        this.attackType = attackType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getEndTurn() {
        return endTurn;
    }

    public int getMoveSpace() {
        return moveSpace;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }

    public void endTurn(LayerManager lManager, Image image) {
        endSprite = new Sprite(image);
        endSprite.setVisible(true);
        endSprite.setPosition(x, y);
        lManager.insert(endSprite, 4);
        endTurn = true;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);
    }

    public Attackable getAttackType() {
        return attackType;
    }
}
