/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game_Demo;

import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author tinanguyen
 */
public class GameDemo extends MIDlet {
    
    private BasicGameCanvas cv;
    private Display mDisplay;
    
    public GameDemo() throws IOException {
        cv = new BasicGameCanvas(false);
    }
    
    public void startApp() {
        mDisplay = Display.getDisplay(this);
        mDisplay.setCurrent(cv);
        try {
            cv.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
