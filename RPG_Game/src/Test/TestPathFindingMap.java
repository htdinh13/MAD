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
import Unit.Unit;
import View.Cursor;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class TestPathFindingMap extends GameCanvas implements Runnable {

    public int x, y, space;
    public Image[] images;
    public Cursor cursorSpr;
    public Unit selectedUnit;
    public Unit[] ai_units, pl_units;
    public TiledLayer backgroundLayer;
    public LayerManager lManager;
    public boolean onSelected;
    public final int cells[][] = {
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
                selectedUnit = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
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
                    path.print();
                } else {
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
        gridCols = space * 2 + 1;
        gridRows = space * 2 + 1;
        Node[][] movingCells = new Node[gridCols][gridRows];
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                int a = i + col - space, b = j + row - space;
                if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                    if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                        if (backgroundLayer.getCell(a, b) < 10) {
                            if (getPLUnit(a * 24, b * 24) == null) {
                                if (getAIUnit(a * 24, b * 24) == null) {
                                    movingCells[i][j] = new Node(new Cell(a, b));
                                } else {
                                    movingCells[i][j] = new Node(new Cell(a, b, false));
                                }
                            } else {
                                movingCells[i][j] = new Node(new Cell(a, b));
                            }
                        } else {
                            movingCells[i][j] = new Node(new Cell(a, b, false));
                        }
                    } else {
                        movingCells[i][j] = new Node(new Cell(a, b, false));
                    }
                } else {
                    if (j == space && i == space) {
                        movingCells[i][j] = new Node(new Cell(a, b));
                        start = movingCells[i][j];
                    } else {
                        movingCells[i][j] = new Node(new Cell(a, b, false));
                    }
                }
            }
        }
        return movingCells;
    }
    int gridCols, gridRows;
    Node start, goal;
    public AStar atar;

    public LinkedList move(int goalX, int goalY) {
        int x_ = goalX / 24, y_ = goalY / 24;
        goal = new Node(new Cell(x_, y_));
        LinkedList openList = new LinkedList();
        LinkedList closedList = new LinkedList();
        AStar astar = new AStar(openList, closedList);
        Node[][] movingMap = createMovingLayer();
        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {
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
        ai_units[0] = new AIUnit(3, 4, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[1] = new AIUnit(10, 3, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[2] = new AIUnit(11, 2, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[3] = new AIUnit(11, 3, images[14], 5, new CavalryAttack(images[5], lManager));
        ai_units[4] = new AIUnit(11, 4, images[14], 5, new CavalryAttack(images[5], lManager));
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
        pl_units[3] = new PlayerUnit(2, 6, images[17], 5, new CavalryAttack(images[5], lManager));
        pl_units[4] = new PlayerUnit(3, 7, images[19], 5, new CavalryAttack(images[5], lManager));
        for (int i = 0; i < pl_units.length; i++) {
            if (pl_units[i] != null) {
                lManager.append(pl_units[i].getSprite());
            }
        }
    }

    public void sortUnitsByHealth(Unit[] units) {
        for (int i = 0; i < units.length - 1; i++) {
            for (int j = i + 1; j < units.length; j++) {
                if (units[i] != null && units[j] != null) {
                    if (units[j].getHealth() < units[i].getHealth()) {
                        Unit tmp = units[i];
                        units[i] = units[j];
                        units[j] = tmp;
                    }
                }
            }
        }
    }

    public Unit getUnit(int x, int y) {
        Unit unit = getPLUnit(x, y);
        return (unit != null) ? unit : getAIUnit(x, y);
    }
}