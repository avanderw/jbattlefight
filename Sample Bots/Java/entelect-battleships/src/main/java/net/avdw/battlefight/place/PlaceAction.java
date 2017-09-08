package net.avdw.battlefight.place;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Zone;

public class PlaceAction extends Action {

    List<String> ships = new ArrayList();

    public PlaceAction() {
        super();
        filename = "place.txt";
    }

    public void place(StateModel.ShipType type, net.avdw.battlefight.struct.Place place) {
        try {
            ships.add(String.format("%s %s %s %s", new Object[]{type, place.x, place.y, place.direction.name()}));
            Zone zone = Zone.ALL_ZONES.stream().filter(z -> z.containsPoint(place.x, place.y)).findAny().get();
            zone.addShip(type);
        } catch (Exception e) {
            System.out.println(ships);
            throw e;
        }
    }

    @Override
    public String toString() {
        return ships.stream().collect(Collectors.joining("\n"));
    }
}
