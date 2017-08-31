package net.avdw.battlefight;

public class Point {

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x;
    public int y;

    @Override
    public String toString() {
        return x + "," + y;
    }
}
