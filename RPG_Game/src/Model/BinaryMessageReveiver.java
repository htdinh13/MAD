package Model;

import View.GameMIDlet;
import View.HighscoreForm;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.MessageConnection;

public class BinaryMessageReveiver implements Runnable {

    private HighscoreForm highscoreForm;
    private String port;
    private String protocol;
    byte[] message;
    private BinaryMessage txt;
    private MessageConnection conn;

    public BinaryMessageReveiver(HighscoreForm highscoreForm) {
        this.highscoreForm = highscoreForm;
        port = "1234";
        protocol = "sms://" + ":" + port;
    }

    public void run() {

        try {
            conn = (MessageConnection) Connector.open(protocol);
            while (!highscoreForm.quit) {
                txt = (BinaryMessage) conn.receive();
                message = txt.getPayloadData();
                System.out.println(message.toString());
                highscoreForm.receiveSMS.setText(message.toString());
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
}