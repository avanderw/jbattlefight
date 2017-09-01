package net.avdw.battlefight.kill;

import java.util.List;
import net.avdw.battlefight.NoAction;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

public class KillBehaviourTree {

    static public Action execute(StateModel stateModel) {
        final StateModel.OpponentCell[][] map = transformMap(stateModel.OpponentMap.Cells);
        StateModel.OpponentCell killShot = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && unfinishedKillCell(map, cell)).findAny().get();

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

    static boolean unfinishedKillCell(StateModel.OpponentCell[][] map, StateModel.OpponentCell cell) {
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

}
