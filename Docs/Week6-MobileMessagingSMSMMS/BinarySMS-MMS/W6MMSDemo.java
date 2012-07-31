/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessagePart;
import javax.wireless.messaging.MultipartMessage;

/**
 * @author Sunny
 */
public class W6MMSDemo extends MIDlet implements CommandListener, Runnable {

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
                u = new Thread(new MMSReceiver());
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
            MessagePart part1, part2;
            String port = "1234";
            String recipient = "+5550001";
            String protocol = "mms://" + ":" + port;
            String add = "mms://" + recipient;
            String mimeType;
            String encoding;
            String text;
            String image;

            conn = (MessageConnection) Connector.open(protocol);
            System.out.println("Start sending");
            System.out.println("1");

            MultipartMessage msg = (MultipartMessage) conn.newMessage(MessageConnection.MULTIPART_MESSAGE);


            System.out.println("2");

            //Prepare part 1
            mimeType = "text/plain";
            encoding = "UTF-8";
            text = "Hello";
            byte[] contents = text.getBytes();
            System.out.println("B4");
            part1 = new MessagePart(contents, 0, contents.length, mimeType, "id1", "contentLocation", null);
            System.out.println("After");

            //Prepare part 2
            mimeType = "image/png";
            image = "/hello.png";

            InputStream is = getClass().getResourceAsStream(image);
            contents = new byte[is.available()];
            is.read(contents);
            part2 = new MessagePart(contents, 0, contents.length, mimeType, "id2", "contentLocation", null);

            //Adding parts
            msg.addMessagePart(part1);
            msg.addMessagePart(part2);

            msg.setAddress(add);


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

class MMSReceiver implements Runnable {

    private String port = "1234";
    private String protocol = "sms://" + ":" + port;
    private String strPart1;
    MessagePart[] message;
    private MultipartMessage txt;
    private MessageConnection conn;
    MessagePart part1, part2;
    Image img;

    public void run() {

        try {
            conn = (MessageConnection) Connector.open(protocol);
            while (!W6MMSDemo.quit) {
                txt = (MultipartMessage) conn.receive();
                message = txt.getMessageParts();

                part1 = message[0];
                strPart1 = new String(part1.getContent());

                part2 = message[1];
                img = Image.createImage(part2.getContent(), 0, part2.getContent().length);

                W6MMSDemo.receiveSMS.setText(message.toString());
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
        W6MMSDemo.quit = true;
    }
}