/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Algorithm.AStar;
import Algorithm.Cell;
import Algorithm.LinkedList;
import Algorithm.Node;
import Attack.CavalryAttack;
import Attack.KnightAttack;
import Attack.RangedAttack;
import Unit.AIUnit;
import Unit.PlayerUnit;
import View.*;
import Unit.Unit;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class TestPathFinding extends MIDlet implements CommandListener {

    private Display mDisplay;

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        mDisplay = Display.getDisplay(this);
        TestPathFindingMap cv = new TestPathFindingMap(false);
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
class TestPathFindingMap extends GameCanvas implements Runnable {

    private int x, y, space;
    private Image[] images;
    private Cursor cursorSpr;
    private Unit[] ai_units, pl_units;
    private Unit selectedUnit;
    private TiledLayer backgroundLayer;
    private LayerManager lManager;
    private boolean onSelected;
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

    public void loadImages() throws IOException {
        images = new Image[21];
        for (int i = 0; i < 21; i++) {
            images[i] = Image.createImage("/Images/" + i + ".png");
        }
    }

    public TestPathFindingMap(boolean suppressKeyEvents) {
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
        cursorSpr = new Cursor(images[7], 24, 24, 9 * 24, 8 * 24);
        cursorSpr.setVisible(true);
        createPLUnits();
        createAIUnits();
        cursorSpr.setPosition(cursorSpr.getX_(), cursorSpr.getY_());
        lManager.insert(cursorSpr, 0);
        lManager.insert(backgroundLayer, lManager.getSize());
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        setActiveView(9 * 24, 8 * 24);
        lManager.paint(getGraphics(), 0, 0);
        flushGraphics();
    }

    void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void keyPressed() {
        int action = getKeyStates();
        if ((action & RIGHT_PRESSED) != 0) {
            if (onSelected) {
                cursorSpr.move(action);
                moveActiveView(action);
            } else {
                moveActiveView(action);
            }
        } else if ((action & LEFT_PRESSED) != 0) {
            if (onSelected) {
                cursorSpr.move(action);
                moveActiveView(action);
            } else {
                moveActiveView(action);
            }
        } else if ((action & UP_PRESSED) != 0) {
            if (onSelected) {
                cursorSpr.move(action);
                moveActiveView(action);
            } else {
                moveActiveView(action);
            }

        } else if ((action & DOWN_PRESSED) != 0) {
            if (onSelected) {
                cursorSpr.move(action);
                moveActiveView(action);
            } else {
                moveActiveView(action);
            }
        } else if ((action & FIRE_PRESSED) != 0) {
            if (!onSelected) {
                selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                if (selectedUnit != null) {
                    onSelected = true;
                    System.out.println("Selected");
                }
            } else {
                System.out.println("Start Find Path");
                LinkedList path = move(cursorSpr.getX_(), cursorSpr.getY_());
                if (path != null) {
                    selectedUnit.move(cursorSpr.getX_(), cursorSpr.getY_());
                    System.out.println("Current Position " + selectedUnit.getX() / 24 + "," + selectedUnit.getY() / 24);
                }else{
                    System.out.println("NO WAY");
                }
            }
        }
        if (!onSelected) {
            cursorSpr.move(action);
        }
    }

    public void run() {
        while (true) {
            cursorSpr.nextFrame();
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
            keyPressed();
            drawLayer();
            try {
                Thread.sleep(175);
            } catch (InterruptedException ex) {
            }
        }
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

    public void setActiveView(int x, int y) {
        this.x = (x < 96) ? 0 : (x > 456) ? 456 - 96 : x - 96;
        this.y = (y < 120) ? 0 : (y > 192) ? 192 - 120 : y - 120;
    }

    public void drawLayer() {
        Graphics g = this.getGraphics();
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(g, 0, 0);
        flushGraphics();
    }

    public Node[][] createMovingLayer() {
        int col = selectedUnit.getX() / 24;
        int row = selectedUnit.getY() / 24;
        int space = selectedUnit.getMoveSpace();
        System.out.println("Space move " + space);
        gridCols = space * 2 + 1;
        gridRows = space * 2 + 1;
        Node[][] movingCells = new Node[gridCols][gridRows];
        int counter = 0;
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                int a = i + col - space, b = j + row - space;
                //System.out.println(a + "," + b);
                if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                    if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                        if (backgroundLayer.getCell(a, b) < 10) {
                            if (getPLUnit(a * 24, b * 24) == null) {
                                if (getAIUnit(a * 24, b * 24) == null) {
                                    movingCells[i][j] = new Node(new Cell(a, b), counter);
                                } else {
                                    movingCells[i][j] = new Node(new Cell(a, b, false), counter);
                                }
                            } else {
                                movingCells[i][j] = new Node(new Cell(a, b), counter);
                            }
                        } else {
                            movingCells[i][j] = new Node(new Cell(a, b, false), counter);
                        }
                    } else {
                        movingCells[i][j] = new Node(new Cell(a, b, false), counter);
                    }
                } else {
                    if (j == space && i == space) {
                        movingCells[i][j] = new Node(new Cell(a, b), counter);
                        start = movingCells[i][j];
                    } else {
                        movingCells[i][j] = new Node(new Cell(a, b, false), counter);
                    }
                }
                counter++;
            }
        }
        System.out.println("counter " + counter);
        return movingCells;
    }
    int gridCols, gridRows;
    Node start, goal;
    public AStar atar;

    public LinkedList move(int goalX, int goalY) {
        int x_ = goalX / 24, y_ = goalY / 24;
        //if (backgroundLayer.getCell(x_, y_) < 10 && getAIUnit(x_, y_) == null) {
        System.out.println("Create PathFinder");
        goal = new Node(new Cell(x_, y_), -1);
        LinkedList openList = new LinkedList();
        LinkedList closedList = new LinkedList();
        AStar astar = new AStar(openList, closedList);
        Node[][] movingMap = createMovingLayer();
        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {
                //System.out.println(x+","+y);
                System.out.println(x + "," + y + " NodeID:" + movingMap[x][y].getNodeId() + " " + movingMap[x][y].getX() + "," + movingMap[x][y].getY() + " " + movingMap[x][y].blocked);
                //LinkedList neighbours = new LinkedList();
                if (y - 1 >= 0) {
                    movingMap[x][y].getNeighbours()[0] = movingMap[x][y - 1];
                }
                if (x + 1 < gridCols) {
                    movingMap[x][y].getNeighbours()[1] = movingMap[x + 1][y];
                }
                if (y + 1 < gridRows) {
                    movingMap[x][y].getNeighbours()[2] = movingMap[x][y + 1];
                }
                if (x - 1 >= 0) {
                    movingMap[x][y].getNeighbours()[3] = movingMap[x - 1][y];
                }
            }
        }
        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {
                System.out.println(x + "," + y + " NodeID:" + movingMap[x][y].getNodeId() + " " + movingMap[x][y].getX() + "," + movingMap[x][y].getY() + " " + movingMap[x][y].blocked);
                System.out.println("Neighbours:");
                for (int i = 0; i < movingMap[x][y].getNeighbours().length; i++) {
                    System.out.println(movingMap[x][y].getNeighbours()[i]);
                }
            }
        }
        System.out.println("Finish PathFinder");
        System.out.println("SNode " + start.getX() + "," + start.getY() + " GNode " + goal.getX() + "," + goal.getY());
        return astar.findPath(start, goal);
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

    public void createAIUnits() {
        ai_units[0] = new AIUnit(1, 7, images[14], 5, new CavalryAttack(images[5], lManager));
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
        pl_units[1] = new PlayerUnit(3, 3, images[18], 4, new KnightAttack(images[4], lManager));
        pl_units[2] = new PlayerUnit(9, 3, images[20], 3, new RangedAttack(images[6], lManager));
        pl_units[3] = new PlayerUnit(2, 8, images[17], 5, new CavalryAttack(images[5], lManager));
        pl_units[4] = new PlayerUnit(12, 3, images[19], 5, new CavalryAttack(images[5], lManager));
        for (int i = 0; i < pl_units.length; i++) {
            if (pl_units[i] != null) {
                lManager.append(pl_units[i].getSprite());
            }
        }
    }
}
