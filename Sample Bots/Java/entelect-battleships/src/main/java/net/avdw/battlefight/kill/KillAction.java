package net.avdw.battlefight.kill;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;

public class KillAction extends Action {

    public KillAction(Point point) {
        super();
        filename = "command.txt";
        this.point = point;
        type = Type.FIRESHOT;
    }
}
