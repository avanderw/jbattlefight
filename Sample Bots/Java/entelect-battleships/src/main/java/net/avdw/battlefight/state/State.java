package net.avdw.battlefight.state;

import java.util.ArrayList;
import za.co.entelect.challenge.domain.command.ship.ShipType;

public class State {

    public PlayerMap PlayerMap;
    public OpponentMap OpponentMap;
    public String GameVersion;
    public int GameLevel;
    public int Round;
    public int MapDimension;
    public int Phase;

    public class PlayerMap {

        public Owner Owner;
        public ArrayList<Cell> Cells;
        public int MapWidth;
        public int MapHeight;
    }

    public class OpponentMap {

        public boolean Alive;
        public int Points;
        public String Name;
        public ArrayList<OpponentShip> Ships;
        public ArrayList<OpponentCell> Cells;
    }

    public class Owner {

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

    public class Cell {

        public boolean Occupied;
        public boolean Hit;
        public int X;
        public int Y;
    }

    public class OpponentShip {

        public boolean Destroyed;
        public ShipType ShipType;
    }

    public class OpponentCell {

        public boolean Damaged;
        public boolean Missed;
        public int X;
        public int Y;
    }

    public class Ship {

        public boolean Destroyed;
        public boolean Placed;
        public ShipType ShipType;
        public ArrayList<Weapon> Weapons;
        public ArrayList<Cell> Cells;
    }

    public class Weapon {

        public WeaponType WeaponType;
    }

    public enum WeaponType {
        SingleShot, SeekerMissle, DoubleShot, DiagonalCrossShot, CornerShot, CrossShot
    }
}
