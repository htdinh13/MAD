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
    private Sprite selectedSpr, movedSpr;
    private Unit[] ai_units, pl_units;
    private TiledLayer backgroundLayer, movingLayer;
    private LayerManager lManager;
    private final int cells[][] = {
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
    private boolean onSelected, isMoved, isMoving, isAI;
    private Unit selectedUnit;

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
        onSelected = false;
        isMoved = false;
        isAI = false;
        isMoving = false;
        ai_units = new Unit[23];
        pl_units = new Unit[5];
        lManager = new LayerManager();
        try {
            loadImages();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //create attack sprite

        AttackSprite a1 = new AttackSprite(images[6], 30, 30);
        a1.setVisible(true);
        a1.setPosition(9 * 24+15, 2 * 24);

        Thread t = new Thread(a1);
        t.start();


        // End attack sprite
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

        lManager.insert(cursorSpr, 1);
        lManager.insert(a1, 0);
        lManager.insert(backgroundLayer, lManager.getSize());
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(getGraphics(), 0, 0);
        flushGraphics();
    }

    public void createAIUnits() {
        ai_units[0] = new AIUnit(10, 2, images[14], 5);
        ai_units[1] = new AIUnit(10, 3, images[14], 5);
        ai_units[2] = new AIUnit(11, 2, images[14], 5);
        ai_units[3] = new AIUnit(11, 3, images[14], 5);
        ai_units[4] = new AIUnit(11, 0, images[14], 5);
        ai_units[5] = new AIUnit(11, 5, images[14], 5);
        ai_units[6] = new AIUnit(12, 0, images[12], 3);
        ai_units[7] = new AIUnit(12, 5, images[12], 3);
        ai_units[8] = new AIUnit(13, 2, images[12], 3);
        ai_units[9] = new AIUnit(13, 3, images[12], 3);
        ai_units[10] = new AIUnit(15, 2, images[13], 4);
        ai_units[11] = new AIUnit(15, 3, images[13], 4);
        ai_units[12] = new AIUnit(16, 2, images[13], 4);
        ai_units[13] = new AIUnit(16, 3, images[13], 4);
        ai_units[14] = new AIUnit(20, 9, images[14], 5);
        ai_units[15] = new AIUnit(21, 9, images[14], 5);
        ai_units[16] = new AIUnit(22, 9, images[14], 5);
        ai_units[17] = new AIUnit(20, 14, images[12], 3);
        ai_units[18] = new AIUnit(21, 14, images[12], 3);
        ai_units[19] = new AIUnit(22, 14, images[12], 3);
        ai_units[20] = new AIUnit(21, 13, images[15], 6);
        ai_units[21] = new AIUnit(20, 12, images[13], 4);
        ai_units[22] = new AIUnit(22, 12, images[13], 4);
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
        pl_units[4] = new PlayerUnit(8, 3, images[19], 5);
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

    public void createMovingLayer(Unit selectedUnit, int col, int row) {
        int space = selectedUnit.getMoveSpace();
        movingLayer = new TiledLayer(space * 2 + 1, space * 2 + 1, images[10], 24, 24);
        for (int i = 0; i < space * 2 + 1; i++) {
            for (int j = 0; j < space * 2 + 1; j++) {
                if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                    int a = j + col - space, b = i + row - space;
                    if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                        if (backgroundLayer.getCell(a, b) < 10) {
//                            if (getPLUnit(a * 24, b * 24) == null) {
//                                if (getAIUnit(a * 24, b * 24) == null) {
                            movingLayer.setCell(j, i, 2);
//                                }
//                            }
                        }
                    }
                }
            }
        }
        movingLayer.setPosition((col - space) * 24, (row - space) * 24);
        lManager.insert(movingLayer, 28);
    }

    public void createAttackLayer(Unit selectedUnit, int col, int row) {
    }

    protected void keyPressed(int keyCode) {
        //int gameAction = getGameAction(keyCode);
        switch (keyCode) {
            // Use '0' for show the movement range
            case KEY_NUM0:
                if (!onSelected) {
                    int col = cursorSpr.getX_() / 24;
                    int row = cursorSpr.getY_() / 24;

                    selectedUnit = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
                    if (selectedUnit != null) {
                        isAI = true;
                    } else {
                        selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                    }
                    if (selectedUnit != null && !selectedUnit.getEndTurn()) {
                        if (isAI) {
                            createMovingLayer(selectedUnit, col, row);
                        } else {
                            createMovingLayer(selectedUnit, col, row);
                        }
                    }
                }
                break;
            // Use '#' for cancel or undo command    
            case KEY_POUND:
                if (onSelected) {
                    if (isMoving) {
                        if (isMoved) {
                            isMoved = false;
                            selectedUnit.unmove();
                            lManager.remove(movedSpr);
                            selectedSpr.setVisible(true);
                            movingLayer.setVisible(true);
                            cursorSpr.setXY(selectedUnit.getX(), selectedUnit.getY());
                            cursorSpr.setPosition(selectedUnit.getX(), selectedUnit.getY());
                        } else {
                            isMoving = false;
                            cursorSpr.setXY(selectedUnit.getX(), selectedUnit.getY());
                            cursorSpr.setPosition(selectedUnit.getX(), selectedUnit.getY());
                            lManager.remove(movingLayer);
                        }
                    } else {
                        onSelected = false;
                        lManager.remove(selectedSpr);
                    }
                }
                break;
            case KEY_STAR:
                break;

            default:
                break;
        }
    }

    protected void keyReleased(int keyCode) {
        //int gameAction = getGameAction(keyCode);
        switch (keyCode) {
            case KEY_NUM0:
                if (!onSelected) {
                    if (selectedUnit != null && movingLayer != null) {
                        lManager.remove(movingLayer);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void keyPressed() {
        int action = getKeyStates();
        if ((action & RIGHT_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving) {
                    if (isMoved) {
                        movedSpr.nextFrame();
                    } else {
                        cursorSpr.move(action);
                    }
                } else {
                    selectedSpr.nextFrame();
                }
            } else if (x < (600 - this.getWidth()) && cursorSpr.getX_() >= 96) {
                x += 24;
            }
        } else if ((action & LEFT_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving) {
                    if (isMoved) {
                        movedSpr.nextFrame();
                    } else {
                        cursorSpr.move(action);
                    }
                } else {
                    selectedSpr.nextFrame();
                }
            } else if (x > 0 && cursorSpr.getX_() <= 456) {
                x -= 24;
            }
        } else if ((action & UP_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving && !isMoved) {
                    cursorSpr.move(action);
                }
            } else if (y > 0 && cursorSpr.getY_() <= 208) {
                y -= 24;
            }
        } else if ((action & DOWN_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving && !isMoved) {
                    cursorSpr.move(action);
                }
            } else if (y < (312 - this.getWidth()) && cursorSpr.getY_() >= 120) {
                y += 24;
            }
        } else if ((action & FIRE_PRESSED) != 0) {
            if (!onSelected) {
                selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                if (selectedUnit != null && !selectedUnit.getEndTurn()) {
                    onSelected = true;
                    selectedSpr = new Sprite(images[0], 30, 14);
                    selectedSpr.setPosition(cursorSpr.getX_() - 3, cursorSpr.getY_() - 14);
                    lManager.insert(selectedSpr, 2);
                }
            } else {
                if (!isMoving) {
                    switch (selectedSpr.getFrame()) {
                        case 0:
                            int col = cursorSpr.getX_() / 24;
                            int row = cursorSpr.getY_() / 24;
                            if (selectedUnit != null) {
                                createMovingLayer(selectedUnit, col, row);
                                isMoving = true;
                            }
                            break;
                        case 1:
                            selectedSpr.setVisible(false);
                            selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                            selectedUnit.endTurn(lManager, images[3]);
                            if (movedSpr != null) {
                                lManager.remove(movedSpr);
                            }
                            if (selectedSpr != null) {
                                lManager.remove(selectedSpr);
                            }
                            if (movingLayer != null) {
                                lManager.remove(movingLayer);
                            }
                            isMoved = false;
                            isMoving = false;
                            onSelected = false;
                            break;
                        case 2:
                            System.out.println("Attack");
                            break;
                        default:
                            break;
                    }
                } else {
                    if (!isMoved) {
                        selectedUnit.move(cursorSpr.getX(), cursorSpr.getY());
                        selectedSpr.setVisible(false);
                        movingLayer.setVisible(false);
                        isMoved = true;
                        movedSpr = new Sprite(images[11], 22, 14);
                        movedSpr.setFrame(1);
                        movedSpr.setPosition(cursorSpr.getX_() + 1, cursorSpr.getY_() - 14);
                        lManager.insert(movedSpr, 3);
                    } else {
                        switch (movedSpr.getFrame()) {
                            case 0:
                                System.out.println("ATTACK");
                                break;
                            case 1:
                                selectedSpr.setVisible(false);
                                selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                                selectedUnit.endTurn(lManager, images[3]);
                                if (movedSpr != null) {
                                    lManager.remove(movedSpr);
                                }
                                if (selectedSpr != null) {
                                    lManager.remove(selectedSpr);
                                }
                                if (movingLayer != null) {
                                    lManager.remove(movingLayer);
                                }
                                isMoved = false;
                                isMoving = false;
                                onSelected = false;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        if (!onSelected) {
            cursorSpr.move(action);
        }
    }

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
            keyPressed();
            drawLayer();
            try {
                Thread.sleep(175);
            } catch (InterruptedException ex) {
            }
        }
    }

    public Unit getUnit(int x, int y) {

        Unit unit = getPLUnit(x, y);
        return (unit != null) ? unit : getAIUnit(x, y);
    }

    public Unit getPLUnit(int x, int y) {

        for (int i = 0; i < pl_units.length; i++) {
            if (pl_units[i].getX() == x && pl_units[i].getY() == y) {
                return pl_units[i];
            }
        }
        return null;
    }

    public Unit getAIUnit(int x, int y) {
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i].getX() == x && ai_units[i].getY() == y) {
                return ai_units[i];
            }
        }
        return null;
    }
}
