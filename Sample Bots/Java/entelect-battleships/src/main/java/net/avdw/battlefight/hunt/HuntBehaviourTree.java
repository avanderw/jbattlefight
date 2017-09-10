package net.avdw.battlefight.hunt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.avdw.battlefight.NoAction;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateModel.OpponentCell;
import net.avdw.battlefight.struct.Point;

public class HuntBehaviourTree {

    static public Action execute(StateModel stateModel) {
        List<Point> weakShots = new ArrayList();
        weakShots.add(new Point(0, 0));
        weakShots.add(new Point(0, 2));
        weakShots.add(new Point(2, 0));
        weakShots.add(new Point(13, 0));
        weakShots.add(new Point(13, 2));
        weakShots.add(new Point(11, 0));
        weakShots.add(new Point(0, 13));
        weakShots.add(new Point(2, 13));
        weakShots.add(new Point(0, 11));
        weakShots.add(new Point(13, 13));
        weakShots.add(new Point(11, 13));
        weakShots.add(new Point(13, 11));

        while (!weakShots.isEmpty()) {
            Point weak = weakShots.remove(0);
            Optional<OpponentCell> shot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.X == weak.x && cell.Y == weak.y && !(cell.Damaged || cell.Missed)).findAny();
            if (shot.isPresent()) {
                return new HuntAction(weak);
            }

        }
        PotentialField field = new PotentialField(stateModel, false, null);

        if (field.maxPotential().isEmpty()) {
            System.out.println("WARNING: empty potential field");
            OpponentCell c = stateModel.OpponentMap.Cells.stream().filter(cell-> !(cell.Damaged || cell.Missed)).findAny().get();
            return new HuntAction(new Point(c.X, c.Y));
        }
        return new HuntAction(field.maxPotential().remove(ThreadLocalRandom.current().nextInt(field.maxPotential().size())));
    }
}
