package net.avdw.battlefight.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.avdw.battlefight.state.StateModel;

public class Zone {

    static public final Zone ONE = new Zone(0, 13, 4, 9, "ONE");
    static public final Zone TWO = new Zone(4, 13, 9, 9, "TWO");
    static public final Zone THREE = new Zone(9, 13, 13, 9, "THREE");
    static public final Zone FOUR = new Zone(0, 9, 4, 4, "FOUR");
    static public final Zone FIVE = new Zone(4, 9, 9, 4, "FIVE");
    static public final Zone SIX = new Zone(9, 9, 13, 4, "SIX");
    static public final Zone SEVEN = new Zone(0, 4, 4, 0, "SEVEN");
    static public final Zone EIGHT = new Zone(4, 4, 9, 0, "EIGHT");
    static public final Zone NINE = new Zone(9, 4, 13, 0, "NINE");

    public final static List<Zone> ALL_ZONES = new ArrayList();
    public final static List<Zone> CORNER_ZONES = new ArrayList();
    public final static List<Zone> ADJACENT_ZONES = new ArrayList();

    static {
        ALL_ZONES.add(ONE);
        ALL_ZONES.add(TWO);
        ALL_ZONES.add(THREE);
        ALL_ZONES.add(FOUR);
        ALL_ZONES.add(FIVE);
        ALL_ZONES.add(SIX);
        ALL_ZONES.add(SEVEN);
        ALL_ZONES.add(EIGHT);
        ALL_ZONES.add(NINE);

        CORNER_ZONES.add(ONE);
        CORNER_ZONES.add(THREE);
        CORNER_ZONES.add(SEVEN);
        CORNER_ZONES.add(NINE);

        ADJACENT_ZONES.add(TWO);
        ADJACENT_ZONES.add(FOUR);
        ADJACENT_ZONES.add(SIX);
        ADJACENT_ZONES.add(EIGHT);
    }

    public static void resetZones() {
        for (Zone zone : ALL_ZONES) {
            zone.reset();
        }
    }

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final String name;

    private final List<StateModel.ShipType> containedShips;

    private Zone(int x1, int y1, int x2, int y2, String name) {
        this.containedShips = new ArrayList();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.name = name;
    }

    public boolean containsPoint(int x, int y) {
        return (x <= x2 && x >= x1 && y <= y1 && y >= y2);
    }

    public void addShip(StateModel.ShipType type) {
        containedShips.add(type);
    }

    public int containedShipCount() {
        return containedShips.size();
    }

    public boolean containsShip(StateModel.ShipType type) {
        return containedShips.contains(type);
    }

    public Place fit(StateModel.ShipType type, boolean boundsAllowed) {
        while (true) {
            int x = ThreadLocalRandom.current().nextInt(boundsAllowed ? x1 : x1 + 1, x2);
            int y = ThreadLocalRandom.current().nextInt(boundsAllowed ? y2 : y2 + 1, y1);
            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2;
            boolean vertical = ThreadLocalRandom.current().nextBoolean();

            Direction direction;
            if (vertical) {
                direction = (y < midY) ? Direction.North : Direction.South;
            } else {
                direction = (x > midX) ? Direction.West : Direction.East;
            }

            if (boundsAllowed) {
                switch (direction) {
                    case North:
                        if (y + (type.length() - 1) <= y1) {
                            return new Place(x, y, direction);
                        }
                        break;
                    case South:
                        if (y - (type.length() - 1) >= y2) {
                            return new Place(x, y, direction);
                        }
                        break;
                    case West:
                        if (x - (type.length() - 1) >= x1) {
                            return new Place(x, y, direction);
                        }
                        break;
                    case East:
                        if (x + (type.length() - 1) <= x2) {
                            return new Place(x, y, direction);
                        }
                        break;
                }
            } else {
                switch (direction) {
                    case North:
                        if (y + (type.length() - 1) < y1) {
                            return new Place(x, y, direction);
                        }
                        break;
                    case South:
                        if (y - (type.length() - 1) > y2) {
                            return new Place(x, y, direction);
                        }
                        break;
                    case West:
                        if (x - (type.length() - 1) > x1) {
                            return new Place(x, y, direction);
                        }
                        break;
                    case East:
                        if (x + (type.length() - 1) < x2) {
                            return new Place(x, y, direction);
                        }
                        break;
                }
            }
        }
    }

    private void reset() {
        containedShips.clear();
    }

    @Override
    public String toString() {
        return String.format("zone:%s, hasShips:%s", new Object[]{name, containedShips.size()});
    }

}
