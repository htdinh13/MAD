/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Algorithm.Cell;
import Algorithm.LinkedList;
import Algorithm.Node;
import Attack.CavalryAttack;
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

/**
 *
 * @author Luu Manh 13
 */
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

    private int x, y;
    private Image[] images;
    private Cursor cursorSpr;
    private Unit pl_unit, selectedUnit;
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
        cursorSpr.setPosition(cursorSpr.getX_(), cursorSpr.getY_());
        pl_unit = new PlayerUnit(9, 8, images[16], 5, new CavalryAttack(images[5], lManager));
        lManager.insert(cursorSpr, 0);
        lManager.append(pl_unit.getSprite());
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
                move(cursorSpr.getX_() / 24, cursorSpr.getY_() / 24);
                selectedUnit.move(cursorSpr.getX_(), cursorSpr.getY_());
                System.out.println("Current Position " + selectedUnit.getX() / 24 + "," + selectedUnit.getY() / 24);
            }
        }
        if (!onSelected) {
            cursorSpr.move(action);
        }
    }

    public void run() {
        while (true) {
            cursorSpr.nextFrame();
            pl_unit.getSprite().nextFrame();
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

    public Unit getPLUnit(int x, int y) {
        if (pl_unit != null && pl_unit.getX() == x && pl_unit.getY() == y) {
            return pl_unit;
        }
        return null;
    }

    public void drawLayer() {

        Graphics g = this.getGraphics();
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(g, 0, 0);
        flushGraphics();
    }

    public void move(int x, int y) {
        try {
            boolean pathFound = false;
            LinkedList openList = new LinkedList();
            LinkedList closedList = new LinkedList();
            int startX = selectedUnit.getX() / 24;
            int startY = selectedUnit.getY() / 24;
            openList.addToHead(new Node(new Cell(startX, startY, false), x, y));

            Node currentNode;
            do {
                currentNode = openList.getMinFScore();
                //currentNode.computeScore(null, x, y);
                System.out.println("FScore" + currentNode.fScore + "HScore" + currentNode.hScore);
                startX = currentNode.data.getX();
                startY = currentNode.data.getY();
                closedList.addToHead(currentNode);
                openList.removeNode(currentNode);
                if (closedList.findNode(new Cell(x, y)) >= 0) {
                    pathFound = true;
                    break;
                }
                LinkedList adjacentList = new LinkedList();
                if (backgroundLayer.getCell(startX, startY - 1) < 10) {
                    adjacentList.addToHead(new Node(new Cell(startX, startY - 1, true), x, y));
                }
                if (backgroundLayer.getCell(startX - 1, startY) < 10) {
                    adjacentList.addToHead(new Node(new Cell(startX - 1, startY, true), x, y));
                }
                if (backgroundLayer.getCell(startX, startY + 1) < 10) {
                    adjacentList.addToHead(new Node(new Cell(startX, startY + 1, true), x, y));
                }
                if (backgroundLayer.getCell(startX + 1, startY) < 10) {
                    adjacentList.addToHead(new Node(new Cell(startX + 1, startY, true), x, y));
                }
                Node traversalNode = adjacentList.head;
                while (traversalNode != null) {
                    if (closedList.findNode(traversalNode.data) >= 0) {
                        traversalNode = traversalNode.next;
                        continue;
                    }
                    if (openList.findNode(traversalNode.data) < 0) {
                        traversalNode.computeScore(currentNode, x, y);
                        openList.addToHead(traversalNode);
                    } else {
                        System.out.println("current gScore " + currentNode.gScore + " traversalNode gScore " + traversalNode.gScore);
                        if (currentNode.gScore + 1 < traversalNode.gScore) {
                            traversalNode.gScore = currentNode.gScore + 1;
                            openList.removeNode(traversalNode);
                            openList.addToHead(traversalNode);
                        }
                    }
                    traversalNode = traversalNode.next;
                }
                
            } while (!openList.isEmpty());
            if (pathFound) {
                Node current = closedList.head;
                int c = 1;
                while (current != null) {
                    System.out.println("Step " + c + " (" + current.data.getX() + "," + current.data.getY() + ")");
                    c++;
                    current = current.next;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
