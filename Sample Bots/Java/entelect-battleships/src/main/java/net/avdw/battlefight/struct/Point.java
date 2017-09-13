package net.avdw.battlefight.struct;

public class Point {

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x;
    public int y;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point)o;
            return p.x == this.x && p.y == this.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.x;
        hash = 17 * hash + this.y;
        return hash;
    }
    
    @Override
    public String toString() {
        return x + "," + y;
    }
}
