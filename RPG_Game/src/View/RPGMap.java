package View;

import Algorithm.AStar;
import Algorithm.Cell;
import Algorithm.LinkedList;
import Algorithm.Node;
import Attack.Attackable;
import Attack.CavalryAttack;
import Attack.KnightAttack;
import Attack.RangedAttack;
import Model.GameHandler;
import Model.SoundPlayer;
import Unit.AIUnit;
import Unit.PlayerUnit;
import Unit.Unit;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.midlet.MIDlet;

public class RPGMap extends GameCanvas implements Runnable, CommandListener {

    public SoundPlayer soundPlayer;
    private int x, y, index;
    public Image[] images;
    public Cursor cursorSpr;
    private Sprite selectedSpr;
    public Sprite movedSpr;
    public Unit[] ai_units, pl_units;
    public TiledLayer backgroundLayer, movingLayer, attackingLayer;
    public LayerManager lManager;
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
    private Node movingCells[][];
    private boolean onSelected, isMoved, isMoving, isAttacking;
    private Unit selectedUnit;
    private int gridCols, gridRows;
    private Node start, goal;
    private AStar astar;
    private LinkedList path, openList, closedList;
    private String statusStr;
    public Thread keyThread;
    public GameHandler game;
    Command cmdMenu, cmdExit;
    public MIDlet mainMidlet;

    public void loadImages() throws IOException {
        images = new Image[21];
        for (int i = 0; i < 21; i++) {
            images[i] = Image.createImage("/Images/" + i + ".png");
        }
    }

    public RPGMap(boolean suppressKeyEvents, MIDlet mainMid) {
        super(suppressKeyEvents);
        this.mainMidlet = mainMid;
        this.x = 0;
        this.y = 0;
        this.index = 0;
        this.onSelected = false;
        this.isMoved = false;
        this.isMoving = false;
        this.isAttacking = false;
        this.ai_units = new Unit[23];
        this.pl_units = new Unit[5];
        this.lManager = new LayerManager();
        this.statusStr = "";

        openList = new LinkedList();
        closedList = new LinkedList();
        astar = new AStar(openList, closedList);

        try {
            loadImages();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.backgroundLayer = new TiledLayer(25, 15, images[2], 24, 24);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 25; j++) {
                this.backgroundLayer.setCell(j, i, cells[i][j]);
            }
        }
        this.cursorSpr = new Cursor(images[7], 24, 24, this.getWidth() / 2 - 24, (this.getHeight() - 3) / 2 - 24);
        this.cursorSpr.setVisible(true);
        this.cursorSpr.setPosition(cursorSpr.getX_(), cursorSpr.getY_());

        createPLUnits();
        createAIUnits();
        game = new GameHandler(ai_units, pl_units, this);

        this.lManager.insert(cursorSpr, 1);
        this.lManager.insert(backgroundLayer, lManager.getSize());
        this.lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        this.lManager.paint(getGraphics(), 0, 0);
        flushGraphics();

        keyThread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    keyPressed();
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        keyThread.start();
        soundPlayer = new SoundPlayer();
        soundPlayer.start();
        cmdMenu = new Command("Menu", Command.BACK, 0);
        this.addCommand(cmdMenu);
        cmdExit = new Command("Exit", Command.EXIT, 0);
        this.addCommand(cmdExit);
        this.setCommandListener(this);
    }

    public void createAIUnits() {
        ai_units[0] = new AIUnit(10, 2, images[14],images[3], 5, new CavalryAttack(images[5], this));
        ai_units[1] = new AIUnit(10, 3, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[2] = new AIUnit(11, 2, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[3] = new AIUnit(11, 3, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[4] = new AIUnit(11, 0, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[5] = new AIUnit(11, 5, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[6] = new AIUnit(12, 0, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[7] = new AIUnit(12, 5, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[8] = new AIUnit(13, 2, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[9] = new AIUnit(13, 3, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[10] = new AIUnit(15, 2, images[13], images[3], 4, new KnightAttack(images[4], this));
        ai_units[11] = new AIUnit(15, 3, images[13], images[3], 4, new KnightAttack(images[4], this));
        ai_units[12] = new AIUnit(16, 2, images[13], images[3], 4, new KnightAttack(images[4], this));
        ai_units[13] = new AIUnit(16, 3, images[13], images[3], 4, new KnightAttack(images[4], this));
        ai_units[14] = new AIUnit(20, 9, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[15] = new AIUnit(21, 9, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[16] = new AIUnit(22, 9, images[14], images[3], 5, new CavalryAttack(images[5], this));
        ai_units[17] = new AIUnit(20, 14, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[18] = new AIUnit(21, 14, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[19] = new AIUnit(22, 14, images[12], images[3], 3, new RangedAttack(images[6], this));
        ai_units[20] = new AIUnit(21, 13, images[15], images[3], 6, new CavalryAttack(images[5], this), 150, 50, 20);
        ai_units[21] = new AIUnit(20, 12, images[13], images[3], 4, new KnightAttack(images[4], this));
        ai_units[22] = new AIUnit(22, 12, images[13], images[3], 4, new KnightAttack(images[4], this));
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i] != null) {
                lManager.append(ai_units[i].getSprite());
            }
        }
    }

    public void createPLUnits() {
        pl_units[0] = new PlayerUnit(2, 2, images[16], images[3], 5, new CavalryAttack(images[5], this), 120, 40, 15);
        pl_units[1] = new PlayerUnit(3, 3, images[18], images[3], 4, new KnightAttack(images[4], this), 150, 30, 20);
        pl_units[2] = new PlayerUnit(3, 1, images[20], images[3], 3, new RangedAttack(images[6], this), 80, 35, 10);
        pl_units[3] = new PlayerUnit(2, 7, images[17], images[3], 5, new CavalryAttack(images[5], this), 140, 50, 15);
        pl_units[4] = new PlayerUnit(3, 7, images[19], images[3], 5, new CavalryAttack(images[5], this), 140, 50, 15);
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
        cursorSpr.nextFrame();
    }

    public Node[][] createMovingNodes(Unit selectedUnit) {
        int col = selectedUnit.getX() / 24;
        int row = selectedUnit.getY() / 24;
        int space = selectedUnit.getMoveSpace();
        gridCols = space * 2 + 1;
        gridRows = space * 2 + 1;
        movingLayer = new TiledLayer((gridCols), gridRows, images[10], 24, 24);
        movingCells = new Node[gridCols][gridRows];
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                int a = i + col - space, b = j + row - space;
                if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                    if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                        if (backgroundLayer.getCell(a, b) < 10) {
                            if (getPLUnit(a * 24, b * 24) == null) {
                                if (getAIUnit(a * 24, b * 24) == null) {
                                    movingLayer.setCell(i, j, 2);
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
        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {
                if (y - 1 >= 0) {
                    movingCells[x][y].getNeighbours()[0] = movingCells[x][y - 1];
                }
                if (x + 1 < gridCols) {
                    movingCells[x][y].getNeighbours()[1] = movingCells[x + 1][y];
                }
                if (y + 1 < gridRows) {
                    movingCells[x][y].getNeighbours()[2] = movingCells[x][y + 1];
                }
                if (x - 1 >= 0) {
                    movingCells[x][y].getNeighbours()[3] = movingCells[x - 1][y];
                }
            }
        }
        movingLayer.setPosition((col - space) * 24, (row - space) * 24);
        lManager.insert(movingLayer, lManager.getSize() - 1);
        return movingCells;
    }

    

    public void createMovingLayer(Unit selectedUnit) {
        int col = selectedUnit.getX() / 24;
        int row = selectedUnit.getY() / 24;
        int space = selectedUnit.getMoveSpace();
        gridCols = space * 2 + 1;
        gridRows = space * 2 + 1;
        movingLayer = new TiledLayer((gridCols), gridRows, images[10], 24, 24);
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                int a = i + col - space, b = j + row - space;
                if (!(j == space && i == space) && ((i <= space) && (j >= space - i) && (j <= space + i) || ((i > space) && (j >= i - space) && (j < space * 2 + 1 - i + space)))) {
                    if (a >= 0 && b >= 0 && a < 25 && b < 15) {
                        if (backgroundLayer.getCell(a, b) < 10) {
                            if (getPLUnit(a * 24, b * 24) == null) {
                                if (getAIUnit(a * 24, b * 24) == null) {
                                    movingLayer.setCell(i, j, 2);
                                }
                            }

                        }
                    }
                }
            }
        }
        movingLayer.setPosition((col - space) * 24, (row - space) * 24);
        lManager.insert(movingLayer, lManager.getSize() - 1);
    }

    public void resetMovingCells() {
        for (int x_ = 0; x_ < gridCols; x_++) {
            for (int y_ = 0; y_ < gridRows; y_++) {
                movingCells[x_][y_].reset();
            }
        }
    }

    public LinkedList move(int goalX, int goalY) {
        int x_ = goalX / 24, y_ = goalY / 24;
        goal = new Node(new Cell(x_, y_));
        return astar.findPath(start, goal);
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
        switch (keyCode) {
            case KEY_NUM0:
                if (!onSelected) {

                    selectedUnit = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
                    if (selectedUnit != null) {
                    } else {
                        selectedUnit = getPLUnit(cursorSpr.getX_(), cursorSpr.getY_());
                    }
                    if (selectedUnit != null && !selectedUnit.getEndTurn()) {
                        createMovingLayer(selectedUnit);
                        statusStr = selectedUnit.getHealth() + "/" + selectedUnit.getAttack() + "/" + selectedUnit.getDefence();
                    }
                }
                break;
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
                game.endTurnAllUnit(pl_units);
                break;
            default:
                break;
        }
    }

    protected void keyReleased(int keyCode) {
        switch (keyCode) {
            case KEY_NUM0:
                if (!onSelected) {
                    if (selectedUnit != null && movingLayer != null) {
                        lManager.remove(movingLayer);
                        statusStr = "";
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
                            index = moveAttackCell(0);
                            if (index >= 0) {
                                cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                                setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            }
                        } else {
                            movedSpr.nextFrame();
                        }
                    } else {
                        cursorSpr.move(action);
                        moveActiveView(3);
                    }
                } else {
                    if (isAttacking) {
                        index = moveAttackCell(0);
                        if (index >= 0) {
                            cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        }
                    } else {
                        selectedSpr.nextFrame();
                    }
                }
            } else {
                moveActiveView(3);
            }
        } else if ((action & LEFT_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving) {
                    if (isMoved) {
                        if (isAttacking) {
                            index = moveAttackCell(1);
                            if (index >= 0) {
                                cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                                setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            }
                        } else {
                            movedSpr.prevFrame();
                        }
                    } else {
                        cursorSpr.move(action);
                        moveActiveView(1);
                    }
                } else {
                    if (isAttacking) {
                        index = moveAttackCell(1);
                        if (index >= 0) {
                            cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                            setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        }
                    } else {
                        selectedSpr.prevFrame();
                    }
                }
            } else {
                moveActiveView(1);
            }
        } else if ((action & UP_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving && !isMoved && !isAttacking) {
                    cursorSpr.move(action);
                    moveActiveView(0);
                } else if (isAttacking) {
                    index = moveAttackCell(2);
                    if (index >= 0) {
                        cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                    }
                }
            } else {
                moveActiveView(0);
            }

        } else if ((action & DOWN_PRESSED) != 0) {
            if (onSelected) {
                if (isMoving && !isMoved && !isAttacking) {
                    cursorSpr.move(action);
                    moveActiveView(2);
                } else if (isAttacking) {
                    index = moveAttackCell(3);
                    if (index >= 0) {
                        cursorSpr.move(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                        setActiveView(attackCells[index].getX() * 24, attackCells[index].getY() * 24);
                    }
                }
            } else {

                moveActiveView(2);

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
                            case 0:
                                createMovingNodes(selectedUnit);
                                selectedSpr.setVisible(false);
                                isMoving = true;
                                break;
                            case 1:
                                unitEndTurned();
                                break;
                            case 2:
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
                            if (attacked.getHealth() <= 0) {
                                attacked.isDead(lManager, images[8]);
                            }
                            unitEndTurned();
                        }
                    }
                } else {
                    if (!isMoved) {
                        if (getUnit(cursorSpr.getX_(), cursorSpr.getY_()) == null) {
                            try {
                                resetMovingCells();
                                openList.clear();
                                closedList.clear();
                                path = move(cursorSpr.getX_(), cursorSpr.getY_());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (selectedUnit.move(this, path)) {
                            path = null;
                            selectedSpr.setVisible(false);
                            movingLayer.setVisible(false);
                            isMoved = true;
                        }
                    } else {
                        if (!isAttacking) {
                            switch (movedSpr.getFrame()) {
                                case 0:
                                    isAttacking = true;
                                    createAttackingLayer(selectedUnit);
                                    movedSpr.setVisible(false);
                                    break;

                                case 1:
                                    unitEndTurned();
                                default:
                                    break;
                            }
                        } else {
                            Unit attacked = getAIUnit(cursorSpr.getX_(), cursorSpr.getY_());
                            if (attacked != null) {
                                selectedUnit.getAttackType().attack(selectedUnit, attacked);
                                selectedUnit.getAttackType().start();
                                if (attacked.getHealth() <= 0) {
                                    attacked.isDead(lManager, images[8]);
                                }
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
        cursorSpr.move(selectedUnit.getX(), selectedUnit.getY());
        selectedUnit.endTurn(lManager);
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

    public int moveAttackCell(int dir) {
        switch (dir) {
            case 0:
                for (int i = 0; i < attackCells.length; i++) {
                    if (attackCells[i] != null && selectedUnit.getX() < attackCells[i].getX() * 24 && selectedUnit.getY() == attackCells[i].getY() * 24) {
                        return i;
                    }
                }
                return -1;
            case 1:
                for (int i = 0; i < attackCells.length; i++) {
                    if (attackCells[i] != null && selectedUnit.getX() > attackCells[i].getX() * 24 && selectedUnit.getY() == attackCells[i].getY() * 24) {
                        return i;
                    }
                }
                return -1;
            case 2:
                for (int i = 0; i < attackCells.length; i++) {
                    if (attackCells[i] != null && selectedUnit.getX() == attackCells[i].getX() * 24 && selectedUnit.getY() > attackCells[i].getY() * 24) {
                        return i;
                    }
                }
                return -1;
            case 3:
                for (int i = 0; i < attackCells.length; i++) {
                    if (attackCells[i] != null && selectedUnit.getX() == attackCells[i].getX() * 24 && selectedUnit.getY() < attackCells[i].getY() * 24) {
                        return i;
                    }
                }
                return -1;
            default:
                return -1;
        }
    }

    public void setActiveView(int x, int y) {
        this.x = (x < 96) ? 0 : (x > 456) ? 456 - 96 : x - 96;
        this.y = (y < 120) ? 0 : (y > 192) ? 192 - 120 : y - 120;
    }

    public void moveActiveView(int action) {
        switch (action) {
            case 0:
                if (y > 0 && cursorSpr.getY_() <= 208) {
                    y -= 24;
                }
                break;
            case 1:
                if (x > 0 && cursorSpr.getX_() <= 456) {
                    x -= 24;
                }
                break;
            case 2:
                if (y < (312 - this.getWidth()) && cursorSpr.getY_() >= 120) {
                    y += 24;
                }
                break;
            case 3:
                if (x < (600 - this.getWidth()) && cursorSpr.getX_() >= 96) {
                    x += 24;
                }
                break;
            default:
                break;
        }
    }

    public void drawLayer() {
        Graphics g = this.getGraphics();
        lManager.setViewWindow(x, y, this.getWidth(), this.getHeight() - 3);
        lManager.paint(g, 0, 0);
        g.setColor(0xFFFF00);
        Font font = Font.getFont(g.getFont().getFace(), Font.STYLE_BOLD, g.getFont().getSize());
        g.setFont(font);
        g.drawString("" + statusStr, 0, 0, Graphics.TOP | Graphics.LEFT);
        flushGraphics();
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            if (game.checkGameEnd()) {
                System.out.println("Finish");
                soundPlayer.stop();
                break;
            }
            animationUnits();
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
            if (pl_units[i] != null && pl_units[i].getX() == x && pl_units[i].getY() == y && pl_units[i].getHealth() > 0) {
                return pl_units[i];
            }
        }
        return null;
    }

    public Unit getAIUnit(int x, int y) {
        for (int i = 0; i < ai_units.length; i++) {
            if (ai_units[i] != null && ai_units[i].getX() == x && ai_units[i].getY() == y && ai_units[i].getHealth() > 0) {
                return ai_units[i];
            }
        }
        return null;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdExit) {
            soundPlayer.close();
            mainMidlet.notifyDestroyed();
        } else if (c == cmdMenu) {

            soundPlayer.stop();
            MainForm subForm = new MainForm(mainMidlet, ((GameMIDlet) mainMidlet).getmDisplay(), "Options", Choice.IMPLICIT, new String[]{
                        "Resume", "New Game", "Save Game", "Load Game", "End Game"}, null);
            subForm.setGame(this);
            ((GameMIDlet) mainMidlet).subForm = subForm;
            ((GameMIDlet) mainMidlet).getmDisplay().setCurrent(subForm);
        }
    }
}
