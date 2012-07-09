
import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author George Nguyen
 */
public class SpriteDemo extends MIDlet {

    private Display mDisplay;

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        mDisplay = Display.getDisplay(this);
        GameCanvasSpriteDemo cv = new GameCanvasSpriteDemo(false);
        new Thread(cv).start();
        mDisplay.setCurrent(cv);

    }
}

class GameCanvasSpriteDemo extends GameCanvas implements Runnable {

    private Image img;
    private Sprite mSprite;
    private LayerManager lManager;

    public GameCanvasSpriteDemo(boolean suppressKeyEvents) {
        super(suppressKeyEvents);
        lManager = new LayerManager();

        try {
            img = Image.createImage("/spriteImg.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        mSprite = new Sprite(img, 50, 67);
        lManager.append(mSprite);

    }

    public void run() {
        while (true) {
            
            lManager.paint(getGraphics(), 0, getHeight() - 100);
            flushGraphics();
            mSprite.nextFrame();

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void animateSprite() {
        if (mSprite.getFrame() < 5) {
        }
    }
}
