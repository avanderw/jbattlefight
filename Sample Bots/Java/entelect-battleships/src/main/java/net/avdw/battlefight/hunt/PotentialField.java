package net.avdw.battlefight.hunt;

import net.avdw.battlefight.MapQuery;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import net.avdw.battlefight.struct.Point;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.struct.Direction;

public class PotentialField {

    final int[][] field = new int[14][14];

    public PotentialField(StateModel stateModel) {
        Stream<StateModel.OpponentShip> huntShips = stateModel.OpponentMap.Ships.stream().filter((ship) -> !ship.Destroyed);

        StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        huntShips.forEach((ship) -> {
            for (int y = 13; y >= 0; y--) {
                for (int x = 0; x < field.length; x++) {
                    if (MapQuery.shipCanFit(map, x, y, ship.ShipType.length(), Direction.North)) {
                        for (int i = 0; i < ship.ShipType.length(); i++) {
                            field[y + i][x]++;
                        }
                    }

                    if (MapQuery.shipCanFit(map, x, y, ship.ShipType.length(), Direction.South)) {
                        for (int i = 0; i < ship.ShipType.length(); i++) {
                            field[y - i][x]++;
                        }
                    }

                    if (MapQuery.shipCanFit(map, x, y, ship.ShipType.length(), Direction.East)) {
                        for (int i = 0; i < ship.ShipType.length(); i++) {
                            field[y][x + i]++;
                        }
                    }

                    if (MapQuery.shipCanFit(map, x, y, ship.ShipType.length(), Direction.West)) {
                        for (int i = 0; i < ship.ShipType.length(); i++) {
                            field[y][x - i]++;
                        }
                    }
                }
            }
        });

        System.out.println(this);
    }

    int potentialAt(int x, int y) {
        return field[y][x];
    }

    void apply(HuntMask huntMask) {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field.length; x++) {
                field[y][x] = huntMask.mask[y][x] ? field[y][x] : 0;
            }
        }
    }

    List<Point> maxPotential() {
        int max = 0;
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field.length; x++) {
                if (max < field[y][x]) {
                    max = field[y][x];
                }
            }
        }

        if (max == 0) {
            return new ArrayList();
        }

        List<Point> points = new ArrayList();
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field.length; x++) {
                if (field[y][x] == max) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Potential field:\n");
        for (int y = 13; y >= 0; y--) {
            for (int x = 0; x < field.length; x++) {
                sb.append(field[y][x] < 10 ? "0" : "")
                        .append(field[y][x])
                        .append("|");
            }
            sb.append(" ").append(y).append("\n");
        }
        sb.append(" 0  1  2  3  4  5  6  7  8  9 10 11 12 13");
        return sb.toString();
    }

}
