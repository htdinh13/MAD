package Unit;

import Algorithm.AStar;
import Algorithm.Cell;
import Algorithm.LinkedList;
import Algorithm.Node;
import Attack.Attackable;
import Attack.RangedAttack;
import Model.GameHandler;
import View.RPGMap;
import javax.microedition.lcdui.Image;

/**
 *
 * @author HOANG TRUONG DINH
 */
public class AIUnit extends UnitAbstract implements Runnable{

    public AIUnit(int colnum, int rownum, Image img, Image imgEnd, int moveSpace, Attackable attackType) {
        super(colnum, rownum, img, imgEnd, moveSpace, attackType);
    }

    public AIUnit(int x, int y, Image img, Image imgEnd, int moveSpace, Attackable attackType, int health, int attack, int defence) {
        super(x, y, img, imgEnd, moveSpace, attackType, health, attack, defence);
    }
    
    public RPGMap map;

    public void setMap(RPGMap map) {
        this.map = map;
    }  
    
    public boolean live(RPGMap map, GameHandler game) {
        map.setActiveView(this.getX(), this.getY());
        if (this.getHealth() < 10) {
        } else {
            Unit[] nearUnit = getNearUnit(map);
            if (!isNearUnitEmpty(nearUnit)) {
                sortUnitsByHealth(nearUnit);
                for (int i = 0; i < nearUnit.length && this.getEndTurn() == false; i++) {
                    LinkedList goalNodes = createGoalNodes(nearUnit[i], map);
                    if (!goalNodes.isEmpty()) {
                        Node nodes[][] = map.createAIMovingNodes(this.getX() / 24, this.getY() / 24, this.getMoveSpace());
                        LinkedList open = new LinkedList(), close = new LinkedList();
                        AStar astar = new AStar(open, close);
                        Node start = getStartNode(nodes);
                        Node goal = goalNodes.head;
                        boolean isDone = false;
                        do {
                            LinkedList path = astar.findPath(start, goal);

                            if (path != null && !path.isEmpty()) {
                                this.move(map, path, game);
                                getAttackType().attack(this, nearUnit[i]);
                                getAttackType().start();
                                if (nearUnit[i].getHealth() <= 0) {
                                    nearUnit[i].isDead(map.lManager, map.images[8]);
                                }
                                this.setEndTurn(true);
                                isDone = true;
                            }
                            goal = goal.next;
                        } while (!isDone);

                    }
                }
            } else {
                this.endTurn(map.lManager);
            }
        }
        return false;
    }

    public boolean move(final RPGMap map, final LinkedList path, final GameHandler game) {
        if (path != null) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    synchronized (map.lManager) {
                        Node n = path.head;
                        while (n != null) {
                            x = n.getX() * 24;
                            y = n.getY() * 24;
                            map.setActiveView(x, y);
                            getSprite().setPosition(x, y);
                            n = n.next;
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
            t.start();
            return true;
        }
        return false;
    }

    private Node getStartNode(Node[][] nodes) {
        int gridRows = this.getMoveSpace() * 2 + 1;
        int gridCols = this.getMoveSpace() * 2 + 1;
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                if (nodes[i][j].data.getX() == this.getX() / 24 && nodes[i][j].data.getY() == this.getY() / 24) {
                    return nodes[i][j];
                }
            }
        }
        return null;
    }

    private Unit[] getNearUnit(RPGMap map) {
        Unit[] nearUnit = new Unit[map.pl_units.length];
        int range = (this.getAttackType() instanceof RangedAttack) ? this.getMoveSpace() + 2 : this.getMoveSpace() + 1;
        int c = 0;
        for (int i = 0; i < map.pl_units.length; i++) {
            if (map.pl_units[i] != null && map.pl_units[i].getHealth() > 0) {
                Unit u = map.pl_units[i];
                int x = u.getX();
                int y = u.getY();
                if (Math.abs(x / 24 - this.getX() / 24) + Math.abs((y / 24 - this.getY() / 24)) <= range) {
                    nearUnit[c] = u;
                    c++;
                }
            }
        }
        return nearUnit;
    }

    private boolean isNearUnitEmpty(Unit[] units) {
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null) {
                return false;
            }
        }
        return true;
    }

    private LinkedList createGoalNodes(Unit unit, RPGMap map) {

        LinkedList nodes = new LinkedList();
        if (unit != null) {
            int x = unit.getX() / 24;
            int y = unit.getY() / 24;
            int r = (this.getAttackType() instanceof RangedAttack) ? 2 : 1;
            for (int g = -1 * r; g <= r; g++) {
                for (int h = -1 * r; h <= r; h++) {
                    if (r == 2) {
                        if ((g + h) % 2 == 0 && g % 2 == 0 && h % 2 == 0 && ((g > -2 && g < 2) || (h > -2 && h < 2))) {
                            addNode(nodes, map, x + g, y + h);
                        }
                    } else if (Math.abs((g + h) % 2) == 1) {
                        addNode(nodes, map, x + g, y + h);
                    }
                }
            }
        }
        return nodes;
    }

    private void addNode(LinkedList nodes, RPGMap map, int x, int y) {
        if (map.getUnit(x * 24, y * 24) == null) {
            if (map.backgroundLayer.getCell(x, y) < 10) {
                nodes.add(new Node(new Cell(x, y)));
            }
        }
    }

    private void sortUnitsByHealth(Unit[] units) {
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

    public void run() {
        this.live(map, map.game);
    }
}
