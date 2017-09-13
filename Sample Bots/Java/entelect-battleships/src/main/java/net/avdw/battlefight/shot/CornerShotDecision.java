package net.avdw.battlefight.shot;

import java.util.ArrayList;
import java.util.List;
import net.avdw.battlefight.hunt.PotentialField;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

public class CornerShotDecision {

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
                if (x <= 3 || x >= 10 || y <= 3 || y >= 10) {
                    total += potential.field[y - 1][x - 1];
                    total += potential.field[y - 1][x + 1];
                    total += potential.field[y + 1][x - 1];
                    total += potential.field[y + 1][x + 1];
                }
                if (total > max) {
                    p.x = x;
                    p.y = y;
                    max = total;
                }
            }
        }

        System.out.println("Corner shot fire: " + p);
        return p;
    }

    static public List<Point> check(StateModel.OpponentCell[][] map, PersistentModel.Action last) {
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

        return hits;
    }
}
