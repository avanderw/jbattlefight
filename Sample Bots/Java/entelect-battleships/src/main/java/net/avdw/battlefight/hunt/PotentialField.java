package net.avdw.battlefight.hunt;

import java.util.stream.Stream;
import net.avdw.battlefight.state.StateModel;

public class PotentialField {

    public PotentialField(StateModel stateModel) {
        Stream<StateModel.OpponentShip> huntShips = stateModel.OpponentMap.Ships.stream().filter((ship) -> !ship.Destroyed);
        final int[][] field = new int[14][14];
        huntShips.forEach((ship) -> {
            for (int y = 0; y < field.length; y++) {
                for (int x = 0; x < field.length; x++) {
                    field[y][x] += MapQuery.countShipFit(stateModel.OpponentMap, x, y, ship.ShipType);
                }
            }
        });
    }

}
