/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luu Manh 13
 */
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class TiledMap extends MIDlet implements CommandListener{

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

    public void commandAction(Command c, Displayable d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
class GameCanvasTiledLayerDemo extends GameCanvas {

    private Image img;
    private TiledLayer backgroundLayer;
    private LayerManager lManager;
    private int cells[] = {
        9, 9, 9, 9, 9,17, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4,
        9, 10,10,10,9, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4,
        9, 10,11,10,9, 9, 1, 1,12, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4,
        9, 10,10,10,9, 9, 1, 1,12, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        9, 9, 9, 9, 9,22, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        15,18,9, 9,19,21, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        6, 8, 9, 9, 6, 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3,13, 3, 3, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 1, 1, 1,12, 1, 1, 1, 1, 1, 1, 1,
        2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1,20,18, 1,19,15,15,
        2, 2, 1, 1, 1, 1, 1, 1,14,14, 1, 1, 1, 1, 1, 1, 1, 1, 1,16, 8, 9, 6, 7, 7,
        2, 2, 2, 2, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1,17, 9,11, 9, 9, 9,
        2, 2, 2, 2, 2, 2, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 9, 9, 9, 9, 9};

    public GameCanvasTiledLayerDemo(boolean suppressKeyEvents) {
        super(suppressKeyEvents);
        lManager = new LayerManager();


        try {
            img = Image.createImage("/Sprites/2.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        backgroundLayer = new TiledLayer(25, 15, img, 24, 24);

        for (int i = 0; i < cells.length; i++) {
            int col = i % 25;
            int row = (i - col) / 25;
            backgroundLayer.setCell(col, row, cells[i]);

        }

        lManager.append(backgroundLayer);

        lManager.paint(getGraphics(), 0, 0);
        lManager.setViewWindow(100 , 100, this.getWidth(), this.getHeight());
        flushGraphics();
    }
    
    public void keyPressed(int keyCode){
//        int action = getGameAction(keyCode);
//        if(action == RIGHT){
//        this.lManager.setViewWindow(this.lManager., UP, UP, action);
//        }
    }
}
