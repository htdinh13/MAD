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
public class MainForm extends List implements CommandListener {

    Command cmdExit;
    Command cmdOK;
    MIDlet mainMidlet;
    Display display;
    int highscore;
    String[] stringElements;
    RPGMap game;

    public MainForm(MIDlet mainMidlet, Display display, String title, int listType, String[] stringElements, Image[] imageElements) {
        super(title, listType, stringElements, imageElements);
        this.mainMidlet = mainMidlet;
        this.display = display;
        cmdOK = new Command("OK", Command.EXIT, 0);
        this.addCommand(cmdOK);
        cmdExit = new Command("Exit", Command.OK, 0);
        this.addCommand(cmdExit);
        this.setCommandListener(this);
        this.highscore = 0;
        this.stringElements = stringElements;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdExit) {
            mainMidlet.notifyDestroyed();
        } else if (c == cmdOK) {
            onSelected();
        } else if (d == this) {
            onSelected();
        }
    }

    public void onSelected() {
        if (stringElements.length == 4) {
            switch (this.getSelectedIndex()) {
                case 0:
                    game = new RPGMap(false, mainMidlet);
                    display.setCurrent(game);
                    game.start();
                    game.game.start();
                    highscore = game.game.getHighscore();
                    break;
                case 1:
                    System.out.println("Load game");
                    break;
                case 2:
                    Form highscoreForm = new HighscoreForm(display, this, highscore);
                    display.setCurrent(highscoreForm);
                    break;
                case 3:
                    mainMidlet.notifyDestroyed();
                    break;
                default:
                    break;
            }
        } else {
            switch (this.getSelectedIndex()) {
                case 0:
                    if (game != null) {
                        game.setActiveView(game.pl_units[0].getX(), game.pl_units[0].getY());
                        game.cursorSpr.move(game.pl_units[0].getX(), game.pl_units[0].getY());
                        display.setCurrent(game);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public RPGMap getGame() {
        return game;
    }

    public void setGame(RPGMap game) {
        this.game = game;
    }
}
