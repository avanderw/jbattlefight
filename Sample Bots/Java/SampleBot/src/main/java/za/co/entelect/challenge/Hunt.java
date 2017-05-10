package za.co.entelect.challenge;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;
import za.co.entelect.challenge.domain.state.OpponentCell;
import za.co.entelect.challenge.domain.state.Ship;

public class Hunt {

    public static void main(String[] args) {
        for (int y = 10; y >= 0; y--) {
            for (int x = 0; x <= 10; x++) {
                System.out.print((y % 2 == 0 && x % 2 == 0) || (x % 2 == 1 && y % 2 == 1) ? "x" : "o");
            }
            System.out.println("");
        }
    }
    private final GameState state;

    Hunt(GameState state) {
        this.state = state;
    }

    Command randomShot() {
        Optional<OpponentCell> cell = state.OpponentMap.Cells.stream()
                .filter(c -> !c.Damaged && !c.Missed && ((c.Y % 2 == 0 && c.X % 2 == 0) || (c.X % 2 == 1 && c.Y % 2 == 1)))
                .findAny();

        Command command = cell.isPresent() ? new Command(Code.FIRESHOT, cell.get().X, cell.get().Y) : new Command(Code.DO_NOTHING, 0, 0);
        return command;
    }

}
