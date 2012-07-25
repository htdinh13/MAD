package Unit;

import Algorithm.LinkedList;
import Algorithm.Node;
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
public abstract class UnitAbstract implements Unit {

    private Sprite sprite, endSprite, deadSprite;
    private int x, y, moveSpace;
    private boolean endTurn;
    private Attackable attackType;
    // private LayerManager lManager;

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

    public void newTurn(LayerManager lManager) {
        endTurn = false;
        lManager.remove(endSprite);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean move(final RPGMap map, final Cursor cursor, final LayerManager lManager, final LinkedList path,final Image image) {
        if (path != null) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    synchronized (lManager) {
                        Node n = path.head;
                        while (n != null) {
                            x = n.getX() * 24;
                            y = n.getY() * 24;
                            cursor.move(x, y);
                            map.setActiveView(x, y);
                            sprite.setPosition(x, y);
                            n = n.next;
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        
                        map.movedSpr = new Sprite(image, 22, 14);
                        map.movedSpr.setFrame(1);
                        if (cursor.getY_() == 0) {
                            map.movedSpr.setPosition(cursor.getX_() + 1, cursor.getY_() + 24);
                        } else {
                            map.movedSpr.setPosition(cursor.getX_() + 1, cursor.getY_() - 14);
                        }
                        lManager.insert(map.movedSpr, 3);
                    }
                }
            });
            t.start();
            System.out.println(x + "," + y);
            return true;
        }

        return false;
    }

    public Attackable getAttackType() {
        return attackType;
    }

    public void isDead(final LayerManager lManager, Image image) {
        deadSprite = new Sprite(image, 15, 20);
        deadSprite.setVisible(true);
        if (y == 0) {
            deadSprite.setPosition(x + 4, y + 24);
        } else {
            deadSprite.setPosition(x + 4, y - 20);
        }
        //lManager.insert(deadSprite, 4);
        Thread t = new Thread(new Runnable() {

            public void run() {
                synchronized (lManager) {
                    lManager.insert(deadSprite, 4);
                    for (int i = 0; i < 8; i++) {
                        deadSprite.nextFrame();
                        sprite.setVisible(i % 2 == 0);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    lManager.remove(deadSprite);
                    lManager.remove(sprite);
                }
            }
        });
        t.start();
    }
}
