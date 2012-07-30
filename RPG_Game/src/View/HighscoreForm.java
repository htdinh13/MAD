package View;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Luu Manh 13
 */
public class HighscoreForm extends Form implements CommandListener {

    private TextField username;
    private ChoiceGroup storeable;
    private StringItem highscore;
    private Command cmdBack;
    private Displayable mainList;
    private Display display;

    public HighscoreForm(Display mDisplay, Displayable mainList, int highscorePoint) {
        super("Highscore");
        this.mainList = mainList;
        this.display = mDisplay;
        username = new TextField("Your Name:", "", 20, TextField.ANY);
        storeable = new ChoiceGroup(null, ChoiceGroup.EXCLUSIVE);
        storeable.append("Store name and location automatically to server", null);
        highscore = new StringItem("Highscore: ", "" + highscorePoint, StringItem.PLAIN);
        cmdBack = new Command("Back", Command.BACK, 0);
        this.append(username);
        this.append(storeable);
        this.append(highscore);
        this.addCommand(cmdBack);
        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBack) {
            display.setCurrent(mainList);
        }
    }
}
