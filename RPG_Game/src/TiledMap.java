/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luu Manh 13
 */
import Unit.*;
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
    private Image[] images;
    private Cursor cursorSpr;
    private Unit[] ai_units, pl_units;
    private TiledLayer backgroundLayer;
    private LayerManager lManager;
    private int cells[][] = {
        {4, 4, 4, 4, 4, 17, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3},
        {4, 5, 5, 5, 4, 11, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3},
        {4, 5, 6, 5, 4, 4, 1, 1, 7, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 3, 3, 3},
        {4, 5, 5, 5, 4, 4, 1, 1, 7, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 4, 22, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {15, 18, 4, 4, 19, 21, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {12, 14, 4, 4, 12, 14, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 10, 10, 10},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 10, 8, 10, 10, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 10, 1, 7, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 10, 10, 1, 1, 1, 7, 1, 20, 18, 1, 19, 15, 15},
        {2, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 1, 1, 1, 1, 1, 1, 1, 1, 16, 11, 4, 12, 13, 13},
        {2, 2, 1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 16, 4, 4, 4, 4, 4},
        {2, 2, 2, 2, 1, 1, 1, 1, 10, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 17, 4, 6, 4, 4, 4},
        {2, 2, 2, 2, 2, 2, 1, 1, 10, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 4, 4, 4, 4, 4}
    };

    public void loadImages() throws IOException {
        images = new Image[21];
        for (int i = 0; i < 21; i++) {
            images[i] = Image.createImage("/Sprites/" + i + ".png");
        }
    }

    public GameCanvasTiledLayerDemo(boolean suppressKeyEvents) {
        super(suppressKeyEvents);
        x = 0;
        y = 0;
        ai_units = new Unit[23];
        pl_units = new Unit[5];
        lManager = new LayerManager();
        try {
            loadImages();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        backgroundLayer = new TiledLayer(25, 15, images[2], 24, 24);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 25; j++) {
                backgroundLayer.setCell(j, i, cells[i][j]);
            }
        }
        cursorSpr = new Cursor(images[7], 24, 24, this.getWidth() / 2 - 24, (this.getHeight() - 3) / 2 - 24);
        cursorSpr.setVisible(true);
        cursorSpr.setPosition(cursorSpr.getX_(), cursorSpr.getY_());
        createAIUnits();
        createPLUnits();
        lManager.append(cursorSpr);
        lManager.append(backgroundLayer);
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(getGraphics(), 0, 0);
        flushGraphics();
    }

    public void createAIUnits() {
        ai_units[0] = new AIUnit(10, 2, images[14]);
        ai_units[1] = new AIUnit(10, 3, images[14]);
        ai_units[2] = new AIUnit(11, 2, images[14]);
        ai_units[3] = new AIUnit(11, 3, images[14]);
        ai_units[4] = new AIUnit(11, 0, images[14]);
        ai_units[5] = new AIUnit(11, 5, images[14]);
        ai_units[6] = new AIUnit(12, 0, images[12]);
        ai_units[7] = new AIUnit(12, 5, images[12]);
        ai_units[8] = new AIUnit(13, 2, images[12]);
        ai_units[9] = new AIUnit(13, 3, images[12]);
        ai_units[10] = new AIUnit(15, 2, images[13]);
        ai_units[11] = new AIUnit(15, 3, images[13]);
        ai_units[12] = new AIUnit(16, 2, images[13]);
        ai_units[13] = new AIUnit(16, 3, images[13]);
        ai_units[14] = new AIUnit(20, 9, images[14]);
        ai_units[15] = new AIUnit(21, 9, images[14]);
        ai_units[16] = new AIUnit(22, 9, images[14]);
        ai_units[17] = new AIUnit(20, 14, images[12]);
        ai_units[18] = new AIUnit(21, 14, images[12]);
        ai_units[19] = new AIUnit(22, 14, images[12]);
        ai_units[20] = new AIUnit(21, 13, images[15]);
        ai_units[21] = new AIUnit(20, 12, images[13]);
        ai_units[22] = new AIUnit(22, 12, images[13]);
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i] != null) {
                lManager.append(ai_units[i].getSprite());
            }
        }
    }

    public void createPLUnits() {
        pl_units[0] = new PlayerUnit(2, 2, images[16], 5);
        pl_units[1] = new PlayerUnit(3, 2, images[18], 4);
        pl_units[2] = new PlayerUnit(3, 3, images[20], 3);
        pl_units[3] = new PlayerUnit(2, 8, images[17], 5);
        pl_units[4] = new PlayerUnit(3, 8, images[19], 5);
        for (int i = 0; i < pl_units.length; i++) {
            if (pl_units[i] != null) {
                lManager.append(pl_units[i].getSprite());
            }
        }
    }

    public void animationUnits() {
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i] != null) {
                ai_units[i].getSprite().nextFrame();
            }
        }
        for (int i = 0; i < pl_units.length; i++) {
            if (pl_units[i] != null) {
                pl_units[i].getSprite().nextFrame();
            }
        }
    }

    protected void keyPressed(int keyCode) {
        int gameAction = getGameAction(keyCode);
        switch (gameAction) {
            case RIGHT:
                if (x < (600 - this.getWidth()) && cursorSpr.getX_() >= 96) {
                    x += 24;
                }
                break;
            case LEFT:
                if (x > 0 && cursorSpr.getX_() <= 456) {
                    x -= 24;
                }
                break;
            case UP:
                if (y > 0 && cursorSpr.getY_() <= 208) {
                    y -= 24;
                }
                break;
            case DOWN:
                if (y < (312 - this.getWidth()) && cursorSpr.getY_() >= 120) {
                    y += 24;
                }
                break;
            case 0:
                int col = cursorSpr.getX_() / 24;
                int row = cursorSpr.getY_() / 24;
                Unit selectedUnit = getUnit(cursorSpr.getX_(), cursorSpr.getY_());
                if (selectedUnit != null) {
                    int space = selectedUnit.getMoveSpace();
                    for (int i = 0; i < space * 2 + 1; i++) {
                        for (int j = 0; j < space * 2 + 1; j++) {

                        }
                    }
                }
                break;
            default:
                break;
        }
        cursorSpr.move(gameAction);
    }

    protected void keyReleased(int keyCode) {
        int gameAction = getGameAction(keyCode);
        switch (gameAction) {
            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case FIRE:
                break;
            case 0:

                break;
            default:
                System.out.println(gameAction + " action");
                break;
        }
    }

//    public void keyPressed() {
//        int action = getKeyStates();
//        if ((action & RIGHT_PRESSED) != 0 && x < (600 - this.getWidth()) && cursorSpr.getX_() >= 96) {
//            x += 24;
//        }
//        if ((action & LEFT_PRESSED) != 0 && x > 0 && cursorSpr.getX_() <= 456) {
//            x -= 24;
//        }
//        if ((action & UP_PRESSED) != 0 && y > 0 && cursorSpr.getY_() <= 208) {
//            y -= 24;
//        }
//        if ((action & DOWN_PRESSED) != 0 && y < (312 - this.getWidth()) && cursorSpr.getY_() >= 120) {
//            y += 24;
//        }
//        if ((action & KEY_NUM0) != 0) {
//            int col = cursorSpr.getX_() / 24;
//            int row = cursorSpr.getY_() / 24;
//            Unit selectedUnit = getUnit(cursorSpr.getX_(), cursorSpr.getY_());
//            if (selectedUnit != null) {
//                int space = selectedUnit.getMoveSpace();
//                System.out.println("Unit");
//            }
//        }
//        cursorSpr.move(action);
//    }
    public void drawLayer() {

        Graphics g = this.getGraphics();
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(g, 0, 0);
        flushGraphics();
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            cursorSpr.nextFrame();
            animationUnits();
            //keyPressed();
            drawLayer();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }
    }

    public Unit getUnit(int x, int y) {
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i].getX() == x && ai_units[i].getY() == y) {
                return ai_units[i];
            }
        }
        for (int i = 0; i < pl_units.length; i++) {
            if (pl_units[i].getX() == x && pl_units[i].getY() == y) {
                return pl_units[i];
            }
        }
        return null;
    }
}
