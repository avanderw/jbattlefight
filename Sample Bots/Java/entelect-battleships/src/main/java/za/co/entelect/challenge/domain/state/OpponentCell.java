package za.co.entelect.challenge.domain.state;


import za.co.entelect.challenge.domain.command.Point;

public class OpponentCell {

    public boolean Damaged;

    public boolean Missed;

    public int X;

    public int Y;

    public Point getPoint() {

        return new Point(X, Y);
    }

    public boolean isShot() {
        return Damaged || Missed;
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s : damaged=%s", new Object[]{X, Y, Damaged});
    }
}
