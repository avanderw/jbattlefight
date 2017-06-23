package za.co.entelect.challenge.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import za.co.entelect.challenge.Placement;
import za.co.entelect.challenge.domain.command.PlaceShipCommand;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;

public class DanglingDestroyerPlacementStrategy {

    public static PlaceShipCommand placeShips(GameState state) {
        Point start = new Point(ThreadLocalRandom.current().nextInt(state.MapDimension), ThreadLocalRandom.current().nextInt(state.MapDimension));

        List<Placement> placements = new ArrayList();
        placements.add(new Placement(state).group(ShipType.Carrier, start));
        placements.add(new Placement(state).group(ShipType.Battleship, start));
        placements.add(new Placement(state).group(ShipType.Cruiser, start));
        placements.add(new Placement(state).group(ShipType.Submarine, start));
        placements.add(new Placement(state).avoid(ShipType.Destroyer));

        ArrayList<ShipType> shipsToPlace = new ArrayList();
        ArrayList<Point> points = new ArrayList();
        ArrayList<Direction> directions = new ArrayList();

        placements.stream().forEach((placement) -> {
            shipsToPlace.add(placement.type);
            points.add(placement.point);
            directions.add(placement.direction);
        });

        return new PlaceShipCommand(shipsToPlace, points, directions);
    }

    public static void main(String[] args) {
        GameState state = new GameState();
        state.MapDimension = 7;
        placeShips(state);
    }
}
