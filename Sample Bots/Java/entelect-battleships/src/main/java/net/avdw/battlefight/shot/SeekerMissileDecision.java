/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.shot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.avdw.battlefight.hunt.PotentialField;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

/**
 *
 * @author CP318674
 */
public class SeekerMissileDecision {

    public static Point execute(StateModel state) {
        PotentialField potential = new PotentialField(state, false, null);
        if (potential.maxPotential().isEmpty()) {
            System.out.println("WARNING: empty potential field");
            StateModel.OpponentCell c = state.OpponentMap.Cells.stream().filter(cell -> !(cell.Damaged || cell.Missed)).findAny().get();
            return new Point(c.X, c.Y);
        }

        int max = 0;
        Point p = new Point(0, 0);
        for (int y = 2; y < 12; y++) {
            for (int x = 2; x < 12; x++) {
                int total = 0;
                total += potential.field[y - 1][x - 1];
                total += potential.field[y - 1][x + 1];
                total += potential.field[y + 1][x - 1];
                total += potential.field[y + 1][x + 1];
                total += potential.field[y - 1][x];
                total += potential.field[y][x + 1];
                total += potential.field[y][x - 1];
                total += potential.field[y + 1][x];
                total += potential.field[y - 2][x];
                total += potential.field[y][x + 2];
                total += potential.field[y][x - 2];
                total += potential.field[y + 2][x];
                total += potential.field[y][x] == 0 ? -8 : potential.field[y][x];
                if (total > max) {
                    p.x = x;
                    p.y = y;
                    max = total;
                }
            }
        }

        System.out.println("Seeker shot fire: " + p);

        return p;
    }

    public static List<Point> check(StateModel.OpponentCell[][] map, PersistentModel.Action last) {
        List<Point> hits = new ArrayList();

        if (map[last.y - 1][last.x - 1].Damaged) {
            hits.add(new Point(last.x - 1, last.y - 1));
        }
        if (map[last.y - 1][last.x + 1].Damaged) {
            hits.add(new Point(last.x + 1, last.y - 1));
        }
        if (map[last.y + 1][last.x - 1].Damaged) {
            hits.add(new Point(last.x - 1, last.y + 1));
        }
        if (map[last.y + 1][last.x + 1].Damaged) {
            hits.add(new Point(last.x + 1, last.y + 1));
        }
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
        if (map[last.y - 2][last.x].Damaged) {
            hits.add(new Point(last.x, last.y - 2));
        }
        if (map[last.y][last.x + 2].Damaged) {
            hits.add(new Point(last.x + 2, last.y));
        }
        if (map[last.y][last.x - 2].Damaged) {
            hits.add(new Point(last.x - 2, last.y));
        }
        if (map[last.y + 2][last.x].Damaged) {
            hits.add(new Point(last.x, last.y + 2));
        }
        if (map[last.y][last.x].Damaged) {
            hits.add(new Point(last.x, last.y));
        }

        return hits;
    }

}
