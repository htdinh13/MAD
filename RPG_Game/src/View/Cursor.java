package View;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

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
        if ((action & RPGMap.RIGHT_PRESSED) != 0 && x_ < (600 - 24)) {
            x_ += 24;
        }
        if ((action & RPGMap.LEFT_PRESSED) != 0 && x_ > 0) {
            x_ -= 24;
        }
        if ((action & RPGMap.UP_PRESSED) != 0 && y_ > 0) {
            y_ -= 24;
        }
        if ((action & RPGMap.DOWN_PRESSED) != 0 && y_ < (312 + 24)) {
            y_ += 24;
        }
        this.setPosition(x_, y_);
    }

    public void move(int x, int y) {
        this.x_ = x;
        this.y_ = y;
        this.setPosition(x_, y_);
    }

    public void setXY(int x, int y) {
        this.x_ = x;
        this.y_ = y;
    }
}
