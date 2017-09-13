package net.avdw.battlefight.state;

import java.util.stream.Stream;
import net.avdw.battlefight.MapQuery;
import net.avdw.battlefight.struct.Action;

public class StateResolver {

    public static AiState state;

    static void reset() {
        state = null;
    }

    static public void setup(final StateModel stateModel, final PersistentModel persist) {
        if (stateModel.Phase == 1) {
            state = AiState.PLACE;
        } else {
            long damagedCells = stateModel.OpponentMap.Cells.stream().filter((cell) -> cell.Damaged).count();
            int deadShipCells = stateModel.OpponentMap.Ships.stream().filter(ship -> ship.Destroyed).mapToInt((ship) -> ship.ShipType.length()).sum();
            if (damagedCells == deadShipCells) {
                state = AiState.HUNT;
            } else if (persist.lastAction.type != Action.Type.FIRESHOT) {
                state = AiState.KILL;
            } else {
                final StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
                Stream<StateModel.OpponentCell> unfinishedCells = stateModel.OpponentMap.Cells.stream().filter((cell) -> cell.Damaged && MapQuery.killIsUnfinished(map, cell, persist));
                state = (unfinishedCells.count() > 0) ? AiState.KILL : AiState.HUNT;
            }
        }
    }

    public enum AiState {
        PLACE, HUNT, KILL;
    }
}
