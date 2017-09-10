package net.avdw.battlefight.struct;

public class Action {

    public String filename;
    public Type type;
    public Point point;
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s", type.ordinal(), point.x, point.y);
    }

    static public enum Type {
        DO_NOTHING(0),
        FIRESHOT(1),
        DOUBLE_SHOT_VERTICAL(32),
        DOUBLE_SHOT_HORIZONTAL(32),
        CORNER_SHOT(40),
        CROSS_SHOT_DIAGONAL(48),
        CROSS_SHOT_HORIZONTAL(56),
        SEEKER_MISSILE(10),
        SHIELD(0);
        
        public int energy;
        
        Type(int energy) {
            this.energy = energy;
        }
    }
}
