package Model;

import Unit.AIUnit;
import Unit.Unit;
import View.RPGMap;

public class GameHandler implements Runnable {

    private boolean playerTurn;
    private Unit[] aiUnits, plUnits;
    private int highscore;
    private RPGMap map;

    public GameHandler(Unit[] aiUnits, Unit[] plUnits, RPGMap map) {
        this.aiUnits = aiUnits;
        this.plUnits = plUnits;
        highscore = 3600; // 1 hour
        this.map = map;
        playerTurn = true;
    }

    public boolean checkAllUnitEndTurn(Unit[] units) {
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null && aiUnits[i].getHealth() > 0 && !units[i].getEndTurn()) {
                return false;
            }
        }
        return true;
    }

    public void endTurnAllUnit(Unit[] units) {
        for (int i = 0; i < units.length; i++) {
            if (aiUnits[i] != null && aiUnits[i].getHealth() > 0) {
                units[i].endTurn(map.lManager);
            }
        }
    }

    public void increaseHighscore() {
        highscore += 100;
    }

    public int getHighscore() {
        return highscore;
    }

    public boolean checkGameEnd() {
        for (int i = 0; i < aiUnits.length; i++) {
            if (aiUnits[i] != null && aiUnits[i].getHealth() > 0) {
                return false;
            }
        }
        return true;
    }

    public void newTurnAllUnits() {
        for (int i = 0; i < aiUnits.length; i++) {
            if (aiUnits[i] != null && aiUnits[i].getHealth() > 0) {
                aiUnits[i].newTurn(map.lManager);
            }
        }
        for (int i = 0; i < plUnits.length; i++) {
            if (plUnits[i] != null && plUnits[i].getHealth() >0) {
                plUnits[i].newTurn(map.lManager);
            }
        }
    }

    public void PLTurn() {
        map.cursorSpr.setXY(plUnits[0].getX(), plUnits[0].getY());
        map.setActiveView(plUnits[0].getX(), plUnits[0].getY());
        map.cursorSpr.setVisible(true);
        newTurnAllUnits();
    }

    public void AITurn() {
        map.cursorSpr.setVisible(false);        
        newTurnAllUnits();
        for (int i = 0; i < aiUnits.length; i++) {
            if (aiUnits[i] != null && aiUnits[i].getHealth() > 0) {
                synchronized (this) {
                    map.setActiveView(aiUnits[i].getX(), aiUnits[i].getY());
                    ((AIUnit) aiUnits[i]).live(map, this);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        playerTurn = true;
    }

    public void run() {
        while (true) {
            if (playerTurn) {
                this.PLTurn();
                while (!checkAllUnitEndTurn(plUnits)) {
                }
                playerTurn = false;
            } else {
                this.AITurn();
            }
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}
