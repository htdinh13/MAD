/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Attack;

import Attack.AttackAbstract;
import Model.GameHandler;
import Unit.Unit;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class CavalryAttack extends AttackAbstract {

    public CavalryAttack(Image img, int width, int height) {
        super(img, width, height);
    }

    public CavalryAttack(Image img, LayerManager lManager, GameHandler game) {
        super(img,lManager, game);
    }

    
}
