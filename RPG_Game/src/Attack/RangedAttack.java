/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Attack;

import Attack.AttackAbstract;
import Unit.Unit;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class RangedAttack extends AttackAbstract {
    // 0-North 1-South 2-West 3-East 4-NorthWest 5-NorthEast 6-SouthWest 7-SouthEast

    private int direction, startFrame;

    public RangedAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public RangedAttack(Image img, LayerManager lManager) {
        super(img, lManager);
    }

    public void attack(Unit attacker, Unit attacked) {
        if (attacker.getX() == attacked.getX()) {
            direction = (attacker.getY() > attacked.getY()) ? 0 : 1;
            startFrame = 8;
        } else if (attacker.getY() == attacked.getY()) {
            direction = (attacker.getX() > attacked.getX()) ? 2 : 3;
            startFrame = 1;
        }
        switch (direction) {
            case 0:
                break;
            case 1:
                super.getAttackSpr().setTransform(Sprite.TRANS_ROT180);
                break;
            case 2:
                super.getAttackSpr().setTransform(Sprite.TRANS_ROT180);
                break;
            case 3:
                break;
            default:
                break;
        }
        super.getAttackSpr().setFrame(startFrame);
        super.getAttackSpr().setVisible(true);
        super.getAttackSpr().setPosition(attacked.getX() - 4, attacked.getY() - 4);
        super.getLManager().insert(super.getAttackSpr(), 0);
    }

    public void run() {
        super.getAttackSpr().setFrame(startFrame);
        for (int i = 0; i < super.getAttackSpr().getFrameSequenceLength() / 2 - 2; i++) {
            super.getAttackSpr().nextFrame();
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        super.getLManager().remove(super.getAttackSpr());
    }
}
