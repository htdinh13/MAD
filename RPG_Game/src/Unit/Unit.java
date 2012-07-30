package Unit;

import Algorithm.LinkedList;
import Attack.Attackable;
import View.Cursor;
import View.RPGMap;
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

    public int getMoveSpace();

    public boolean move(RPGMap map, LinkedList path);

    public void move(int x, int y);

    public void endTurn(LayerManager lManager);

    public void newTurn(LayerManager lManager);

    public void beAttacked(Unit attacker);

    public void isDead(LayerManager lManager, Image image);

    public boolean getEndTurn();

    public void setEndTurn(boolean endTurn);

    public Attackable getAttackType();

    public int getAttack();

    public int getHealth();

    public int getDefence();

    public void setHealth(int health);

    public int getMaxHealth();

    public void loadUnit(int x, int y, int health, boolean endTurn, RPGMap map);
}
