package net.avdw.battlefight.state;

import net.avdw.battlefight.struct.Direction;

public class PersistentModel {

    public Action lastAction;
    public Direction lastHeading;

    static public class Action {

        public int x;
        public int y;
        public net.avdw.battlefight.struct.Action.Type type;
    }
}
