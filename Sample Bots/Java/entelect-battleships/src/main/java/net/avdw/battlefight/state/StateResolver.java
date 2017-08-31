package net.avdw.battlefight.state;

import java.util.stream.Stream;

public class StateResolver {

    public static AiState state;

    static void reset() {
        state = null;
    }

    static public void setup(StateModel stateModel) {
        if (stateModel.Phase == 1) {
            state = AiState.PLACE;
        } else {
            Stream<StateModel.OpponentCell> hitCells = stateModel.OpponentMap.Cells.stream().filter((cell) -> cell.Damaged);
            state = (hitCells.count() > 0) ? AiState.KILL : AiState.HUNT;
        }
    }

    public enum AiState {
        PLACE, HUNT, KILL;
    }
}
