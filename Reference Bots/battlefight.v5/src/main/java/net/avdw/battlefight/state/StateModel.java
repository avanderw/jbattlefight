package net.avdw.battlefight.state;

import java.io.Serializable;
import java.util.ArrayList;

public class StateModel implements Serializable {

    public PlayerMap PlayerMap;
    public OpponentMap OpponentMap;
    public String GameVersion;
    public int GameLevel;
    public int Round;
    public int MapDimension;
    public int Phase;

    static public class PlayerMap implements Serializable {

        public Owner Owner;
        public ArrayList<Cell> Cells;
        public int MapWidth;
        public int MapHeight;
    }

    static public class OpponentMap implements Serializable {

        public boolean Alive;
        public int Points;
        public String Name;
        public ArrayList<OpponentShip> Ships;
        public ArrayList<OpponentCell> Cells;
    }

    static public class Owner implements Serializable {

        public int FailedFirstRoundCommands;
        public String Name;
        public ArrayList<Ship> Ships;
        public int Points;
        public int Energy;
        public boolean Killed;
        public boolean IsWinner;
        public int ShotsFired;
        public int ShotsHit;
        public int ShipsRemaining;
        public char Key;
    }

    static public class Cell implements Serializable {

        public boolean Occupied;
        public boolean Hit;
        public int X;
        public int Y;
    }

    static public class OpponentShip implements Serializable {

        public boolean Destroyed;
        public ShipType ShipType;
    }

    static public class OpponentCell implements Serializable{

        public boolean Damaged;
        public boolean Missed;
        public int X;
        public int Y;
    }

    static public class Ship  implements Serializable{

        public boolean Destroyed;
        public boolean Placed;
        public ShipType ShipType;
        public ArrayList<Weapon> Weapons;
        public ArrayList<Cell> Cells;
    }

    static public class Weapon implements Serializable {

        public WeaponType WeaponType;
        public int EnergyRequired;
    }

    static public enum WeaponType implements Serializable {
        SingleShot,
        SeekerMissle,
        DoubleShot,
        DiagonalCrossShot,
        CornerShot,
        CrossShot
    }

    static public enum ShipType implements Serializable {
        Battleship(4),
        Carrier(5),
        Cruiser(3),
        Destroyer(2),
        Submarine(3);

        private final int length;

        private ShipType(final int length) {
            this.length = length;
        }

        public int length() {
            return length;
        }
    }
}
