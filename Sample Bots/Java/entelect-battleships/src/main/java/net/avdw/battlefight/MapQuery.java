package net.avdw.battlefight;

import java.util.List;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Direction;

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

    public static boolean killIsUnfinished(StateModel.OpponentCell[][] map, StateModel.OpponentCell cell, PersistentModel persist) {
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

        if ((cell.Y == 13 && map[12][cell.X].Damaged) || (cell.Y == 0 && map[1][cell.X].Damaged)) {
            return false;
        }
        if ((cell.X == 13 && map[cell.Y][12].Damaged) || (cell.X == 0 && map[cell.Y][1].Damaged)) {
            return false;
        }

        if (persist == null || persist.lastAction == null || cell.X == persist.lastAction.x || cell.Y == persist.lastAction.y) {
            if (persist == null || persist.lastAction == null) {
                return true;
            }
            if (cell.X != persist.lastAction.x) {
                int min = Math.min(cell.X, persist.lastAction.x) + 1;
                int max = Math.max(cell.X, persist.lastAction.x) - 1;

                for (int i = min; i <= max; i++) {
                    if (!(map[cell.Y][i].Damaged || map[cell.Y][i].Missed)) {
                        return false;
                    }
                }
            } else if (cell.Y != persist.lastAction.y) {
                int min = Math.min(cell.Y, persist.lastAction.y);
                int max = Math.max(cell.Y, persist.lastAction.y);

                for (int i = min; i <= max; i++) {
                    if (!(map[i][cell.X].Damaged || map[i][cell.X].Missed)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public static StateModel.OpponentCell[][] transformMap(List<StateModel.OpponentCell> cells) {
        final StateModel.OpponentCell[][] map = new StateModel.OpponentCell[14][14];
        cells.stream().forEach(cell -> {
            map[cell.Y][cell.X] = cell;
        });
        return map;
    }

    public static void printMap(StateModel.OpponentCell[][] map) {
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

    public static void printMap(int[][] map) {
        for (int y = 13; y >= 0; y--) {
            for (int x = 0; x < 14; x++) {
                System.out.print("" + map[y][x]);
            }
            System.out.println(" " + y);
        }
        System.out.println("01234567890123");
    }

    public static boolean shipCanFit(StateModel.OpponentCell[][] map, int x, int y, int length, Direction direction) {
        switch (direction) {
            case North:
                if (y + (length - 1) > 13) {
                    return false;
                }

                for (int i = 0; i < length; i++) {
                    if (map[y + i][x].Damaged || map[y + i][x].Missed) {
                        return false;
                    }
                }
                break;
            case South:
                if (y - (length - 1) < 0) {
                    return false;
                }

                for (int i = 0; i < length; i++) {
                    if (map[y - i][x].Damaged || map[y - i][x].Missed) {
                        return false;
                    }
                }
                break;
            case East:
                if (x + (length - 1) > 13) {
                    return false;
                }

                for (int i = 0; i < length; i++) {
                    if (map[y][x + i].Damaged || map[y][x + i].Missed) {
                        return false;
                    }
                }
                break;
            case West:
                if (x - (length - 1) < 0) {
                    return false;
                }

                for (int i = 0; i < length; i++) {
                    if (map[y][x - i].Damaged || map[y][x - i].Missed) {
                        return false;
                    }
                }
                break;
        }
        return true;
    }

}
