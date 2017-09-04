package net.avdw.battlefight.kill;

import java.util.List;
import net.avdw.battlefight.NoAction;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

public class KillBehaviourTree {

    static public Action execute(StateModel stateModel) {
        final StateModel.OpponentCell[][] map = transformMap(stateModel.OpponentMap.Cells);
        StateModel.OpponentCell killShot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && killIsUnfinished(map, cell)).findAny().get();

        printMap(map);
        return finishKill(map, killShot);
    }

    private static Action finishKill(StateModel.OpponentCell[][] map, StateModel.OpponentCell killShot) {
        if (map[killShot.Y - 1][killShot.X].Damaged && !(map[killShot.Y + 1][killShot.X].Missed || map[killShot.Y + 1][killShot.X].Damaged)) {
            return new KillAction(new Point(killShot.X, killShot.Y + 1));
        } else if (map[killShot.Y + 1][killShot.X].Damaged && !(map[killShot.Y - 1][killShot.X].Missed || map[killShot.Y - 1][killShot.X].Damaged)) {
            return new KillAction(new Point(killShot.X, killShot.Y - 1));
        } else if (map[killShot.Y][killShot.X - 1].Damaged && !(map[killShot.Y][killShot.X + 1].Missed || map[killShot.Y][killShot.X + 1].Damaged)) {
            return new KillAction(new Point(killShot.X + 1, killShot.Y));
        } else if (map[killShot.Y][killShot.X + 1].Damaged && !(map[killShot.Y][killShot.X - 1].Missed || map[killShot.Y][killShot.X - 1].Damaged)) {
            return new KillAction(new Point(killShot.X - 1, killShot.Y));
        }

        return huntAxis(map, killShot);
    }

    static Action huntAxis(final StateModel.OpponentCell[][] map, StateModel.OpponentCell killShot) {
        if (!(map[killShot.Y - 1][killShot.X].Damaged || map[killShot.Y - 1][killShot.X].Missed)) {
            return new KillAction(new Point(killShot.X, killShot.Y - 1));
        } else if (!(map[killShot.Y][killShot.X + 1].Damaged || map[killShot.Y][killShot.X + 1].Missed)) {
            return new KillAction(new Point(killShot.X + 1, killShot.Y));
        } else if (!(map[killShot.Y + 1][killShot.X].Damaged || map[killShot.Y + 1][killShot.X].Missed)) {
            return new KillAction(new Point(killShot.X, killShot.Y + 1));
        } else if (!(map[killShot.Y][killShot.X - 1].Damaged || map[killShot.Y][killShot.X - 1].Missed)) {
            return new KillAction(new Point(killShot.X - 1, killShot.Y));
        }

        return new NoAction();
    }

    static boolean killIsUnfinished(StateModel.OpponentCell[][] map, StateModel.OpponentCell cell) {
        if (map[cell.Y - 1][cell.X].Damaged && map[cell.Y + 1][cell.X].Damaged) {
            return false;
        }
        if (map[cell.Y][cell.X - 1].Damaged && map[cell.Y][cell.X + 1].Damaged) {
            return false;
        }
        if (map[cell.Y - 1][cell.X].Damaged && map[cell.Y + 1][cell.X].Missed) {
            return false;
        }
        if (map[cell.Y - 1][cell.X].Missed && map[cell.Y + 1][cell.X].Damaged) {
            return false;
        }
        if (map[cell.Y][cell.X - 1].Damaged && map[cell.Y][cell.X + 1].Missed) {
            return false;
        }
        if (map[cell.Y][cell.X - 1].Missed && map[cell.Y][cell.X + 1].Damaged) {
            return false;
        }

        return true;
    }

    static StateModel.OpponentCell[][] transformMap(List<StateModel.OpponentCell> cells) {
        final StateModel.OpponentCell[][] map = new StateModel.OpponentCell[14][14];
        cells.stream().forEach(cell -> {
            map[cell.Y][cell.X] = cell;
        });
        return map;
    }

    private static void printMap(StateModel.OpponentCell[][] map) {
        for (int y = 13; y >= 0; y--) {
            for (int x = 0; x < 14; x++) {
                if (map[y][x].Damaged) {
                    System.out.print("!");
                } else if (map[y][x].Missed) {
                    System.out.print("x");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println(" " + y);
        }
        System.out.println("01234567890123");
    }

}
