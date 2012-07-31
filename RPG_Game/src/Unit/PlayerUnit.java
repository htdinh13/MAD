package Unit;

import Algorithm.LinkedList;
import Attack.Attackable;
import View.RPGMap;
import javax.microedition.lcdui.Image;

public class PlayerUnit extends UnitAbstract {

    private int preX, preY;

    public PlayerUnit(int colnum, int rownum, Image img, Image imgEnd, int moveSpace, Attackable attackType) {
        super(colnum, rownum, img, imgEnd, moveSpace, attackType);
        this.preX = super.getX();
        this.preY = super.getY();
    }

    public PlayerUnit(int x, int y, Image img, Image imgEnd, int moveSpace, Attackable attackType, int health, int attack, int defence) {
        super(x, y, img, imgEnd, moveSpace, attackType, health, attack, defence);
        this.preX = super.getX();
        this.preY = super.getY();
    }

    public void move(int x, int y) {
        this.preX = super.getX();
        this.preY = super.getY();
        super.setX(x);
        super.setY(y);
        (super.getSprite()).setPosition(x, y);
    }

    public boolean move(final RPGMap map, final LinkedList path) {
        this.preX = super.getX();
        this.preY = super.getY();
        return super.move(map, path);
    }

    public void unmove() {
        super.setX(preX);
        super.setY(preY);
        (super.getSprite()).setPosition(super.getX(), super.getY());
    }
}
