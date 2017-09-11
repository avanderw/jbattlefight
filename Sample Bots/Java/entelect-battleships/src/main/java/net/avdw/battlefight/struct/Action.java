package net.avdw.battlefight.struct;

import net.avdw.battlefight.state.StateModel.ShipType;

public class Action {

    public String filename;
    public Type type;
    public Point point;
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s", type.ordinal(), point.x, point.y);
    }

    static public enum Type {
        DO_NOTHING(0, null),
        FIRESHOT(1, null),
        DOUBLE_SHOT_VERTICAL(32, ShipType.Destroyer),
        DOUBLE_SHOT_HORIZONTAL(32, ShipType.Destroyer),
        CORNER_SHOT(40, ShipType.Carrier),
        CROSS_SHOT_DIAGONAL(48, ShipType.Battleship),
        CROSS_SHOT_HORIZONTAL(56, ShipType.Cruiser),
        SEEKER_MISSILE(48, ShipType.Submarine),
        SHIELD(0, null);
        
        public int energy;
        public ShipType ship;
        
        Type(int energy, ShipType ship) {
            this.energy = energy;
            this.ship = ship;
        }
    }
}
