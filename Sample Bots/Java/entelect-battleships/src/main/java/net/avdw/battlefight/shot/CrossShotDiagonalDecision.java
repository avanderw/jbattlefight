package net.avdw.battlefight.shot;

import net.avdw.battlefight.hunt.PotentialField;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

public class CrossShotDiagonalDecision {

    public static Point execute(StateModel state) {
        PotentialField potential = new PotentialField(state, false, null);
        if (potential.maxPotential().isEmpty()) {
            System.out.println("WARNING: empty potential field");
            StateModel.OpponentCell c = state.OpponentMap.Cells.stream().filter(cell -> !(cell.Damaged || cell.Missed)).findAny().get();
            return new Point(c.X, c.Y);
        }
        
        int max = 0;
        Point p = new Point(0, 0);
        for (int y = 1; y < 13; y++) {
            for (int x = 1; x < 13; x++) {
                int total = 0;
                total += potential.field[y - 1][x - 1];
                total += potential.field[y - 1][x + 1];
                total += potential.field[y + 1][x - 1];
                total += potential.field[y + 1][x + 1];
                total += potential.field[y][x];
                if (total > max) {
                    p.x = x;
                    p.y = y;
                    max = total;
                }
            }
        }

        System.out.println("Chross diagonal shot fire: " + p);
        
        return p;
    }
    
}
