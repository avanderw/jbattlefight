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
        if (persist == null) {
            System.out.println("WARN: Test state only");
            return true;
        }

        if (persist.unclearedHits.stream().anyMatch(point -> point.x == cell.X && point.y == cell.Y)) {
            if (persist.unclearedHits.size() == 1) {
                return true;
            } else {
                if (persist.unclearedHits.get(0).y - persist.unclearedHits.get(1).y == 0) {
                    boolean leftShot = cell.X - 1 < 0 || map[cell.Y][cell.X - 1].Damaged || map[cell.Y][cell.X - 1].Missed;
                    boolean rightShot = cell.X + 1 > 13 || map[cell.Y][cell.X + 1].Damaged || map[cell.Y][cell.X + 1].Missed;
                    return !(leftShot && rightShot);
                } else if (persist.unclearedHits.get(0).x - persist.unclearedHits.get(1).x == 0) {
                    boolean belowShot = cell.Y - 1 < 0 || map[cell.Y - 1][cell.X].Damaged || map[cell.Y - 1][cell.X].Missed;
                    boolean aboveShot = cell.Y + 1 > 13 || map[cell.Y + 1][cell.X].Damaged || map[cell.Y + 1][cell.X].Missed;
                    return !(belowShot && aboveShot);
                }
            }
        }
        return false;
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
