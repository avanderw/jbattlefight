package net.avdw.battlefight.state;

import java.util.List;
import net.avdw.battlefight.struct.Direction;
import net.avdw.battlefight.struct.Point;

public class PersistentModel {

    public Action lastAction;
    public Direction lastHeading;
    public StateResolver.AiState lastState;
    public List<Point> unclearedHits;
    public List<Point> clearedHits;

    static public class Action {

        public int x;
        public int y;
        public net.avdw.battlefight.struct.Action.Type type;
    }
}
