package net.avdw.battlefight.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.avdw.battlefight.state.StateModel.Cell;

public class StateResolver {

    public static AiState state;

    static void reset() {
        state = null;
    }

    static public void setup(final StateModel stateModel, final PersistentModel persist) {
        if (stateModel.Phase == 1) {
            state = AiState.PLACE;
        } else {
            long myDamagedCells = stateModel.PlayerMap.Owner.Ships.stream().mapToLong(ship -> ship.Cells.stream().filter(cell -> cell.Hit).count()).sum();
            long myShipCells = stateModel.PlayerMap.Owner.Ships.stream().mapToLong(ship -> ship.Cells.size()).sum();
            if (!stateModel.PlayerMap.Owner.Shield.Active && stateModel.PlayerMap.Owner.Shield.CurrentCharges > 0 && myShipCells - myDamagedCells <= 1) {
                state = AiState.SHIELD;
            } else {
                long damagedCells = stateModel.OpponentMap.Cells.stream().filter((cell) -> cell.Damaged).count();
                int deadShipCells = stateModel.OpponentMap.Ships.stream().filter(ship -> ship.Destroyed).mapToInt((ship) -> ship.ShipType.length()).sum();
                if (damagedCells == deadShipCells) {
                    state = AiState.HUNT;
                } else {
                    state = AiState.KILL;
                }
            }
        }
    }

    public enum AiState {
        PLACE, HUNT, KILL, SHIELD;
    }
}
