package Attack;

import Unit.Unit;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public abstract class AttackAbstract implements Attackable, Runnable {

    private Sprite attackSpr;

    public AttackAbstract(Image img, int width, int height) {
        attackSpr = new Sprite(img, width, height);
    }

    public Sprite getAttackSpr() {
        return attackSpr;
    }

    public void attack(Unit attacker, Unit attacked) {
        attackSpr.setVisible(true);
        attackSpr.setPosition(attacked.getX() - 4, attacked.getY() - 4);
    }

    public void run() {
        attackSpr.setFrame(0);
        for (int i = 0; i < attackSpr.getFrame(); i++) {
            attackSpr.nextFrame();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}
