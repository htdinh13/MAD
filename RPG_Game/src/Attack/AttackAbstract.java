package Attack;

import Model.GameHandler;
import Unit.Unit;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public abstract class AttackAbstract implements Attackable, Runnable {

    private Sprite attackSpr;
    private LayerManager lManager;
    public GameHandler game;

    public AttackAbstract(Image img, int width, int height) {
        attackSpr = new Sprite(img, width, height);
    }

    public AttackAbstract(Image img, LayerManager lManager, GameHandler game) {
        attackSpr = new Sprite(img, 30, 30);
        this.lManager = lManager;
        this.game = game;
    }

    public void setAttackSpr(Sprite attackSpr) {
        this.attackSpr = attackSpr;
    }

    public Sprite getAttackSpr() {
        return attackSpr;
    }

    public void attack(Unit attacker, Unit attacked) {
        attacked.beAttacked(attacker);
        attackSpr.setPosition(attacked.getX() - 4, attacked.getY() - 4);
    }

    public void run() {
        synchronized (lManager) {
            synchronized (game) {
                attackSpr.setVisible(true);
                lManager.insert(attackSpr, 0);
                attackSpr.setFrame(1);
                //while(true){
                for (int i = 0; i < attackSpr.getFrameSequenceLength() - 2; i++) {
                    attackSpr.nextFrame();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                lManager.remove(attackSpr);
            }
        }
    }

    public LayerManager getLManager() {
        return lManager;
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}
