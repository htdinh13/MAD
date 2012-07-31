package View;

/**
 *
 * @author Luu Manh 13
 */
import Model.SoundPlayer;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class GameMIDlet extends MIDlet {

    private Display mDisplay;
    public MainForm mainForm, subForm;

    public GameMIDlet() {
        mDisplay = Display.getDisplay(this);
        mainForm = new MainForm(this, mDisplay, "Acay Game", Choice.IMPLICIT, new String[]{"New Game", "Load Game", "Highscores", "Quit"}, null);
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        mDisplay.setCurrent(mainForm);
    }

    public Display getmDisplay() {
        return mDisplay;
    }

    public MainForm getMainForm() {
        return mainForm;
    }
}