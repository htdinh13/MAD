package Model;

import Unit.AIUnit;
import Unit.Unit;
import View.GameMIDlet;
import View.HighscoreForm;
import View.RPGMap;
import javax.microedition.lcdui.Form;

public class GameHandler implements Runnable {

    private boolean playerTurn, isWon;
    private Unit[] aiUnits, plUnits;
    private int highscore, numTurn;
    private RPGMap map;

    public GameHandler(Unit[] aiUnits, Unit[] plUnits, RPGMap map) {
        this.aiUnits = aiUnits;
        this.plUnits = plUnits;
        highscore = 100; // 1 hour
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
        if (plUnits[0] == null || plUnits[0].getHealth() <= 0) {
            isWon = false;
            return true;
        }
        if (aiUnits[20] == null || aiUnits[20].getHealth() <= 0) {
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
    public Object lock;

    public void AITurn() {
        lock = new Object();
        map.cursorSpr.setVisible(false);
        newTurnAllUnits();
        Thread ts[] = new Thread[aiUnits.length];
        for (int i = 0; i < aiUnits.length; i++) {
            if (aiUnits[i] != null && aiUnits[i].getHealth() > 0) {
                try {
                    ((AIUnit) aiUnits[i]).setMap(map);
                    Thread t = new Thread(((AIUnit) aiUnits[i]));
                    ts[i] = t;
                    t.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        for (int i = 0; i < ts.length; i++) {
            if (ts[i] != null) {
                synchronized (this) {
                    synchronized (map.lManager) {
                        ts[i].start();
                        
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        playerTurn = true;
    }

    public void checkRecoverHealth(Unit[] units) {
        for (int i = 0; i < units.length; i++) {
            int cell = map.backgroundLayer.getCell(units[i].getX() / 24, units[i].getY() / 24);
            if (cell == 6 || cell == 7) {
                int h = units[i].getHealth();
                units[i].setHealth((h + 10 > units[i].getMaxHealth()) ? units[i].getMaxHealth() : h + 10);
            }
        }
    }

    public void run() {
        while (!checkGameEnd()) {
            if (playerTurn) {
                checkRecoverHealth(plUnits);
                this.PLTurn();
                while (!checkAllUnitEndTurn(plUnits)) {
                }
                playerTurn = false;
            } else {
                checkRecoverHealth(aiUnits);
                this.AITurn();
            }
        }
        Form highscoreForm = ((GameMIDlet) map.mainMidlet).getMainForm().highscoreForm;
        if (highscoreForm == null) {
            highscoreForm = new HighscoreForm(((GameMIDlet) map.mainMidlet).getmDisplay(), map, highscore);
        }
        ((GameMIDlet) map.mainMidlet).getmDisplay().setCurrent(highscoreForm);
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
}
