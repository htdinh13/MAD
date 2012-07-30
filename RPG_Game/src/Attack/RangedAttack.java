/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Attack;

import Model.GameHandler;
import Unit.Unit;
import View.RPGMap;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class RangedAttack extends AttackAbstract {

    private int direction, startFrame;
    private int currX, currY, destX, destY;

    public RangedAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public RangedAttack(Image img, RPGMap map) {
        super(img, map);
    }

    public void attack(Unit attacker, Unit attacked) {
        this.attacker = attacker;
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
        attacked.beAttacked(attacker);
        this.getAttackSpr().setPosition(currX, currY);
        this.map.lManager.insert(super.getAttackSpr(), 0);
    }

    public void run() {
        synchronized (this.map.lManager) {
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
            super.getAttackSpr().setVisible(false);
            this.map.lManager.remove(super.getAttackSpr());
            this.attacker.endTurn(map.lManager);
        }
    }
}
