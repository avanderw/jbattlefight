package net.avdw.battlefight.hunt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
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
import net.avdw.battlefight.struct.PotentialFieldPoint;

public class HuntDecision {

    static public Action execute(StateModel state) {
        if (state.OpponentMap.Ships.stream().filter(ship -> !ship.Destroyed).count() == 1) {
            PotentialField potential = new PotentialField(state, false, null);
            PriorityQueue<PotentialFieldPoint> weakShots = new PriorityQueue((p1, p2) -> {
                return ((PotentialFieldPoint) p2).potential - ((PotentialFieldPoint) p1).potential;
            });
            weakShots.add(new PotentialFieldPoint(0, 0, potential.field[0][0]));
            weakShots.add(new PotentialFieldPoint(0, 2, potential.field[2][0]));
            weakShots.add(new PotentialFieldPoint(2, 0, potential.field[0][2]));
            weakShots.add(new PotentialFieldPoint(13, 0, potential.field[0][13]));
            weakShots.add(new PotentialFieldPoint(13, 2, potential.field[2][13]));
            weakShots.add(new PotentialFieldPoint(11, 0, potential.field[0][11]));
            weakShots.add(new PotentialFieldPoint(0, 13, potential.field[13][0]));
            weakShots.add(new PotentialFieldPoint(2, 13, potential.field[13][2]));
            weakShots.add(new PotentialFieldPoint(0, 11, potential.field[11][0]));
            weakShots.add(new PotentialFieldPoint(13, 13, potential.field[13][13]));
            weakShots.add(new PotentialFieldPoint(11, 13, potential.field[13][11]));
            weakShots.add(new PotentialFieldPoint(13, 11, potential.field[11][13]));

            System.out.println(weakShots);
            if (weakShots.peek().potential != 0) {
                PotentialFieldPoint p = weakShots.remove();
                return new HuntAction(Action.Type.FIRESHOT, new Point(p.x, p.y));
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
