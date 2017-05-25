package za.co.entelect.challenge;

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
    int[][] probabilityMap;

    Hunt(GameState state) {
        this.state = state;

        probabilityMap = new int[state.MapDimension][state.MapDimension];
    }

    Command defaultPlacement() {
        return new DefaultPlacement().hunt(state);
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
        int maxProb = 0;
        for (int y = 0; y < probabilityMap.length; y++) {
            for (int x = 0; x < probabilityMap.length; x++) {
                for (ShipType ship : ShipType.values()) {
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.North, ship.length())) {
                        probabilityMap[y][x]++;
                    }
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.East, ship.length())) {
                        probabilityMap[y][x]++;
                    }
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.South, ship.length())) {
                        probabilityMap[y][x]++;
                    }
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.West, ship.length())) {
                        probabilityMap[y][x]++;
                    }
                }
                maxProb = Math.max(maxProb, probabilityMap[y][x]);
            }
        }

        if (maxProb == 0) {
            System.out.println("Max probability 0");
            throw new RuntimeException("Max probability 0");
            //return randomShot();
        }
        
        List<Point> options = new ArrayList();
        String pMap = "";
        for (int y = 0; y < probabilityMap.length; y++) {
            for (int x = 0; x < probabilityMap.length; x++) {
                pMap += String.format("%2d,", probabilityMap[y][x]);
                if (probabilityMap[y][x] == maxProb) {
                    options.add(new Point(x, y));
                }
            }
            pMap += "\n";
        }
        System.out.println(pMap);
        
        if (options.isEmpty()) {
            System.out.println("No highest probability");
            return randomShot();
        }
        
        int randIdx = ThreadLocalRandom.current().nextInt(options.size());
        return new Command(Code.FIRESHOT, options.get(randIdx).x, options.get(randIdx).y);
    }

}
