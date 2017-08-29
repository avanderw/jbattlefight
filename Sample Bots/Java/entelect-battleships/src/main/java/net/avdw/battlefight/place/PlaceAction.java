package net.avdw.battlefight.place;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.avdw.battlefight.Action;
import net.avdw.battlefight.Place;
import net.avdw.battlefight.state.StateModel;

public class PlaceAction extends Action {

    List<String> ships = new ArrayList();

    public PlaceAction() {
        super();
        filename = "place.txt";
    }

    public void place(StateModel.ShipType type, Place place) {
        ships.add(String.format("%s %s %s %s", new Object[]{type, place.x, place.y, place.direction.name()}));
    }

    @Override
    public String toString() {
        return ships.stream().collect(Collectors.joining("\n"));
    }
}
