/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.shot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.avdw.battlefight.hunt.PotentialField;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

/**
 *
 * @author CP318674
 */
public class FireShotDecision {

    static public Point execute(StateModel state) {
        PotentialField field = new PotentialField(state, false, null);

        if (field.maxPotential().isEmpty()) {
            System.out.println("WARNING: empty potential field");
            StateModel.OpponentCell c = state.OpponentMap.Cells.stream().filter(cell -> !(cell.Damaged || cell.Missed)).findAny().get();
            return new Point(c.X, c.Y);
        }

        return field.maxPotential().remove(ThreadLocalRandom.current().nextInt(field.maxPotential().size()));
    }

    static public List<Point> check(StateModel.OpponentCell[][] map, PersistentModel.Action last) {
        List<Point> hits = new ArrayList();

        if (map[last.y][last.x].Damaged) {
            hits.add(new Point(last.x, last.y));
        }

        return hits;
    }
}
