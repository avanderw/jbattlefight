/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.shot;

import net.avdw.battlefight.hunt.PotentialField;
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
                total += potential.field[y][x];
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
    
}
