package View;

import Attack.Attackable;
import Attack.CavalryAttack;
import Attack.KnightAttack;
import Attack.RangedAttack;
import Model.DataRecord;
import Unit.Unit;
import gov.nist.core.StringTokenizer;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class MainForm extends List implements CommandListener {

    Command cmdExit;
    Command cmdOK;
    MIDlet mainMidlet;
    Display display;
    int highscore, c;
    String[] stringElements;
    RPGMap map;
    DataRecord dataRecord;
    public Form highscoreForm;

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
        dataRecord = new DataRecord();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdExit) {
            // map.soundPlayer.stop();
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
                    map = new RPGMap(false, mainMidlet);
                    display.setCurrent(map);
                    map.start();
                    map.game.start();
                    highscore = map.game.getHighscore();
                    break;
                case 1:
                    loadGame();
                    break;
                case 2:
                    if (highscoreForm == null) {
                        highscoreForm = new HighscoreForm(display, this, highscore);
                    } else {
                        ((HighscoreForm) highscoreForm).setHighscore(highscore);
                    }
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
                    if (map != null) {
                        map.setActiveView(map.pl_units[0].getX(), map.pl_units[0].getY());
                        map.cursorSpr.move(map.pl_units[0].getX(), map.pl_units[0].getY());
                        display.setCurrent(map);
                        map.soundPlayer.start();
                    }
                    break;
                case 1:
                    map = new RPGMap(false, mainMidlet);
                    display.setCurrent(map);
                    map.start();
                    map.game.start();
                    highscore = map.game.getHighscore();
                    break;

                case 2:
                    dataRecord.clear();
                    dataRecord.save(1, map.game.getHighscore() + "");
                    c = 2;
                    for (int i = 0; i < map.pl_units.length; i++, c++) {
                        dataRecord.save(c, i + ":" + map.pl_units[i].toString());
                    }
                    for (int i = 0; i < map.ai_units.length; i++, c++) {
                        dataRecord.save(c, i + ":" + map.ai_units[i].toString());
                    }
                    break;
                case 3:
                    loadGame();
                    break;
                case 4:
                    display.setCurrent(((GameMIDlet) mainMidlet).mainForm);
                    break;
                default:
                    break;
            }
        }
    }

    public RPGMap getGame() {
        return map;
    }

    public void setGame(RPGMap game) {
        this.map = game;
    }

    public void loadUnit(Unit[] units, String record) {
        StringTokenizer tok = new StringTokenizer(record, ':');
        while (tok.hasMoreChars()) {
            int index = intC(tok.nextToken());
            int x = intC(tok.nextToken());
            int y = intC(tok.nextToken());
            int health = intC(tok.nextToken());
            String endStr = tok.nextToken();
            boolean endTurn = (endStr.equals("1") ? true : false);
            units[index].loadUnit(x, y, health, endTurn, map);
            break;
        }
    }

    public void loadGame() {
        int size = dataRecord.size();
        if (size > 0) {
            if (map == null) {
                map = new RPGMap(false, mainMidlet);
                display.setCurrent(map);
                map.start();
                map.game.start();
            } else {
                display.setCurrent(map);
            }
            for (int i = 1; i <= size; i++) {
                String record = dataRecord.load(i);
                if (i == 1) {
                    map.game.setHighscore(Integer.parseInt(record));
                    highscore = map.game.getHighscore();
                } else if (i <= 6) {
                    loadUnit(map.pl_units, record);
                } else {
                    loadUnit(map.ai_units, record);
                }
            }
            map.setActiveView(map.pl_units[0].getX(), map.pl_units[0].getY());
            map.cursorSpr.move(map.pl_units[0].getX(), map.pl_units[0].getY());
        } else {
            System.out.println("NO SAVE DATA");
        }
    }

    public int intC(String token) {
        return (int) Integer.parseInt(token.substring(0, token.length() - 1));
    }
}
