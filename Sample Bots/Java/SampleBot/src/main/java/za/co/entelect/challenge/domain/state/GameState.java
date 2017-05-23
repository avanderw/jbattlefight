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

    private boolean[][] placeMap;

    public void place(Placement placement) {
        if (placeMap == null) {
            placeMap = new boolean[MapDimension][MapDimension];
        }

        switch (placement.direction) {
            case North:
                for (int i = 0; i < placement.type.length(); i++) {
                    placeMap[placement.point.getY() + i][placement.point.getX()] = true;
                }
                break;
            case East:
                for (int i = 0; i < placement.type.length(); i++) {
                    placeMap[placement.point.getY()][placement.point.getX() + 1] = true;
                }
                break;
            case South:
                for (int i = 0; i < placement.type.length(); i++) {
                    placeMap[placement.point.getY() - i][placement.point.getX()] = true;
                }
                break;
            case West:
                for (int i = 0; i < placement.type.length(); i++) {
                    placeMap[placement.point.getY()][placement.point.getX() - i] = true;
                }
                break;
        }

    }

    public boolean canPlace(Placement aThis) {
        return true;
    }

    public static void main(String[] args) {
        GameState state = new GameState();
        state.MapDimension = 10;
        state.place(new Placement(state).fixed(ShipType.Carrier, new Point(2, 2), Direction.East));
        state.place(new Placement(state).fixed(ShipType.Carrier, new Point(3, 1), Direction.North));
    }
}
