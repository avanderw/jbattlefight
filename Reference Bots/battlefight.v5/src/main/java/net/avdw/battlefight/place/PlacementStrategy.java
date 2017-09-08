package net.avdw.battlefight.place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Direction;
import net.avdw.battlefight.struct.Place;

public class PlacementStrategy {

    static public Action place(StateModel stateModel) {
        List<Direction> remainingSides = new ArrayList(Arrays.asList(Direction.values()));
        
        PlaceAction action = new PlaceAction();
        action.place(StateModel.ShipType.Destroyer,
                onCornerEdge(remainingSides.remove(ThreadLocalRandom.current().nextInt(remainingSides.size())), 2, 0));

        action.place(StateModel.ShipType.Submarine,
                onCornerEdge(remainingSides.remove(ThreadLocalRandom.current().nextInt(remainingSides.size())), 3, 0));

        action.place(StateModel.ShipType.Cruiser,
                onCornerEdge(remainingSides.remove(ThreadLocalRandom.current().nextInt(remainingSides.size())), 3, 1));
        
        action.place(StateModel.ShipType.Battleship,
                onCornerEdge(remainingSides.remove(ThreadLocalRandom.current().nextInt(remainingSides.size())), 4, 1));
        
        action.place(StateModel.ShipType.Carrier, 
                onCenterEdge(Direction.random(), 5, 2));

        return action;
    }

    private static Place onCornerEdge(Direction side, int length, int indent) {
        final Place place = new Place(0, 0, side);
        switch (side) {
            case North:
                place.y = 13 - indent;
                place.x = ThreadLocalRandom.current().nextBoolean() ? length - 1 : 13 - length;
                place.direction = Direction.East;
                break;
            case South:
                place.y = 0 + indent;
                place.x = ThreadLocalRandom.current().nextBoolean() ? length - 1 : 13 - length;
                place.direction = Direction.East;
                break;
            case East:
                place.x = 13 - indent;
                place.y = ThreadLocalRandom.current().nextBoolean() ? length - 1 : 13 - length;
                place.direction = Direction.North;
                break;
            case West:
                place.x = 0 + indent;
                place.y = ThreadLocalRandom.current().nextBoolean() ? length - 1 : 13 - length;
                place.direction = Direction.North;
                break;
        }
        return place;
    }
    
    private static Place onCenterEdge(Direction side, int length, int indent) {
        final Place place = new Place(0, 0, side);
        switch (side) {
            case North:
                place.y = 13 - indent;
                place.x = 7 - Math.round(length/2);
                place.direction = Direction.East;
                break;
            case South:
                place.y = 0 + indent;
                place.x = 7 - Math.round(length/2);
                place.direction = Direction.East;
                break;
            case East:
                place.x = 13 - indent;
                place.y = 7 - Math.round(length/2);
                place.direction = Direction.North;
                break;
            case West:
                place.x = 0 + indent;
                place.y = 7 - Math.round(length/2);
                place.direction = Direction.North;
                break;
        }
        return place;
    }

}
