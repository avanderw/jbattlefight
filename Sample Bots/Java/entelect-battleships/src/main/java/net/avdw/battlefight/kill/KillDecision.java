package net.avdw.battlefight.kill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.avdw.battlefight.MapQuery;
import net.avdw.battlefight.hunt.PotentialField;
import net.avdw.battlefight.shot.CornerShotDecision;
import net.avdw.battlefight.shot.CrossShotDiagonalDecision;
import net.avdw.battlefight.shot.CrossShotHorizontalDecision;
import net.avdw.battlefight.shot.DoubleShotDecision;
import net.avdw.battlefight.shot.FireShotDecision;
import net.avdw.battlefight.shot.SeekerMissileDecision;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateModel.OpponentCell;
import net.avdw.battlefight.state.StateModel.OpponentShip;
import net.avdw.battlefight.struct.Direction;
import net.avdw.battlefight.struct.Point;

public class KillDecision {

    static private StateModel state;

    static public Action execute(final StateModel stateModel, final PersistentModel persist) {
        state = stateModel;
        final StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        MapQuery.printMap(map);

        List<Point> recentHits = new ArrayList();
        if (persist.lastAction != null && persist.lastAction.type != null) {
            switch (persist.lastAction.type) {
                case CORNER_SHOT:
                    recentHits.addAll(CornerShotDecision.check(map, persist.lastAction));
                    break;
                case CROSS_SHOT_DIAGONAL:
                    recentHits.addAll(CrossShotDiagonalDecision.check(map, persist.lastAction));
                    break;
                case CROSS_SHOT_HORIZONTAL:
                    recentHits.addAll(CrossShotHorizontalDecision.check(map, persist.lastAction));
                    break;
                case DOUBLE_SHOT_HORIZONTAL:
                case DOUBLE_SHOT_VERTICAL:
                    recentHits.addAll(DoubleShotDecision.check(map, persist.lastAction));
                    break;
                case SEEKER_MISSILE:
                    recentHits.addAll(SeekerMissileDecision.check(map, persist.lastAction));
                    break;
                case FIRESHOT:
                    recentHits.addAll(FireShotDecision.check(map, persist.lastAction));
                    break;
            }
        }
        if (persist.unclearedHits == null) {
            persist.unclearedHits = new ArrayList();
        }
        if (persist.clearedHits == null) {
            persist.clearedHits = new ArrayList();
        }
        
        persist.clearedHits.stream().forEach(p -> recentHits.remove(p));
        persist.unclearedHits.addAll(recentHits);

        if (persist.huntShips != null && persist.huntShips.size() > state.OpponentMap.Ships.stream().filter(ship -> !ship.Destroyed).count()) {
            // determine ship that was killed
            for (OpponentShip ship : state.OpponentMap.Ships.stream().filter(ship -> !ship.Destroyed).collect(Collectors.toList())) {
                persist.huntShips.remove(ship);
            }
            int clear = persist.huntShips.get(0).ShipType.length();
            if (persist.unclearedHits.size() == clear) {
                System.out.println("Clearing shots");
                persist.clearedHits.addAll(persist.unclearedHits);
                persist.unclearedHits.clear();
            } else {
                System.out.println("Clearing shots with reamainder");
                int remain = persist.unclearedHits.size() - clear;
                for (int i = persist.unclearedHits.size() - 1; i > remain; i--) {
                    persist.clearedHits.add(persist.unclearedHits.remove(i));
                }
                Point p = persist.unclearedHits.remove(persist.unclearedHits.size() - 1);
                persist.clearedHits.add(p);

                Point rPoint = persist.unclearedHits.stream().filter(point -> Math.abs(p.x - point.x) < 2 || Math.abs(p.y - point.y) < 2).findAny().get();
                persist.unclearedHits.remove(rPoint);
                persist.clearedHits.add(rPoint);

                System.out.println("Remainder uncleared " + persist.unclearedHits);
            }
        }

        Optional<OpponentCell> nextShot = null;
        if (persist.lastHeading != null && !map[persist.lastAction.y][persist.lastAction.x].Missed) {
            switch (persist.lastHeading) {
                case North:
                    nextShot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.X == persist.lastAction.x && cell.Y == persist.lastAction.y + 1).findAny();
                    break;
                case South:
                    nextShot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.X == persist.lastAction.x && cell.Y == persist.lastAction.y - 1).findAny();
                    break;
                case East:
                    nextShot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.X == persist.lastAction.x + 1 && cell.Y == persist.lastAction.y).findAny();
                    break;
                case West:
                    nextShot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.X == persist.lastAction.x - 1 && cell.Y == persist.lastAction.y).findAny();
                    break;
            }

            if (nextShot.isPresent() && !(nextShot.get().Damaged || nextShot.get().Missed)) {
                return new KillAction(new Point(nextShot.get().X, nextShot.get().Y));
            }
        }

        if (!recentHits.isEmpty()) {

            if (recentHits.size() == 2) {
                if (recentHits.get(0).x - recentHits.get(1).x != 0) {
                    return new KillAction(new Point((recentHits.get(0).x + recentHits.get(1).x) / 2, recentHits.get(0).y));
                } else {
                    return new KillAction(new Point(recentHits.get(0).x, (recentHits.get(0).y + recentHits.get(1).y) / 2));
                }
            }
//            Point p = recentHits.get(0);
//            return finishKill(map, map[p.y][p.x], persist);
        }

        if (persist.unclearedHits.isEmpty()) {
            return null;
        }

        Optional<StateModel.OpponentCell> killShotOption = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && MapQuery.killIsUnfinished(map, cell, persist)).findAny();
        if (!killShotOption.isPresent()) {
            return null;
        }

        StateModel.OpponentCell killShot = killShotOption.get();

        System.out.println("Finishing kill from " + killShot.X + ", " + killShot.Y);
        return finishKill(map, killShot, persist);
    }

    private static Action finishKill(StateModel.OpponentCell[][] map, StateModel.OpponentCell killShot, PersistentModel persist) {
        if (persist.lastHeading == null) {
            if (killShot.Y - 1 >= 0 && killShot.Y + 1 < 14 && map[killShot.Y - 1][killShot.X].Damaged && !(map[killShot.Y + 1][killShot.X].Missed || map[killShot.Y + 1][killShot.X].Damaged)) {
                persist.lastHeading = Direction.North;
                return new KillAction(new Point(killShot.X, killShot.Y + 1));
            } else if (killShot.Y + 1 < 14 && killShot.Y - 1 >= 0 && map[killShot.Y + 1][killShot.X].Damaged && !(map[killShot.Y - 1][killShot.X].Missed || map[killShot.Y - 1][killShot.X].Damaged)) {
                persist.lastHeading = Direction.South;
                return new KillAction(new Point(killShot.X, killShot.Y - 1));
            } else if (killShot.X - 1 >= 0 && killShot.X + 1 < 14 && map[killShot.Y][killShot.X - 1].Damaged && !(map[killShot.Y][killShot.X + 1].Missed || map[killShot.Y][killShot.X + 1].Damaged)) {
                persist.lastHeading = Direction.East;
                return new KillAction(new Point(killShot.X + 1, killShot.Y));
            } else if (killShot.X + 1 < 14 && killShot.X - 1 >= 0 && map[killShot.Y][killShot.X + 1].Damaged && !(map[killShot.Y][killShot.X - 1].Missed || map[killShot.Y][killShot.X - 1].Damaged)) {
                persist.lastHeading = Direction.West;
                return new KillAction(new Point(killShot.X - 1, killShot.Y));
            }

            return huntAxis(map, killShot);
        } else {
            switch (persist.lastHeading) {
                case North:
                    if (killShot.Y + 1 < 14 && !(map[killShot.Y + 1][killShot.X].Damaged || map[killShot.Y + 1][killShot.X].Missed)) {
                        return new KillAction(new Point(killShot.X, killShot.Y + 1));
                    } else {
                        persist.lastHeading = Direction.South;
                        return new KillAction(new Point(killShot.X, killShot.Y - 1));
                    }
                case South:
                    if (killShot.Y - 1 >= 0 && !(map[killShot.Y - 1][killShot.X].Damaged || map[killShot.Y - 1][killShot.X].Missed)) {
                        return new KillAction(new Point(killShot.X, killShot.Y - 1));
                    } else {
                        persist.lastHeading = Direction.North;
                        return new KillAction(new Point(killShot.X, killShot.Y + 1));
                    }
                case East:
                    if (killShot.X + 1 < 14 && !(map[killShot.Y][killShot.X + 1].Damaged || map[killShot.Y][killShot.X + 1].Missed)) {
                        return new KillAction(new Point(killShot.X + 1, killShot.Y));
                    } else {
                        persist.lastHeading = Direction.West;
                        return new KillAction(new Point(killShot.X - 1, killShot.Y));
                    }
                case West:
                    if (killShot.X - 1 >= 0 && !(map[killShot.Y][killShot.X - 1].Damaged || map[killShot.Y][killShot.X - 1].Missed)) {
                        return new KillAction(new Point(killShot.X - 1, killShot.Y));
                    } else {
                        persist.lastHeading = Direction.East;
                        return new KillAction(new Point(killShot.X + 1, killShot.Y));
                    }
                default:
                    return null;
            }
        }
    }

    static Action huntAxis(final StateModel.OpponentCell[][] map, StateModel.OpponentCell cell) {
        System.out.println("Hunting axis");
        PotentialField field = new PotentialField(state, true, new Point(cell.X, cell.Y));

        int max = 0;
        Point shot = new Point(0, 0);
        if (cell.Y - 1 >= 0 && field.potentialAt(cell.X, cell.Y - 1) > max) {
            max = field.potentialAt(cell.X, cell.Y - 1);
            shot.x = cell.X;
            shot.y = cell.Y - 1;
        }
        if (cell.Y + 1 < 14 && field.potentialAt(cell.X, cell.Y + 1) > max) {
            max = field.potentialAt(cell.X, cell.Y + 1);
            shot.x = cell.X;
            shot.y = cell.Y + 1;
        }
        if (cell.X - 1 >= 0 && field.potentialAt(cell.X - 1, cell.Y) > max) {
            max = field.potentialAt(cell.X - 1, cell.Y);
            shot.x = cell.X - 1;
            shot.y = cell.Y;
        }
        if (cell.X + 1 < 14 && field.potentialAt(cell.X + 1, cell.Y) > max) {
            max = field.potentialAt(cell.X + 1, cell.Y);
            shot.x = cell.X + 1;
            shot.y = cell.Y;
        }

        if (max == 0) {
            System.out.println("Should never happen during kill!");
            if (cell.Y - 1 >= 0 && (!(map[cell.Y - 1][cell.X].Damaged || map[cell.Y - 1][cell.X].Missed))) {
                return new KillAction(new Point(cell.X, cell.Y - 1));
            } else if (cell.X + 1 < 14 && (!(map[cell.Y][cell.X + 1].Damaged || map[cell.Y][cell.X + 1].Missed))) {
                return new KillAction(new Point(cell.X + 1, cell.Y));
            } else if (cell.Y + 1 < 14 && (!(map[cell.Y + 1][cell.X].Damaged || map[cell.Y + 1][cell.X].Missed))) {
                return new KillAction(new Point(cell.X, cell.Y + 1));
            } else if (cell.X - 1 >= 0 && (!(map[cell.Y][cell.X - 1].Damaged || map[cell.Y][cell.X - 1].Missed))) {
                return new KillAction(new Point(cell.X - 1, cell.Y));
            }
        }

        return new KillAction(shot);
    }

}
