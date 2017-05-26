package za.co.entelect.challenge;

import java.util.ArrayList;
import za.co.entelect.challenge.domain.command.PlaceShipCommand;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;

public class EdgePlacement {
    static PlaceShipCommand placeShips(GameState state) {
        Placement battleship = new Placement(state).fixed(ShipType.Battleship, new Point(0, 0), Direction.East);
        Placement carrier = new Placement(state).fixed(ShipType.Carrier, new Point(state.MapDimension-1, 0), Direction.North);
        Placement cruiser = new Placement(state).fixed(ShipType.Cruiser, new Point(state.MapDimension-1, state.MapDimension-1), Direction.West);
        Placement destroyer = new Placement(state).fixed(ShipType.Destroyer, new Point(0, state.MapDimension-1), Direction.South);
        Placement submarine = new Placement(state).fixed(ShipType.Submarine, new Point(0, 2), Direction.North);
        
        ArrayList<ShipType> shipsToPlace = new ArrayList<>();
        shipsToPlace.add(battleship.type);
        shipsToPlace.add(carrier.type);
        shipsToPlace.add(cruiser.type);
        shipsToPlace.add(destroyer.type);
        shipsToPlace.add(submarine.type);

        ArrayList<Point> points = new ArrayList<>();
        points.add(battleship.point);
        points.add(carrier.point);
        points.add(cruiser.point);
        points.add(destroyer.point);
        points.add(submarine.point);

        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(battleship.direction);
        directions.add(carrier.direction);
        directions.add(cruiser.direction);
        directions.add(destroyer.direction);
        directions.add(submarine.direction);

        return new PlaceShipCommand(shipsToPlace, points, directions);
    }
}
