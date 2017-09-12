/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.shot;

import java.util.ArrayList;
import java.util.List;
import net.avdw.battlefight.hunt.HuntAction;
import net.avdw.battlefight.hunt.PotentialField;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;

/**
 *
 * @author CP318674
 */
public class DoubleShotDecision {

    public static HuntAction execute(StateModel state) {
        PotentialField potential = new PotentialField(state, false, null);
        if (potential.maxPotential().isEmpty()) {
            System.out.println("WARNING: empty potential field");
            StateModel.OpponentCell c = state.OpponentMap.Cells.stream().filter(cell -> !(cell.Damaged || cell.Missed)).findAny().get();
            return new HuntAction(Action.Type.FIRESHOT, new Point(c.X, c.Y));
        }

        boolean vertical = true;
        int max = 0;
        Point p = new Point(0, 0);
        for (int y = 1; y < 13; y++) {
            for (int x = 1; x < 13; x++) {
                int total = 0;
                total += potential.field[y - 1][x];
                total += potential.field[y + 1][x];
                if (total > max) {
                    p.x = x;
                    p.y = y;
                    max = total;
                    vertical = true;
                }
            }
        }

        for (int y = 1; y < 13; y++) {
            for (int x = 1; x < 13; x++) {
                int total = 0;
                total += potential.field[y][x + 1];
                total += potential.field[y][x - 1];
                if (total > max) {
                    p.x = x;
                    p.y = y;
                    max = total;
                    vertical = false;
                }
            }
        }

        System.out.println("Double shot fire (vertical:" + vertical + "): " + p);

        return new HuntAction(vertical ? Action.Type.DOUBLE_SHOT_VERTICAL : Action.Type.DOUBLE_SHOT_HORIZONTAL, p);
    }

    public static List<Point> check(StateModel.OpponentCell[][] map, PersistentModel.Action last) {
        List<Point> hits = new ArrayList();

        if (map[last.y - 1][last.x].Damaged) {
            hits.add(new Point(last.x, last.y - 1));
        }
        if (map[last.y][last.x + 1].Damaged) {
            hits.add(new Point(last.x + 1, last.y));
        }
        if (map[last.y][last.x - 1].Damaged) {
            hits.add(new Point(last.x - 1, last.y));
        }
        if (map[last.y + 1][last.x].Damaged) {
            hits.add(new Point(last.x, last.y + 1));
        }

        return hits;
    }

}
