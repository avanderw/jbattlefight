/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.hunt;

import net.avdw.battlefight.Action;
import net.avdw.battlefight.state.StateModel;

/**
 *
 * @author CP318674
 */
public class HuntBehaviourTree {

    static public Action execute(StateModel stateModel) {
        PotentialField field = new PotentialField(stateModel);
        HuntMask mask = new HuntMask(stateModel);
        return null;
    }

}
