package net.avdw.battlefight.hunt;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import net.avdw.battlefight.Point;
import net.avdw.battlefight.state.StateModel;

public class PotentialField {

    final int[][] field = new int[14][14];

    public PotentialField(StateModel stateModel) {
        Stream<StateModel.OpponentShip> huntShips = stateModel.OpponentMap.Ships.stream().filter((ship) -> !ship.Destroyed);

        huntShips.forEach((ship) -> {
            for (int y = 0; y < field.length; y++) {
                for (int x = 0; x < field.length; x++) {
                    field[y][x] += MapQuery.countShipFit(stateModel.OpponentMap, x, y, ship.ShipType);
                }
            }
        });
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
            sb.append("\n");
        }

        return sb.toString();
    }

}
