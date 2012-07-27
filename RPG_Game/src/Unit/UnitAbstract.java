package Unit;

import Algorithm.LinkedList;
import Algorithm.Node;
import Attack.Attackable;
import Attack.CavalryAttack;
import Attack.KnightAttack;
import Attack.RangedAttack;
import View.Cursor;
import View.RPGMap;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

public abstract class UnitAbstract implements Unit {

    private Sprite sprite, endSprite, deadSprite;
    private int x, y;
    private boolean endTurn;
    private Attackable attackType;
    public int health, attack, defence, moveSpace;

    public UnitAbstract(int colnum, int rownum, Image img, int moveSpace, Attackable attackType) {
        this.x = colnum * 24;
        this.y = rownum * 24;
        this.moveSpace = moveSpace;
        sprite = new Sprite(img, 24, 24);
        sprite.setVisible(true);
        sprite.setPosition(x, y);
        endTurn = false;
        this.attackType = attackType;
        if (attackType instanceof KnightAttack) {
            this.health = 60;
            this.attack = 20;
            this.defence = 15;
        } else if (attackType instanceof RangedAttack) {
            this.health = 40;
            this.attack = 25;
            this.defence = 5;
        } else if (attackType instanceof CavalryAttack) {
            this.health = 80;
            this.attack = 30;
            this.defence = 10;
        }
    }

    public UnitAbstract(int colnum, int rownum, Image img, int moveSpace, Attackable attackType, int health, int attack, int defence) {
        this.x = colnum * 24;
        this.y = rownum * 24;
        this.moveSpace = moveSpace;
        sprite = new Sprite(img, 24, 24);
        sprite.setVisible(true);
        sprite.setPosition(x, y);
        endTurn = false;
        this.attackType = attackType;

        this.health = health;
        this.attack = attack;
        this.defence = defence;
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

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
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
        if (endSprite != null) {
            endSprite.setVisible(false);
            lManager.remove(endSprite);
        }
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);
    }

    public boolean move(final RPGMap map, final LinkedList path) {
        if (path != null) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    synchronized (map.lManager) {
                        Node n = path.head;
                        while (n != null) {
                            x = n.getX() * 24;
                            y = n.getY() * 24;
                            map.cursorSpr.move(x, y);
                            map.setActiveView(x, y);
                            sprite.setPosition(x, y);
                            n = n.next;
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        map.movedSpr = new Sprite(map.images[11], 22, 14);
                        map.movedSpr.setFrame(0);
                        if (map.cursorSpr.getY_() == 0) {
                            map.movedSpr.setPosition(map.cursorSpr.getX_() + 1, map.cursorSpr.getY_() + 24);
                        } else {
                            map.movedSpr.setPosition(map.cursorSpr.getX_() + 1, map.cursorSpr.getY_() - 14);
                        }
                        map.lManager.insert(map.movedSpr, 3);
                    }
                }
            });
            t.start();
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
        x = -1;
        y = -1;
        deadSprite.setVisible(false);
    }

    public void beAttacked(Unit attacker) {
        if ((attacker.getAttackType() instanceof KnightAttack && this.getAttackType() instanceof RangedAttack)
                || (attacker.getAttackType() instanceof CavalryAttack && this.getAttackType() instanceof KnightAttack)
                || (attacker.getAttackType() instanceof RangedAttack && this.getAttackType() instanceof CavalryAttack)) {
            this.health -= attacker.getAttack() * 1.2 - this.defence;
        } else {
            this.health -= attacker.getAttack() - this.defence;
        }
    }
}
