/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 *
 * @author kem
 */
public class SoundPlayer {

    Player player;

    public SoundPlayer() {
        try {
            InputStream in = getClass().getResourceAsStream("/Sounds/background.mid");
            player = Manager.createPlayer(in, "audio/midi");
            player.prefetch();
            player.setLoopCount(-1);
        } catch (IOException ex) {
        } catch (MediaException ex) {
        }
    }

    public void stop() {
        try {
            player.stop();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        player.close();
    }

    public void start() {
        try {
            player.start();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }
}
