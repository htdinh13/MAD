/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.MessageConnection;

/**
 * @author Sunny
 */
public class W6BinarySMSDemo extends MIDlet implements CommandListener, Runnable {

    MessageConnection conn;
    private Form fr = new Form("Send SMS");
    private TextField dest = new TextField("Message", "", 16, TextField.ANY);
    private Command exit = new Command("Exit", Command.EXIT, 0);
    private Command send = new Command("Send", Command.OK, 1);
    private Command receive = new Command("Receive", Command.OK, 2);
    private Command stop = new Command("Stop", Command.OK, 3);
    private StringItem label = new StringItem("Status:", "");
    static StringItem receiveSMS = new StringItem("Received SMS:", "None");
    static boolean quit = false;

    public void startApp() {
        Display mDisplay = Display.getDisplay(this);
        mDisplay.setCurrent(fr);
        fr.append(dest);
        fr.append(label);
        fr.append(receiveSMS);

        fr.addCommand(send);
        fr.addCommand(exit);
        fr.addCommand(receive);
        fr.addCommand(stop);

        fr.setCommandListener(this);

    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        Thread t = null;
        Thread u = null;
        if (c.getLabel().equals("Send")) {

            t = new Thread(this);

            t.start();
        } else {
            if (c.getLabel().equals("Receive")) {
                receiveSMS.setText("Start receiving ...");
                u = new Thread(new BinaryMessageReceiver());
                u.start();
            } else {
                if (c.getLabel().equals("Stop")) {
                    quit = true;
                } else {
                    notifyDestroyed();
                }
            }
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
            
            byte[] b = dest.getString().getBytes();
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
}

class BinaryMessageReceiver implements Runnable {

    private String port = "1234";
    private String protocol = "sms://" + ":" + port;
    byte[] message ;
    private BinaryMessage txt;
    private MessageConnection conn;

    public void run() {

        try {
            conn = (MessageConnection) Connector.open(protocol);
            while (!W6BinarySMSDemo.quit) {
                txt = (BinaryMessage) conn.receive();
                message = txt.getPayloadData();
                W6BinarySMSDemo.receiveSMS.setText(message.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
                System.out.println("Closed connection");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void quit() {
        W6BinarySMSDemo.quit = true;
    }
}