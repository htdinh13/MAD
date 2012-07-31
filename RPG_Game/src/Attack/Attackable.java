package Attack;

import Unit.Unit;
import javax.microedition.lcdui.game.Sprite;

public interface Attackable {

    public void attack(Unit attacker, Unit attacked);

    public void start();

    public Sprite getAttackSpr();

    public void setAttackSpr(Sprite sprite);
}
