package za.co.entelect.challenge;

import za.co.entelect.challenge.strategy.DefaultPlacementAttackStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;
import za.co.entelect.challenge.domain.state.OpponentCell;

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

    Command defaultPlacement() {
        return new DefaultPlacementAttackStrategy().hunt(state);
    }

    Command randomShot() {
        System.out.println("Making random shot");
        List<OpponentCell> emptyCells = state.OpponentMap.Cells.stream().filter(c -> !c.Damaged && !c.Missed && ((c.Y % 2 == 0 && c.X % 2 == 0) || (c.X % 2 == 1 && c.Y % 2 == 1))).collect(Collectors.toList());
        if (emptyCells.isEmpty()) {
            return new Command(Code.DO_NOTHING, 0, 0);
        } else {
            OpponentCell randomEmptyCell = emptyCells.get(ThreadLocalRandom.current().nextInt(0, (int) emptyCells.size()));
            return new Command(Code.FIRESHOT, randomEmptyCell.X, randomEmptyCell.Y);
        }
    }

    Command probabilityShot() {
        System.out.println("Making probability shot");
        Point shot = new Probability(state).randomBestShot();
        
        return new Command(Code.FIRESHOT, shot.x, shot.y);
    }

}
