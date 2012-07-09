
import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
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
public class TiledLayerDemo extends MIDlet {

    private Display mDisplay;

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        mDisplay = Display.getDisplay(this);
        GameCanvasTiledLayerDemo cv = new GameCanvasTiledLayerDemo(false);
        mDisplay.setCurrent(cv);

    }
}

class GameCanvasTiledLayerDemo extends GameCanvas {

    private Image img;
    private TiledLayer backgroundLayer;
    private LayerManager lManager;
    private int cells[] = {
        1, 0, 2,
        2, 1, 0,
        0, 2, 2,
        0, 0, 1,
        2, 2, 2,
        1, 1, 1
    };

    public GameCanvasTiledLayerDemo(boolean suppressKeyEvents) {
        super(suppressKeyEvents);
        lManager = new LayerManager();


        try {
            img = Image.createImage("/background.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        backgroundLayer = new TiledLayer(3, 6, img, 32, 32);

        for (int i = 0; i < cells.length; i++) {
            int col = i % 3;
            int row = (i - col) / 3;
            backgroundLayer.setCell(col, row, cells[i]);

        }

        lManager.append(backgroundLayer);

        lManager.paint(getGraphics(), 0, 0);
        flushGraphics();
    }
}
