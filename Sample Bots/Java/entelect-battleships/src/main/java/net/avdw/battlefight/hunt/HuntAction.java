package net.avdw.battlefight.hunt;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;

public class HuntAction extends Action {

    private final Point point;

    public HuntAction(Point point) {
        super();
        filename = "command.txt";
        this.point = point;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", Type.FIRESHOT.ordinal(), point.x, point.y);
    }
}
