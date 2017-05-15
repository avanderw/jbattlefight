package za.co.entelect.challenge;

import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.state.GameState;
import za.co.entelect.challenge.domain.state.OpponentCell;

public class Kill {

    private final GameState state;

    public Kill(GameState state) {
        this.state = state;
    }

    Command nextShot() {
        OpponentCell cell = state.OpponentMap.Cells.stream().filter(c -> {
            return c.Damaged && !state.OpponentMap.shotAllAdjacent(c);
        }).findAny().get();

        OpponentCell attack = state.OpponentMap.getAdjacentUnshot(cell);
        return new Command(Code.FIRESHOT, attack.X, attack.Y);
    }
}
