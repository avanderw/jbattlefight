package net.avdw.battlefight.hunt;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;

public class HuntBehaviourTree {

    static public Action execute(StateModel stateModel) {
        PotentialField field = new PotentialField(stateModel);
        HuntMask mask = (stateModel.OpponentMap.Ships.stream().anyMatch(ship -> ship.ShipType == StateModel.ShipType.Battleship && !ship.Destroyed))
                ? new HuntMask(4)
                : new HuntMask(2);

        field.apply(mask);
        return new HuntAction(field.maxPotential().get(0));
    }
}
