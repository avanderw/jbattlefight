package net.avdw.battlefight.place;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Zone;
import net.avdw.battlefight.state.StateModel;

public class PlacementStrategy {

    static public Action place(StateModel stateModel) {
        PlaceAction action = new PlaceAction();
        List<Zone> remainingZones = new ArrayList(Zone.CORNER_ZONES);
        
        Zone currentZone;
        currentZone = remainingZones.remove(ThreadLocalRandom.current().nextInt(remainingZones.size()));
        currentZone.addShip(StateModel.ShipType.Carrier);
        action.place(StateModel.ShipType.Carrier, currentZone.fit(StateModel.ShipType.Carrier, true));
        
        currentZone = remainingZones.remove(ThreadLocalRandom.current().nextInt(remainingZones.size()));
        currentZone.addShip(StateModel.ShipType.Battleship);
        action.place(StateModel.ShipType.Battleship, currentZone.fit(StateModel.ShipType.Battleship, true));

        currentZone = remainingZones.remove(ThreadLocalRandom.current().nextInt(remainingZones.size()));
        currentZone.addShip(StateModel.ShipType.Cruiser);
        action.place(StateModel.ShipType.Cruiser, currentZone.fit(StateModel.ShipType.Cruiser, false));
        
        currentZone = remainingZones.remove(ThreadLocalRandom.current().nextInt(remainingZones.size()));
        currentZone.addShip(StateModel.ShipType.Submarine);
        action.place(StateModel.ShipType.Submarine, currentZone.fit(StateModel.ShipType.Submarine, false));

        currentZone = Zone.ADJACENT_ZONES.get(ThreadLocalRandom.current().nextInt(Zone.ADJACENT_ZONES.size()));
        currentZone.addShip(StateModel.ShipType.Destroyer);
        action.place(StateModel.ShipType.Destroyer, currentZone.fit(StateModel.ShipType.Destroyer, false));

        return action;
    }
}
