package net.avdw.battlefight.struct;

public class Action {

    public String filename;
    public Type type;
    public Point point;
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s", type.ordinal(), point.x, point.y);
    }

    public enum Type {
        DO_NOTHING,
        FIRESHOT,
        DOUBLE_SHOT_VERTICAL,
        DOUBLE_SHOT_HORIZONTAL,
        CORNER_SHOT,
        CROSS_SHOT_DIAGONAL,
        CROSS_SHOT_HORIZONTAL,
        SEEKER_MISSILE,
        SHIELD;
    }
}
