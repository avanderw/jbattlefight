package net.avdw.battlefight.kill;

import java.util.Optional;
import net.avdw.battlefight.MapQuery;
import net.avdw.battlefight.NoAction;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Point;

public class KillBehaviourTree {

    static public Action execute(StateModel stateModel) {
        final StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        Optional<StateModel.OpponentCell> killShotOption = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && MapQuery.killIsUnfinished(map, cell)).findAny();
        if (!killShotOption.isPresent()) {
            return null;
        }

        StateModel.OpponentCell killShot = killShotOption.get();

        printMap(map);
        return finishKill(map, killShot);
    }

    private static Action finishKill(StateModel.OpponentCell[][] map, StateModel.OpponentCell killShot) {
        if ((killShot.Y - 1 >= 0 && map[killShot.Y - 1][killShot.X].Damaged) && (killShot.Y + 1 < 14 && !(map[killShot.Y + 1][killShot.X].Missed || map[killShot.Y + 1][killShot.X].Damaged))) {
            return new KillAction(new Point(killShot.X, killShot.Y + 1));
        } else if ((killShot.Y + 1 < 14 && map[killShot.Y + 1][killShot.X].Damaged) && (killShot.Y - 1 >= 0 && !(map[killShot.Y - 1][killShot.X].Missed || map[killShot.Y - 1][killShot.X].Damaged))) {
            return new KillAction(new Point(killShot.X, killShot.Y - 1));
        } else if ((killShot.X - 1 >= 0 && map[killShot.Y][killShot.X - 1].Damaged) && (killShot.X + 1 < 14 && !(map[killShot.Y][killShot.X + 1].Missed || map[killShot.Y][killShot.X + 1].Damaged))) {
            return new KillAction(new Point(killShot.X + 1, killShot.Y));
        } else if ((killShot.X + 1 < 14 && map[killShot.Y][killShot.X + 1].Damaged) && (killShot.X - 1 >= 0 && !(map[killShot.Y][killShot.X - 1].Missed || map[killShot.Y][killShot.X - 1].Damaged))) {
            return new KillAction(new Point(killShot.X - 1, killShot.Y));
        }

        return huntAxis(map, killShot);
    }

    static Action huntAxis(final StateModel.OpponentCell[][] map, StateModel.OpponentCell killShot) {
        if (killShot.Y - 1 >= 0 && (!(map[killShot.Y - 1][killShot.X].Damaged || map[killShot.Y - 1][killShot.X].Missed))) {
            return new KillAction(new Point(killShot.X, killShot.Y - 1));
        } else if (killShot.X + 1 < 14 && (!(map[killShot.Y][killShot.X + 1].Damaged || map[killShot.Y][killShot.X + 1].Missed))) {
            return new KillAction(new Point(killShot.X + 1, killShot.Y));
        } else if (killShot.Y + 1 < 14 && (!(map[killShot.Y + 1][killShot.X].Damaged || map[killShot.Y + 1][killShot.X].Missed))) {
            return new KillAction(new Point(killShot.X, killShot.Y + 1));
        } else if (killShot.X - 1 >= 0 && (!(map[killShot.Y][killShot.X - 1].Damaged || map[killShot.Y][killShot.X - 1].Missed))) {
            return new KillAction(new Point(killShot.X - 1, killShot.Y));
        }

        return new NoAction();
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
