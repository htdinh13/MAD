
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luu Manh 13
 */
public class General extends Sprite{

    public General(Sprite s) {
        super(s);
    }

    public General(Image image, int frameWidth, int frameHeight) {
        super(image, frameWidth, frameHeight);
    }

    public General(Image image) {
        super(image);
    }
    
}
