/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Unit.AIUnit;
import Unit.Unit;
import View.RPGMap;
import javax.microedition.lcdui.game.LayerManager;

/**
 *
 * @author kem
 */
public class GameHandler implements Runnable {

    private boolean playerTurn;
    private Unit[] aiUnits, plUnits;
    private int highscore;
    private RPGMap map;

    public GameHandler(Unit[] aiUnits, Unit[] plUnits) {
        this.aiUnits = aiUnits;
        this.plUnits = plUnits;
        highscore = 3600; // 1 hour
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void changePlayerTurn() {
        playerTurn = false;
        for (int i = 0; i < plUnits.length; i++) {
            if (plUnits[i] != null && !plUnits[i].getEndTurn()) {
                playerTurn = true;
            }
        }
    }

    public boolean checkAllUnitEndTurn(Unit[] units) {
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null && !units[i].getEndTurn()) {
                return false;
            }
        }
        return true;
    }

    public void endTurnAllUnit(Unit[] units) {
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null) {
                units[i].setEndTurn(true);
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

    public boolean swithchTurnBase() {
        playerTurn = !playerTurn;
        return playerTurn;
    }

    public void AITurn() {
        if (!playerTurn) {
            for (int i = 0; i < aiUnits.length; i++) {
                ((AIUnit)aiUnits[i]).live(null, null);
            }
        }
    }

    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
