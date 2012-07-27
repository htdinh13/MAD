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