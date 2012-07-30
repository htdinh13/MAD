
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
public class MainForm extends List implements CommandListener {

    Command cmdExit;
    MIDlet mainMidlet;
    Display display;

    public MainForm(MIDlet mainMidlet, Display display, String title, int listType, String[] stringElements, Image[] imageElements) {
        super(title, listType, stringElements, imageElements);
        this.mainMidlet = mainMidlet;
        this.display = display;
        cmdExit = new Command("Exit", Command.EXIT, 0);
        this.addCommand(cmdExit);
        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdExit) {
            mainMidlet.notifyDestroyed();
        } else if (d == this) {
            switch (this.getSelectedIndex()) {
                case 0:
                    System.out.println("New");
                    break;
                case 1:
                    System.out.println("Highscore");
                    break;
                case 2:
                    Form settingForm = new SettingForm(display, this);

                    break;
                default:
                    break;
            }
        }
    }
}
