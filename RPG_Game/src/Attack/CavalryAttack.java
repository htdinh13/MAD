/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Attack;

import Model.GameHandler;
import View.RPGMap;
import javax.microedition.lcdui.Image;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class CavalryAttack extends AttackAbstract {

    public CavalryAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public CavalryAttack(Image img, RPGMap map) {
        super(img, map);
    }
}
