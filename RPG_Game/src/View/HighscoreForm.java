package View;

import Model.BinaryMessageReveiver;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.MessageConnection;

public class HighscoreForm extends Form implements CommandListener, Runnable {

    MessageConnection conn;
    private TextField username;
    private ChoiceGroup storeable;
    private StringItem highscore;
    private Command cmdBack, cmdSave, cmdSend, cmdReceive, cmdStop;
    private Displayable mainList;
    private Display display;
    private StringItem label;
    public StringItem receiveSMS;
    public boolean quit;

    public HighscoreForm(Display mDisplay, Displayable mainList, int highscorePoint) {
        super("Highscore");
        quit = false;
        this.mainList = mainList;
        this.display = mDisplay;
        username = new TextField("Your Name:", "", 20, TextField.ANY);
        storeable = new ChoiceGroup(null, ChoiceGroup.EXCLUSIVE);
        storeable.append("Store name and highscore to server", null);
        highscore = new StringItem("Highscore: ", "" + highscorePoint, StringItem.PLAIN);
        label = new StringItem("Status:", "");
        receiveSMS = new StringItem("Received SMS:", "None");
        cmdBack = new Command("Back", Command.BACK, 0);
        cmdSave = new Command("Save", Command.OK, 1);
        cmdSend = new Command("Send", Command.OK, 2);
        cmdReceive = new Command("Receive", Command.OK, 3);
        cmdStop = new Command("Stop", Command.OK, 4);
        this.append(username);
        this.append(storeable);
        this.append(highscore);
        this.append(label);
        this.append(receiveSMS);
        this.addCommand(cmdBack);
        this.addCommand(cmdSave);
        this.addCommand(cmdSend);
        this.addCommand(cmdReceive);
        this.addCommand(cmdStop);
        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        Thread t = null;
        Thread u = null;
        if (c == cmdBack) {
            display.setCurrent(mainList);
        } else if (c == cmdSave) {
            display.setCurrent(mainList);
        } else if (c.getLabel().equals(cmdSend.getLabel())) {
            t = new Thread(this);
            t.start();
        } else if (c.getLabel().equals(cmdReceive.getLabel())) {
            receiveSMS.setText("Start Receiving ...");
            u = new Thread(new BinaryMessageReveiver(this));
            u.start();
        } else if (c.getLabel().equals(cmdStop.getLabel())) {
            quit = true;
        }
    }

    public void run() {
        try {
            String port = "1234";
            String recipient = "+5550001";
            String protocol = "sms://" + ":" + port;
            String add = "sms://" + recipient;

            conn = (MessageConnection) Connector.open(protocol);
            System.out.println("Start sending");
            BinaryMessage msg = (BinaryMessage) conn.newMessage(MessageConnection.BINARY_MESSAGE);
            String msgStr = username.getString() + ":" + highscore.getText();
            System.out.println(msgStr);
            byte[] b = msgStr.getBytes();
            msg.setAddress(add);
            msg.setPayloadData(b);
            label.setText("Sending SMS...");
            conn.send(msg);
            label.setText("SMS Sent sucessfully...");
        } catch (IOException ioe) {
            System.out.println("IO");

        } finally {
            try {
                conn.close();
            } catch (IOException ex) {
                System.out.println("Cannot close the connection !");
            }
        }

    }

    public void setHighscore(int highscorePoint) {
        highscore.setText(highscorePoint + "");
    }
}
