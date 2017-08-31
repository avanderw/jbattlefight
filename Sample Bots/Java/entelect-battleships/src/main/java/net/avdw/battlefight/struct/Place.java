package net.avdw.battlefight.struct;

public class Place {

    public int x;
    public int y;
    public Direction direction;

    Place(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}
