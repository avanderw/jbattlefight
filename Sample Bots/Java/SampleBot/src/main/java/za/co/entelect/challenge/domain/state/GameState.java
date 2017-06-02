package za.co.entelect.challenge.domain.state;

import za.co.entelect.challenge.Placement;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;

public class GameState {

    public PlayerMap PlayerMap;

    public OpponentMap OpponentMap;

    public String GameVersion;

    public int GameLevel;

    public int Round;

    public int MapDimension;

    public int Phase;

    private boolean[][] placedMap;

    public void place(Placement placement) {
        System.out.println("placing " + placement);
        if (placedMap == null) {
            placedMap = new boolean[MapDimension][MapDimension];
        }

        switch (placement.direction) {
            case North:
                for (int i = 0; i < placement.type.length(); i++) {
                    placedMap[placement.point.getY() + i][placement.point.getX()] = true;
                }
                break;
            case East:
                for (int i = 0; i < placement.type.length(); i++) {
                    placedMap[placement.point.getY()][placement.point.getX() + i] = true;
                }
                break;
            case South:
                for (int i = 0; i < placement.type.length(); i++) {
                    placedMap[placement.point.getY() - i][placement.point.getX()] = true;
                }
                break;
            case West:
                for (int i = 0; i < placement.type.length(); i++) {
                    placedMap[placement.point.getY()][placement.point.getX() - i] = true;
                }
                break;
        }
    }

    public boolean canPlace(Placement placement) {
        if (placedMap == null) {
            placedMap = new boolean[MapDimension][MapDimension];
        }

        if (placement.direction == null || placement.point == null || placement.type == null) {
            return false;
        }

        boolean overlap = false;
        switch (placement.direction) {
            case North:
                for (int i = 0; i < placement.type.length(); i++) {
                    overlap = overlap || placement.point.getY() + i >= MapDimension || placedMap[placement.point.getY() + i][placement.point.getX()];
                }
                break;
            case East:
                for (int i = 0; i < placement.type.length(); i++) {
                    overlap = overlap || placement.point.getX() + i >= MapDimension || placedMap[placement.point.getY()][placement.point.getX() + i];
                }
                break;
            case South:
                for (int i = 0; i < placement.type.length(); i++) {
                    overlap = overlap || placement.point.getY() - i < 0 || placedMap[placement.point.getY() - i][placement.point.getX()];
                }
                break;
            case West:
                for (int i = 0; i < placement.type.length(); i++) {
                    overlap = overlap || placement.point.getX() - i < 0 || placedMap[placement.point.getY()][placement.point.getX() - i];
                }
                break;
        }
        return !overlap;
    }

    public String debugPlace() {
        String buffer = "";
        for (int i = 0; i < placedMap.length; i++) {
            for (int j = 0; j < placedMap.length; j++) {
                buffer += placedMap[i][j] ? "1" : "0";
            }
            buffer += "\n";
        }
        return buffer;
    }

    public static void main(String[] args) {
        GameState state = new GameState();
        state.MapDimension = 10;
        state.place(new Placement(state).fixed(ShipType.Carrier, new Point(0, 3), Direction.North));
        System.out.println(state.canPlace(new Placement(state).fixed(ShipType.Submarine, new Point(0, 3), Direction.East)));
    }

    public static void testOverlap() {
        GameState state = new GameState();
        state.MapDimension = 10;
        state.place(new Placement(state).fixed(ShipType.Carrier, new Point(2, 2), Direction.East));
        System.out.println(state.canPlace(new Placement(state).fixed(ShipType.Carrier, new Point(2, 1), Direction.North)));
        System.out.println(state.canPlace(new Placement(state).fixed(ShipType.Carrier, new Point(3, 1), Direction.North)));
        System.out.println(state.canPlace(new Placement(state).fixed(ShipType.Carrier, new Point(4, 1), Direction.North)));
        System.out.println(state.canPlace(new Placement(state).fixed(ShipType.Carrier, new Point(5, 1), Direction.North)));
        System.out.println(state.canPlace(new Placement(state).fixed(ShipType.Carrier, new Point(6, 1), Direction.North)));
    }

    public boolean canPlace(Point point) {
        if (placedMap == null) {
            placedMap = new boolean[MapDimension][MapDimension];
        }

        if (point == null) {
            return false;
        }

        return !placedMap[point.y][point.x];
    }

    public boolean secludedPlace(Placement placement) {
        System.out.println("Secluded place " + placement);
        // inefficient but easier to code
        Point point = new Point(0, 0);
        for (int i = 0; i < placement.type.length(); i++) {
            switch (placement.direction) {
                case North:
                    point.y = placement.point.y + i;
                    point.x = placement.point.x;
                    break;
                case East:
                    point.y = placement.point.y;
                    point.x = placement.point.x + i;
                    break;
                case South:
                    point.y = placement.point.y - i;
                    point.x = placement.point.x;
                    break;
                case West:
                    point.y = placement.point.y;
                    point.x = placement.point.x - i;
                    break;
            }

            if (point.y + 1 < MapDimension && placedMap[point.y + 1][point.x]) {
                return false;
            }
            if (point.y - 1 >= 0 && placedMap[point.y - 1][point.x]) {
                return false;
            }
            if (point.x + 1 < MapDimension && placedMap[point.y][point.x + 1]) {
                return false;
            }
            if (point.x - 1 >= 0 && placedMap[point.y][point.x - 1]) {
                return false;
            }
        }

        return true;
    }

}
