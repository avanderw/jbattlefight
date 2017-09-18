/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.shield;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;

/**
 *
 * @author CP318674
 */
public class ShieldAction extends Action {

    public ShieldAction(Point point) {
        super();
        filename = "command.txt";
        this.point = point;
        type = Type.SHIELD;
    }
    
}
