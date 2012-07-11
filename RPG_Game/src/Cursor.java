/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Luu Manh 13
 */
public class Cursor extends Sprite {

    private int x_, y_;

    public Cursor(Sprite s) {
        super(s);
    }

    public Cursor(Image image, int frameWidth, int frameHeight) {
        super(image, frameWidth, frameHeight);
    }

    public Cursor(Image image) {
        super(image);
    }

    public Cursor(Image image, int frameWidth, int frameHeight, int x, int y) {

        super(image, frameWidth, frameHeight);
        this.x_ = x;
        this.y_ = y;
    }

    public int getX_() {
        return x_;
    }

    public int getY_() {
        return y_;
    }

    public void move(int action) {
        switch (action) {
            case GameCanvasTiledLayerDemo.RIGHT:
                if (x_ < (600 - 24)) {
                    x_ += 24;
                }
                break;
            case GameCanvasTiledLayerDemo.LEFT:
                if (x_ > 0) {
                    x_ -= 24;
                }
                break;
            case GameCanvasTiledLayerDemo.UP:
                if (y_ > 0) {
                    y_ -= 24;
                }
                break;
            case GameCanvasTiledLayerDemo.DOWN:
                if (y_ < (312 + 24)) {
                    y_ += 24;
                }
                break;
            default:
                break;
        }
//        if ((action & GameCanvasTiledLayerDemo.RIGHT_PRESSED) != 0 && x_ < (600 - 24)) {
//            x_ += 24;
//        }
//        if ((action & GameCanvasTiledLayerDemo.LEFT_PRESSED) != 0 && x_ > 0) {
//            x_ -= 24;
//        }
//        if ((action & GameCanvasTiledLayerDemo.UP_PRESSED) != 0 && y_ > 0) {
//            y_ -= 24;
//        }
//        if ((action & GameCanvasTiledLayerDemo.DOWN_PRESSED) != 0 && y_ < (312 + 24)) {
//            y_ += 24;
//        }
        this.setPosition(x_, y_);
    }
}
