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
    private int currX, currY, destX, destY;
    // private Unit attacked, attacker;

    public RangedAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public RangedAttack(Image img, LayerManager lManager) {
        super(img, lManager);
    }

    public void attack(Unit attacker, Unit attacked) {
//        this.attacked = attacked;
//        this.attacker = attacker;
        if (attacker.getX() == attacked.getX()) {
            direction = (attacker.getY() > attacked.getY()) ? 0 : 1;
            startFrame = 7;
        } else if (attacker.getY() == attacked.getY()) {
            direction = (attacker.getX() > attacked.getX()) ? 2 : 3;
            startFrame = 0;
        }
        super.getAttackSpr().setFrame(startFrame);
        super.getAttackSpr().setVisible(true);
        switch (direction) {
            case 0:
                currX = attacker.getX() - 4;
                currY = attacker.getY() - 12 - 4;
                destX = attacked.getX() - 4;
                destY = attacked.getY() + 4;
                break;
            case 1:
                super.getAttackSpr().setTransform(Sprite.TRANS_ROT180);
                currX = attacker.getX() - 4;
                currY = attacker.getY() + 12 - 4;
                destX = attacked.getX() - 4;
                destY = attacked.getY() - 4;
                break;
            case 2:
                super.getAttackSpr().setTransform(Sprite.TRANS_ROT180);
                currX = attacker.getX() - 12 - 4;
                currY = attacker.getY() - 4;
                destX = attacked.getX() + 4;
                destY = attacked.getY() - 4;
                break;
            case 3:
                currX = attacker.getX() + 12 - 4;
                currY = attacker.getY() - 4;
                destX = attacked.getX() - 4;
                destY = attacked.getY() - 4;
                break;
            default:
                break;

        }
        super.getAttackSpr().setPosition(currX, currY);
        super.getLManager().insert(super.getAttackSpr(), 0);
    }

    public void run() {
        synchronized (super.getLManager()) {
            super.getAttackSpr().setFrame(startFrame);
            while (!((currX == destX) && (currY == destY))) {
                switch (direction) {
                    case 0:
                        currY--;
                        super.getAttackSpr().setPosition(currX, currY);
                        break;
                    case 1:
                        currY++;
                        super.getAttackSpr().setPosition(currX, currY);
                        break;
                    case 2:
                        currX--;
                        super.getAttackSpr().setPosition(currX, currY);
                        break;
                    case 3:
                        currX++;
                        super.getAttackSpr().setPosition(currX, currY);
                        break;
                    default:
                        break;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
            for (int i = 0; i < super.getAttackSpr().getFrameSequenceLength() / 2 - 2; i++) {
                super.getAttackSpr().nextFrame();
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            super.getLManager().remove(super.getAttackSpr());
        }
    }
}
