package net.avdw.battlefight.state;

import java.util.List;
import java.util.stream.Stream;
import net.avdw.battlefight.MapQuery;

public class StateResolver {

    public static AiState state;

    static void reset() {
        state = null;
    }

    static public void setup(StateModel stateModel) {
        if (stateModel.Phase == 1) {
            state = AiState.PLACE;
        } else {
            final StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
            Stream<StateModel.OpponentCell> unfinishedCells = stateModel.OpponentMap.Cells.stream().filter((cell) -> cell.Damaged && MapQuery.killIsUnfinished(map, cell));
            state = (unfinishedCells.count() > 0) ? AiState.KILL : AiState.HUNT;
        }
    }
    


    public enum AiState {
        PLACE, HUNT, KILL;
    }
}
