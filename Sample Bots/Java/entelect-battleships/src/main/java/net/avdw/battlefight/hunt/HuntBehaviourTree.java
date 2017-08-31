package net.avdw.battlefight.hunt;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;

public class HuntBehaviourTree {

    static public Action execute(StateModel stateModel) {
        PotentialField field = new PotentialField(stateModel);
        HuntMask mask = new HuntMask(stateModel);
        
        field.apply(mask);
        return null;
    }

}
