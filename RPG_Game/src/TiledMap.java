/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luu Manh 13
 */
import Attack.Attackable;
import Attack.CavalryAttack;
import Attack.KnightAttack;
import Attack.RangedAttack;
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

/**
 *
 * @author HOANG TRUONG DINH
 */
class GameCanvasTiledLayerDemo extends GameCanvas implements Runnable {

    private int x, y, index;
    private Image[] images;
    private Cursor cursorSpr;
    private Sprite selectedSpr, movedSpr;
    private Unit[] ai_units, pl_units;
    private TiledLayer backgroundLayer, movingLayer, attackingLayer;
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
    private Cell attackCells[];
    private boolean onSelected, isMoved, isMoving, isAI, isAttacking;
    private Unit selectedUnit;

    public void loadImages() throws IOException {
        images = new Image[21];
        for (int i = 0; i < 21; i++) {
            images[i] = Image.createImage("/Images/" + i + ".png");
        }
    }

    public GameCanvasTiledLayerDemo(boolean suppressKeyEvents) {
        super(suppressKeyEvents);
        x = 0;
        y = 0;
        index = 0;
        onSelected = false;
        isMoved = false;
        isAI = false;
        isMoving = false;
        isAttacking = false;
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

        //Create attackable
//        Attackable attack = new KnightAttack(images[4],lManager);
//        lManager.insert(attack.getAttackSpr(), 0);
//        attack.start();
        //end attackable

        createPLUnits();
        createAIUnits();

        lManager.insert(cursorSpr, 1);
        lManager.insert(backgroundLayer, lManager.getSize());
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(getGraphics(), 0, 0);
        flushGraphics();
    }

    public void createAIUnits() {
        ai_units[0] = new AIUnit(10, 2, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[1] = new AIUnit(10, 3, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[2] = new AIUnit(11, 2, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[3] = new AIUnit(11, 3, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[4] = new AIUnit(11, 0, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[5] = new AIUnit(11, 5, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[6] = new AIUnit(12, 0, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[7] = new AIUnit(12, 5, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[8] = new AIUnit(13, 2, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[9] = new AIUnit(13, 3, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[10] = new AIUnit(15, 2, images[13], 4, new KnightAttack(images[4], lManager));
        ai_units[11] = new AIUnit(15, 3, images[13], 4, new KnightAttack(images[4], lManager));
        ai_units[12] = new AIUnit(16, 2, images[13], 4, new KnightAttack(images[4], lManager));
        ai_units[13] = new AIUnit(16, 3, images[13], 4, new KnightAttack(images[4], lManager));
        ai_units[14] = new AIUnit(20, 9, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[15] = new AIUnit(21, 9, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[16] = new AIUnit(22, 9, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[17] = new AIUnit(20, 14, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[18] = new AIUnit(21, 14, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[19] = new AIUnit(22, 14, images[12], 3, new RangedAttack(images[6], lManager));
        ai_units[20] = new AIUnit(21, 13, images[15], 6, new CavalryAttack(images[5], lManager));
        ai_units[21] = new AIUnit(20, 12, images[13], 4, new KnightAttack(images[4], lManager));
        ai_units[22] = new AIUnit(22, 12, images[13], 4, new KnightAttack(images[4], lManager));
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i] != null) {
                lManager.append(ai_units[i].getSprite());
            }
        }
    }

    public void createPLUnits() {
        pl_units[0] = new PlayerUnit(2, 2, images[16], 5, new CavalryAttack(images[5], lManager));
        pl_units[1] = new PlayerUnit(3, 2, images[18], 4, new KnightAttack(images[4], lManager));
        pl_units[2] = new PlayerUnit(3, 3, images[20], 3, new RangedAttack(images[6], lManager));
        pl_units[3] = new PlayerUnit(2, 10, images[17], 5, new CavalryAttack(images[5], lManager));
        pl_units[4] = new PlayerUnit(22, 3, images[19], 5, new CavalryAttack(images[5], lManager));
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

    public void createMovingLayer(Unit selectedUnit) {
        int col = selectedUnit.getX() / 24;
        int row = selectedUnit.getY() / 24;
        int space = selectedUnit.getMoveSpace();
        movingLayer = new TiledLayer(space * 2 + 1, space * 2 + 1, images[10], 24, 24);
        for (int i = 0; i < space * 2 + 1; i++) {
            for (int j = 0; j < space * 2 + 1; j++) {
                if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                    int a = j + col - space, b = i + row - space;
                    if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                        if (backgroundLayer.getCell(a, b) < 10) {
                            if (getPLUnit(a * 24, b * 24) == null) {
                                if (getAIUnit(a * 24, b * 24) == null) {
                                    movingLayer.setCell(j, i, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        movingLayer.setPosition((col - space) * 24, (row - space) * 24);
        lManager.insert(movingLayer, 28);
    }

    public void createAttackingLayer(Unit selectedUnit) {
        int col = cursorSpr.getX_() / 24;
        int row = cursorSpr.getY_() / 24;
        int space = 0;
        Attackable attackType = selectedUnit.getAttackType();
        if (attackType instanceof CavalryAttack || attackType instanceof KnightAttack) {
            space = 1;
            attackCells = new Cell[4];
            int counter = 0;
            attackingLayer = new TiledLayer(space * 2 + 1, space * 2 + 1, images[10], 24, 24);
            for (int i = 0; i < space * 2 + 1; i++) {
                for (int j = 0; j < space * 2 + 1; j++) {
                    if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                        int a = j + col - space, b = i + row - space;
                        if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                            if (backgroundLayer.getCell(a, b) < 10) {
                                if (getPLUnit(a * 24, b * 24) == null) {
                                    attackingLayer.setCell(j, i, 1);
                                    attackCells[counter] = new Cell(a, b);
                                    counter++;
                                }
                            }
                        }
                    }
                }
            }
        } else if (attackType instanceof RangedAttack) {
            space = 2;
            attackCells = new Cell[8];
            int counter = 0;
            attackingLayer = new TiledLayer(space * 2 + 1, space * 2 + 1, images[10], 24, 24);
            for (int i = 0; i < space * 2 + 1; i++) {
                for (int j = 0; j < space * 2 + 1; j++) {
                    if (!(j == space && i == space) && (i + j) % 2 == 0 && ((i > 0 && i < 4) || (j < 4 && j > 0)) && i % 2 == 0 && j % 2 == 0) {
                        int a = j + col - space, b = i + row - space;
                        if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                            if (backgroundLayer.getCell(a, b) < 10) {
                                if (getPLUnit(a * 24, b * 24) == null) {
                                    attackingLayer.setCell(j, i, 1);
                                    attackCells[counter] = new Cell(a, b);
                                    counter++;
                                }
                            }
                        }
                    }
                }
            }
        }
        attackingLayer.setPosition((col - space) * 24, (row - space) * 24);
        lManager.insert(attackingLayer, 2);
    }

    protected void keyPressed(int keyCode) {
        //int gameAction = getGameAction(keyCode);
        switch (keyCode) {
            // Use '0' for show the movement range
            case KEY_NUM0:
                if (!onSelected) {

                    selectedUnit = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
                    if (selectedUnit != null) {
                        isAI = true;
                    } else {
                        selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                    }
                    if (selectedUnit != null && !selectedUnit.getEndTurn()) {
                        if (isAI) {
                            createMovingLayer(selectedUnit);
                        } else {
                            createMovingLayer(selectedUnit);
                        }
                    }
                }
                break;
            // Use '#' for cancel or undo command    
            case KEY_POUND:
                if (onSelected) {
                    if (isMoving) {
                        if (isMoved) {
                            if (isAttacking) {
                                isAttacking = false;
                                lManager.remove(attackingLayer);
                                movedSpr.setVisible(true);
                                cursorSpr.move(selectedUnit.getX(), selectedUnit.getY());
                            } else {
                                isMoved = false;
                                ((PlayerUnit) selectedUnit).unmove();
                                lManager.remove(movedSpr);
                                setActiveView(selectedUnit.getX(), selectedUnit.getY());
                                //selectedSpr.setVisible(true);
                                movingLayer.setVisible(true);
                                cursorSpr.move(selectedUnit.getX(), selectedUnit.getY());
                            }
                        } else {
                            isMoving = false;
                            cursorSpr.move(selectedUnit.getX(), selectedUnit.getY());
                            setActiveView(selectedUnit.getX(), selectedUnit.getY());
                            lManager.remove(movingLayer);
                            selectedSpr.setVisible(true);
                        }
                    } else {
                        if (isAttacking) {
                            isAttacking = false;
                            lManager.remove(attackingLayer);
                            selectedSpr.setVisible(true);
                            cursorSpr.move(selectedUnit.getX(), selectedUnit.getY());
                        } else {
                            onSelected = false;
                            lManager.remove(selectedSpr);
                        }
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
                        if (isAttacking) {
                            index = nextAttackCell(index);
                            if (index >= 0) {
                                cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                                setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            }
                        } else {
                            movedSpr.nextFrame();
                        }
                    } else {
                        cursorSpr.move(action);
                        moveActiveView(action);
                    }
                } else {
                    if (isAttacking) {
                        index = nextAttackCell(index);
                        if (index >= 0) {
                            cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        }
                    } else {
                        selectedSpr.nextFrame();
                    }
                }
            } else {
                moveActiveView(action);
            }
        } else if ((action & LEFT_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving) {
                    if (isMoved) {
                        if (isAttacking) {
                            index = prevAttackCell(index);
                            if (index >= 0) {
                                cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                                setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            }
                        } else {
                            movedSpr.prevFrame();
                        }
                    } else {
                        cursorSpr.move(action);
                        moveActiveView(action);
                    }
                } else {
                    if (isAttacking) {
                        index = prevAttackCell(index);
                        if (index >= 0) {
                            cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        }
                    } else {
                        selectedSpr.prevFrame();
                    }
                }
            } else {
                moveActiveView(action);
            }
        } else if ((action & UP_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving && !isMoved && !isAttacking) {
                    cursorSpr.move(action);
                    moveActiveView(action);
                } else if (isAttacking) {
                    index = nextAttackCell(index);
                    if (index >= 0) {
                        cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                    }
                }
            } else {
                moveActiveView(action);
            }

        } else if ((action & DOWN_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving && !isMoved && !isAttacking) {
                    cursorSpr.move(action);
                    moveActiveView(action);
                } else if (isAttacking) {
                    index = prevAttackCell(index);
                    if (index >= 0) {
                        cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                    }
                }
            } else {

                moveActiveView(action);

            }
        } else if ((action & FIRE_PRESSED) != 0) {
            if (!onSelected) {
                selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                if (selectedUnit != null && !selectedUnit.getEndTurn()) {
                    onSelected = true;
                    selectedSpr = new Sprite(images[0], 30, 14);
                    if (selectedUnit.getY() == 0) {
                        selectedSpr.setPosition(selectedUnit.getX() - 3, selectedUnit.getY() + 24);
                    } else {
                        selectedSpr.setPosition(selectedUnit.getX() - 3, selectedUnit.getY() - 14);
                    }
                    lManager.insert(selectedSpr, 2);
                }
            } else {
                if (!isMoving) {
                    if (!isAttacking) {
                        switch (selectedSpr.getFrame()) {
                            // Change to moving state
                            case 0:
                                createMovingLayer(selectedUnit);
                                selectedSpr.setVisible(false);
                                isMoving = true;
                                break;
                            // End Turn without move    
                            case 1:
                                unitEndTurned();
                                break;
                            // Attack without move    
                            case 2:
                                //System.out.println("Attack");
                                isAttacking = true;
                                createAttackingLayer(selectedUnit);
                                selectedSpr.setVisible(false);
                                break;
                            default:
                                break;
                        }
                    } else {
                        Unit attacked = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
                        if (attacked != null) {
                            selectedUnit.getAttackType().attack(selectedUnit, attacked);
                            selectedUnit.getAttackType().start();
                            unitEndTurned();
                        }
                    }
                } else {
                    if (!isMoved) {
                        // Change to moved state (can undo)
                        selectedUnit.move(cursorSpr.getX(), cursorSpr.getY());
                        selectedSpr.setVisible(false);
                        movingLayer.setVisible(false);
                        isMoved = true;
                        movedSpr = new Sprite(images[11], 22, 14);
                        movedSpr.setFrame(1);
                        if (cursorSpr.getY_() == 0) {
                            movedSpr.setPosition(cursorSpr.getX_() + 1, cursorSpr.getY_() + 24);
                        } else {
                            movedSpr.setPosition(cursorSpr.getX_() + 1, cursorSpr.getY_() - 14);
                        }
                        lManager.insert(movedSpr, 3);
                    } else {
                        if (!isAttacking) {
                            switch (movedSpr.getFrame()) {
                                //Attack after moved
                                case 0:
                                    isAttacking = true;
                                    createAttackingLayer(selectedUnit);
                                    movedSpr.setVisible(false);
                                    break;

                                //End Turn after moved    
                                case 1:
                                    unitEndTurned();
                                default:
                                    break;
                            }
                        } else {
                            Unit attacked = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
                            if (attacked != null) {
                                selectedUnit.getAttackType().attack(selectedUnit, attacked);
                                //lManager.insert(selectedUnit.getAttackType().getAttackSpr(), 0);
                                selectedUnit.getAttackType().start();
                                unitEndTurned();
                            }
                        }
                    }
                }
            }
        }
        if (!onSelected) {
            cursorSpr.move(action);
        }
    }

    public void unitEndTurned() {
        //selectedSpr.setVisible(false);
        //selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
        cursorSpr.move(selectedUnit.getX(), selectedUnit.getY());
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
        if (attackingLayer != null) {
            lManager.remove(attackingLayer);
        }
        isMoved = false;
        isMoving = false;
        onSelected = false;
        isAttacking = false;
    }

    public int nextAttackCell(int index) {
        if (attackCells[0] == null) {
            return -1;
        }
        if (index == attackCells.length - 1) {
            return 0;
        } else {
            return (attackCells[++index] != null) ? index : nextAttackCell(index);
        }
    }

    public int prevAttackCell(int index) {
        if (attackCells[0] == null) {
            return -1;
        }
        if (index == 0) {
            index = attackCells.length;
            return prevAttackCell(index);
        } else {
            return (attackCells[--index] != null) ? index : prevAttackCell(index);
        }
    }

    public void setActiveView(int x, int y) {
        this.x = (x < 96) ? 0 : (x > 456) ? 456 - 96 : x - 96;
        this.y = (y < 120) ? 0 : (y > 192) ? 192 - 120 : y - 120;
    }

    public void moveActiveView(int action) {
        if ((action & RIGHT_PRESSED) != 0) {
            if (x < (600 - this.getWidth()) && cursorSpr.getX_() >= 96) {
                x += 24;
            }
        } else if ((action & LEFT_PRESSED) != 0) {
            if (x > 0 && cursorSpr.getX_() <= 456) {
                x -= 24;
            }
        } else if ((action & UP_PRESSED) != 0) {
            if (y > 0 && cursorSpr.getY_() <= 208) {
                y -= 24;
            }
        } else if ((action & DOWN_PRESSED) != 0) {
            if (y < (312 - this.getWidth()) && cursorSpr.getY_() >= 120) {
                y += 24;
            }
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
