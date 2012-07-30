package Model;

import Unit.AIUnit;
import Unit.Unit;
import View.RPGMap;

public class GameHandler implements Runnable {

    private boolean playerTurn, isWon;
    private Unit[] aiUnits, plUnits;
    private int highscore, numTurn;
    private RPGMap map;

    public GameHandler(Unit[] aiUnits, Unit[] plUnits, RPGMap map) {
        this.aiUnits = aiUnits;
        this.plUnits = plUnits;
        highscore = 0; // 1 hour
        numTurn = 0;
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
        if (plUnits[0] == null && plUnits[0].getHealth() <= 0) {
            isWon = false;
            return true;
        }
        if (aiUnits[20] == null && aiUnits[20].getHealth() <= 0) {
            isWon = true;
            return true;
        }
        return false;
    }

    public void newTurnAllUnits() {
        for (int i = 0; i < aiUnits.length; i++) {
            if (aiUnits[i] != null) {
                aiUnits[i].newTurn(map.lManager);
            }
        }
        for (int i = 0; i < plUnits.length; i++) {
            if (plUnits[i] != null) {
                plUnits[i].newTurn(map.lManager);
            }
        }
    }

    public void PLTurn() {
        map.cursorSpr.setXY(plUnits[0].getX(), plUnits[0].getY());
        map.setActiveView(plUnits[0].getX(), plUnits[0].getY());
        map.cursorSpr.setVisible(true);
        newTurnAllUnits();
        numTurn++;
    }

    public void AITurn() {
        map.cursorSpr.setVisible(false);
        newTurnAllUnits();
        for (int i = 0; i < aiUnits.length; i++) {
            if (aiUnits[i] != null && aiUnits[i].getHealth() > 0) {
                synchronized (this) {
                    map.setActiveView(aiUnits[i].getX(), aiUnits[i].getY());
                    ((AIUnit) aiUnits[i]).live(map, this);
                    //aiUnits[i].endTurn(map.lManager);
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
        while (!checkGameEnd()) {
            synchronized (this) {
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
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}
