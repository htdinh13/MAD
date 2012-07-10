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
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class TiledMap extends MIDlet implements CommandListener {

    private Display mDisplay;

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        mDisplay = Display.getDisplay(this);
        GameCanvasTiledLayerDemo cv = new GameCanvasTiledLayerDemo(false);
        mDisplay.setCurrent(cv);
        cv.start();

    }

    public void commandAction(Command c, Displayable d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class GameCanvasTiledLayerDemo extends GameCanvas implements Runnable {

    int x, y;
    private Image img, cursorImg, caocaoImg;
    private Cursor cursorSpr;
    private Sprite caocao;
    private TiledLayer backgroundLayer;
    private LayerManager lManager;
    private int cells[] = {
        9, 9, 9, 9, 9, 17, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4,
        9, 10, 10, 10, 9, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4,
        9, 10, 11, 10, 9, 9, 1, 1, 12, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4,
        9, 10, 10, 10, 9, 9, 1, 1, 12, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        9, 9, 9, 9, 9, 22, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        15, 18, 9, 9, 19, 21, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        6, 8, 9, 9, 6, 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 13, 3, 3, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 1, 1, 1, 12, 1, 1, 1, 1, 1, 1, 1,
        2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 20, 18, 1, 19, 15, 15,
        2, 2, 1, 1, 1, 1, 1, 1, 14, 14, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16, 8, 9, 6, 7, 7,
        2, 2, 2, 2, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 17, 9, 11, 9, 9, 9,
        2, 2, 2, 2, 2, 2, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 9, 9, 9, 9, 9};

    public GameCanvasTiledLayerDemo(boolean suppressKeyEvents) {
        super(suppressKeyEvents);
        x = 0;
        y = 0;
        lManager = new LayerManager();
        try {
            img = Image.createImage("/Sprites/2.png");
            cursorImg = Image.createImage("/Sprites/11.png");
            caocaoImg = Image.createImage("/Sprites/CaoCao.png");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        backgroundLayer = new TiledLayer(25, 15, img, 24, 24);

        for (int i = 0; i < cells.length; i++) {
            int col = i % 25;
            int row = (i - col) / 25;
            backgroundLayer.setCell(col, row, cells[i]);
        }
        cursorSpr = new Cursor(cursorImg, 24, 24, this.getWidth() / 2 - 24, (this.getHeight() - 3) / 2 - 24);
        cursorSpr.setVisible(true);
        
        caocao=new Sprite(caocaoImg,24,24);
        caocao.setPosition(6*24, 6*24);
        caocao.setVisible(true);
        
        cursorSpr.setPosition(cursorSpr.getX_(), cursorSpr.getY_());

       
        lManager.append(cursorSpr);
        lManager.append(caocao);
        lManager.append(backgroundLayer);
        lManager.paint(getGraphics(), 0, 0);

        flushGraphics();
    }

    public void keyPressed() {
        int action = getKeyStates();
        if ((action & RIGHT_PRESSED) != 0 && x < (600 - this.getWidth()) && cursorSpr.getX_() >= 96) {
            x += 24;
        }
        if ((action & LEFT_PRESSED) != 0 && x > 0 && cursorSpr.getX_() <= 456) {
            x -= 24;
        }
        if ((action & UP_PRESSED) != 0 && y > 0 && cursorSpr.getY_() <= 208) {
            y -= 24;
        }
        if ((action & DOWN_PRESSED) != 0 && y < (312 - this.getWidth()) && cursorSpr.getY_() >= 120) {
            y += 24;
        }
        cursorSpr.move(action);
    }

    public void drawLayer() {

        Graphics g = this.getGraphics();
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight());
        lManager.paint(g, 0, 0);
        flushGraphics();
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            caocao.nextFrame();
            cursorSpr.nextFrame();
            keyPressed();
            drawLayer();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }
}
