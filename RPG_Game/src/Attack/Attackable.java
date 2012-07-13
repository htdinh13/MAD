/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Attack;

import Unit.Unit;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author HOANG TRUONG DINH
 */
public interface Attackable {

    public void attack(Unit attacker, Unit attacked);

    public void start();

    public Sprite getAttackSpr();
}
