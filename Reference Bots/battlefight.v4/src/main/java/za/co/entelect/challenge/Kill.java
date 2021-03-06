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
        System.out.println("Searching for linear kills");
        List<OpponentCell> cells = state.OpponentMap.Cells.stream().filter(c -> {
            return c.Damaged;
        }).collect(Collectors.toList());

        if (!cells.isEmpty()) {
            for (OpponentCell cell : cells) {
                OpponentCell north = state.OpponentMap.getAdjacentCell(cell, Direction.North);
                OpponentCell south = state.OpponentMap.getAdjacentCell(cell, Direction.South);
                OpponentCell east = state.OpponentMap.getAdjacentCell(cell, Direction.East);
                OpponentCell west = state.OpponentMap.getAdjacentCell(cell, Direction.West);

                if (north != null && north.Damaged && west != null && west.Damaged) {
                    System.out.println("hit north and west, searching northwest " + cell);
                    OpponentCell northWest = state.OpponentMap.getAdjacentCell(west, Direction.North);
                    if (!northWest.isShot()) {
                        return new Command(Code.FIRESHOT, northWest.X, northWest.Y);
                    }
                }

                if (south != null && south.Damaged && west != null && west.Damaged) {
                    System.out.println("hit south and west, searching southwest " + cell);
                    OpponentCell southWest = state.OpponentMap.getAdjacentCell(west, Direction.South);
                    if (!southWest.isShot()) {
                        return new Command(Code.FIRESHOT, southWest.X, southWest.Y);
                    }
                }

                if (south != null && south.Damaged && east != null && east.Damaged) {
                    System.out.println("hit south and east, searching southeast " + cell);
                    OpponentCell southEast = state.OpponentMap.getAdjacentCell(east, Direction.South);
                    if (!southEast.isShot()) {
                        return new Command(Code.FIRESHOT, southEast.X, southEast.Y);
                    }
                }

                if (north != null && north.Damaged && east != null && east.Damaged) {
                    System.out.println("hit north and east, searching northeast " + cell);
                    OpponentCell northEast = state.OpponentMap.getAdjacentCell(east, Direction.North);
                    if (!northEast.isShot()) {
                        return new Command(Code.FIRESHOT, northEast.X, northEast.Y);
                    }
                }

                if (north != null && north.Damaged) {
                    System.out.println("hit north, searching south " + cell);
                    OpponentCell shot = state.OpponentMap.getNextShot(cell, Direction.South);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing south, searching north");
                    shot = state.OpponentMap.getNextShot(cell, Direction.North);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing north");

                }

                if (south != null && south.Damaged) {
                    System.out.println("hit south, searching north " + cell);
                    OpponentCell shot = state.OpponentMap.getNextShot(cell, Direction.North);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing north, searching south");
                    shot = state.OpponentMap.getNextShot(cell, Direction.South);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing south");
                }

                if (east != null && east.Damaged) {
                    System.out.println("hit east, searching west " + cell);
                    OpponentCell shot = state.OpponentMap.getNextShot(cell, Direction.West);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing west, searching east");
                    shot = state.OpponentMap.getNextShot(cell, Direction.East);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing east");
                }

                if (west != null && west.Damaged) {
                    System.out.println("hit west, searching east " + cell);
                    OpponentCell shot = state.OpponentMap.getNextShot(cell, Direction.East);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing east, searching west");
                    shot = state.OpponentMap.getNextShot(cell, Direction.West);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }

                    System.out.println("nothing west");
                }

                if ((north == null || !north.Damaged) && (east == null || !east.Damaged) && (south == null || !south.Damaged) && (west == null || !west.Damaged)) {
                    System.out.println("First hit! attacking most probable neighbour");
                    OpponentCell shot = new Probability(state).bestNeighbour(cell);
                    if (shot != null) {
                        return new Command(Code.FIRESHOT, shot.X, shot.Y);
                    }
                }
            }
        }

        System.out.println("no attack found");
        return new Command(Code.DO_NOTHING, 0, 0);
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
