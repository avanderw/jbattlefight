package za.co.entelect.challenge;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.state.GameState;
import za.co.entelect.challenge.domain.state.OpponentCell;

public class Kill {

    private final GameState state;

    public Kill(GameState state) {
        this.state = state;
    }

    Command nextShot() {
        return linearSearch();
    }

    Command linearSearch() {
        List<OpponentCell> cells = state.OpponentMap.Cells.stream().filter(c -> {
            return c.Damaged && !state.OpponentMap.shotAllAdjacent(c);
        }).collect(Collectors.toList());

        for (OpponentCell cell : cells) {
            OpponentCell north = state.OpponentMap.getAdjacentCell(cell, Direction.North);
            OpponentCell south = state.OpponentMap.getAdjacentCell(cell, Direction.South);
            OpponentCell east = state.OpponentMap.getAdjacentCell(cell, Direction.East);
            OpponentCell west = state.OpponentMap.getAdjacentCell(cell, Direction.West);
            
            if ((north != null && north.Damaged) || (south != null && south.Damaged)) {
                System.out.println("Attacking north or south of " + cell);
                OpponentCell shot = state.OpponentMap.getNextShot(cell, Direction.North);
                if (shot != null) {
                    return new Command(Code.FIRESHOT, shot.X, shot.Y);
                }

                shot = state.OpponentMap.getNextShot(cell, Direction.South);
                if (shot != null) {
                    return new Command(Code.FIRESHOT, shot.X, shot.Y);
                }
            } else if ((east != null && east.Damaged) || (west != null && west.Damaged)) {
                System.out.println("Attacking east or west of " + cell);
                OpponentCell shot = state.OpponentMap.getNextShot(cell, Direction.East);
                if (shot != null) {
                    return new Command(Code.FIRESHOT, shot.X, shot.Y);
                }

                shot = state.OpponentMap.getNextShot(cell, Direction.West);
                if (shot != null) {
                    return new Command(Code.FIRESHOT, shot.X, shot.Y);
                }
            } else {
                System.out.println("First hit! attacking most probable neighbour");
                OpponentCell shot = new Probability(state).bestNeighbour(cell);
                
                return new Command(Code.FIRESHOT, shot.X, shot.Y);
            }
        }

        System.out.println("All linear attacks made, returning to hunt");
        return completeSearch();
    }

    Command completeSearch() {
        OpponentCell cell = state.OpponentMap.Cells.stream().filter(c -> {
            return c.Damaged && !state.OpponentMap.shotAllAdjacent(c);
        }).findAny().get();

        OpponentCell attack = state.OpponentMap.getAdjacentUnshot(cell);

        System.out.println("Kill around point " + attack);
        return new Command(Code.FIRESHOT, attack.X, attack.Y);
    }
}
