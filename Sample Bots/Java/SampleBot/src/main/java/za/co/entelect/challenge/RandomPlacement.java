/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.entelect.challenge;

import java.util.ArrayList;
import za.co.entelect.challenge.domain.command.PlaceShipCommand;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;

/**
 *
 * @author CP318674
 */
public class RandomPlacement {

    static PlaceShipCommand placeShips(GameState state) {
        Placement battleship = new Placement(state).random(ShipType.Battleship);
        Placement carrier = new Placement(state).random(ShipType.Carrier);
        Placement cruiser = new Placement(state).random(ShipType.Cruiser);
        Placement destroyer = new Placement(state).random(ShipType.Destroyer);
        Placement submarine = new Placement(state).random(ShipType.Submarine);

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
