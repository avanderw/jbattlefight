package net.avdw.battlefight;

import java.util.List;
import java.util.stream.Stream;
import net.avdw.battlefight.state.StateModel;

public class MapQuery {

    public static int countShipFit(StateModel.OpponentMap map, final int x, final int y, StateModel.ShipType ship) {
        int fit = 0;
        if (map.Cells.stream().anyMatch(cell -> (cell.X == x && cell.Y == y && !(cell.Damaged || cell.Missed)))) {
            if (y + ship.length() < 14 && map.Cells.stream().filter(cell -> cell.Y >= y && cell.Y < y + ship.length() && cell.X == x && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }

            if (y - ship.length() >= 0 && map.Cells.stream().filter(cell -> cell.Y <= y && cell.Y > y - ship.length() && cell.X == x && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }

            if (x + ship.length() < 14 && map.Cells.stream().filter(cell -> cell.X >= x && cell.X < x + ship.length() && cell.Y == y && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }

            if (x - ship.length() >= 0 && map.Cells.stream().filter(cell -> cell.X <= x && cell.X > x - ship.length() && cell.Y == y && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }
        }
        return fit;
    }

    public static boolean killIsUnfinished(StateModel.OpponentCell[][] map, StateModel.OpponentCell cell, List<StateModel.OpponentShip> ships) {
        if (cell.Y - 1 >= 0 && map[cell.Y - 1][cell.X].Damaged && cell.Y + 1 < 14 && map[cell.Y + 1][cell.X].Damaged) {
            return false;
        }
        if (cell.X - 1 >= 0 && map[cell.Y][cell.X - 1].Damaged && cell.X + 1 < 14 && map[cell.Y][cell.X + 1].Damaged) {
            return false;
        }
        if (cell.Y - 1 >= 0 && map[cell.Y - 1][cell.X].Damaged && cell.Y + 1 < 14 && map[cell.Y + 1][cell.X].Missed) {
            return false;
        }
        if (cell.Y - 1 >= 0 && map[cell.Y - 1][cell.X].Missed && cell.Y + 1 < 14 && map[cell.Y + 1][cell.X].Damaged) {
            return false;
        }
        if (cell.X - 1 >= 0 && map[cell.Y][cell.X - 1].Damaged && cell.X + 1 < 14 && map[cell.Y][cell.X + 1].Missed) {
            return false;
        }
        if (cell.X - 1 >= 0 && map[cell.Y][cell.X - 1].Missed && cell.X + 1 < 14 && map[cell.Y][cell.X + 1].Damaged) {
            return false;
        }

        if (ships != null) {
            Stream<StateModel.OpponentShip> deadShips = ships.stream().filter(ship -> ship.Destroyed);
        }

        return true;
    }

    public static StateModel.OpponentCell[][] transformMap(List<StateModel.OpponentCell> cells) {
        final StateModel.OpponentCell[][] map = new StateModel.OpponentCell[14][14];
        cells.stream().forEach(cell -> {
            map[cell.Y][cell.X] = cell;
        });
        return map;
    }
}
