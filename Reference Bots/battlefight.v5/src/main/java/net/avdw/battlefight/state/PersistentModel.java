package net.avdw.battlefight.state;

public class PersistentModel {

    public Action lastAction;

    static public class Action {

        public int x;
        public int y;
        public ActionType type;
    }

    static public enum ActionType {
        DO_NOTHING,
        FIRESHOT,
        DOUBLE_SHOT_VERTICAL,
        DOUBLE_SHOT_HORIZONTAL,
        CORNER_SHOT,
        CROSS_SHOT_DIAGONAL,
        CROSS_SHOT_HORIZONTAL,
        SEEKER_MISSILE,
        SHIELD
    }
}
