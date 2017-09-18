/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.shield;

import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateModel.Cell;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;

/**
 *
 * @author CP318674
 */
public class ShieldDecision {

    public static Action execute(StateModel state) {
        Cell shieldCell =  state.PlayerMap.Owner.Ships.stream().flatMap(ship->ship.Cells.stream().filter(cell->!cell.Hit)).findAny().get();
        return new ShieldAction(new Point(shieldCell.X, shieldCell.Y));
    }
    
}
