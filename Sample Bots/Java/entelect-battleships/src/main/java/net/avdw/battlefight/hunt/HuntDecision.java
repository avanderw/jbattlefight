package net.avdw.battlefight.hunt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.avdw.battlefight.shot.CornerShotDecision;
import net.avdw.battlefight.shot.CrossShotDiagonalDecision;
import net.avdw.battlefight.shot.CrossShotHorizontalDecision;
import net.avdw.battlefight.shot.DoubleShotDecision;
import net.avdw.battlefight.shot.FireShotDecision;
import net.avdw.battlefight.shot.SeekerMissileDecision;
import net.avdw.battlefight.shot.ShotTypeDecision;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateModel.OpponentCell;
import net.avdw.battlefight.struct.Point;

public class HuntDecision {

    static public Action execute(StateModel state) {
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
            Optional<OpponentCell> shot = state.OpponentMap.Cells.stream().filter(cell -> cell.X == weak.x && cell.Y == weak.y && !(cell.Damaged || cell.Missed)).findAny();
            if (shot.isPresent()) {
                return new HuntAction(Action.Type.FIRESHOT, weak);
            }

        }

        switch (ShotTypeDecision.huntShot(state)) {
            case CORNER_SHOT:
                return new HuntAction(Action.Type.CORNER_SHOT, CornerShotDecision.execute(state));
            case CROSS_SHOT_DIAGONAL:
                return new HuntAction(Action.Type.CROSS_SHOT_DIAGONAL, CrossShotDiagonalDecision.execute(state));
            case CROSS_SHOT_HORIZONTAL:
                return new HuntAction(Action.Type.CROSS_SHOT_HORIZONTAL, CrossShotHorizontalDecision.execute(state));
            case DOUBLE_SHOT_HORIZONTAL:
                return DoubleShotDecision.execute(state);
            case SEEKER_MISSILE:
                return new HuntAction(Action.Type.SEEKER_MISSILE, SeekerMissileDecision.execute(state));
            case FIRESHOT:
                return new HuntAction(Action.Type.FIRESHOT, FireShotDecision.execute(state));
        }

        return new HuntAction(Action.Type.FIRESHOT, FireShotDecision.execute(state));
    }
}
