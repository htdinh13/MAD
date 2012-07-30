package Attack;

import Unit.Unit;
import View.RPGMap;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public abstract class AttackAbstract implements Attackable, Runnable {

    private Sprite attackSpr;
    public RPGMap map;
    public Unit attacker;
    

    public AttackAbstract(Image img, int width, int height) {
        attackSpr = new Sprite(img, width, height);
    }

    public AttackAbstract(Image img, RPGMap map) {
        attackSpr = new Sprite(img, 30, 30);
        this.map = map;
    }

    public void setAttackSpr(Sprite attackSpr) {
        this.attackSpr = attackSpr;
    }

    public Sprite getAttackSpr() {
        return attackSpr;
    }

    public void attack(Unit attacker, Unit attacked) {
        this.attacker=attacker;
        attacked.beAttacked(attacker);
        attackSpr.setPosition(attacked.getX() - 4, attacked.getY() - 4);
    }

    public void run() {
        synchronized (map.lManager) {
            attackSpr.setVisible(true);
            map.lManager.insert(attackSpr, 0);
            attackSpr.setFrame(1);
            for (int i = 0; i < attackSpr.getFrameSequenceLength() - 2; i++) {
                attackSpr.nextFrame();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            attackSpr.setVisible(false);
            map.lManager.remove(attackSpr);
            attacker.endTurn(map.lManager);
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}
