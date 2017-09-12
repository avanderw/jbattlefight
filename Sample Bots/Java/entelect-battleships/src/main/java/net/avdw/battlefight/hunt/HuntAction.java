package net.avdw.battlefight.hunt;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;

public class HuntAction extends Action {
    
    public HuntAction(Type type,Point point) {
        super();
        filename = "command.txt";
        this.type = type;
        this.point = point;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", type.ordinal(), point.x, point.y);
    }
}
