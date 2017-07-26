package net.avdw.battlefight.place;

import net.avdw.battlefight.Action;
import net.avdw.battlefight.state.StateModel;

public class PlacementStrategy {

    static public Action place(StateModel stateModel) {
        return new PlaceAction();
    }
}
