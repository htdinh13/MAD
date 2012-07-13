
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Luu Manh 13
 */
public class AttackSprite extends Sprite implements Runnable {

    public AttackSprite(Sprite s) {
        super(s);
    }

    public AttackSprite(Image image, int frameWidth, int frameHeight) {
        super(image, frameWidth, frameHeight);
    }

    public AttackSprite(Image image) {
        super(image);
    }

    public void run() {
        while (true) {
//            for (int i = 0; i < this.getRawFrameCount(); i++) {
                this.nextFrame();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            //}
        }
    }
}
