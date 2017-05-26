package za.co.entelect.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;
import za.co.entelect.challenge.domain.state.OpponentCell;

public class Probability {

    GameState state;
    int[][] map;
    int maxProb = 0;

    public Probability(GameState state) {
        this.state = state;

        map = new int[state.MapDimension][state.MapDimension];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                for (ShipType ship : ShipType.values()) {
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.North, ship.length())) {
                        map[y][x]++;
                    }
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.East, ship.length())) {
                        map[y][x]++;
                    }
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.South, ship.length())) {
                        map[y][x]++;
                    }
                    if (state.OpponentMap.allCellsAreUnshot(new Point(x, y), Direction.West, ship.length())) {
                        map[y][x]++;
                    }
                }
                maxProb = Math.max(maxProb, map[y][x]);
            }
        }

        if (maxProb == 0) {
            System.out.println("Max probability 0");
            throw new RuntimeException("Max probability 0");
        }

        String pMap = "";
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                pMap += String.format("%2d,", map[y][x]);

            }
            pMap += "\n";
        }
        System.out.println(pMap);
    }

    OpponentCell bestNeighbour(OpponentCell cell) {
        OpponentCell north = state.OpponentMap.getAdjacentCell(cell, Direction.North);
        OpponentCell east = state.OpponentMap.getAdjacentCell(cell, Direction.East);
        OpponentCell south = state.OpponentMap.getAdjacentCell(cell, Direction.South);
        OpponentCell west = state.OpponentMap.getAdjacentCell(cell, Direction.West);

        OpponentCell bestNeighbour = north;
        int bestProbability = north != null ? map[north.Y][north.X] : 0;
        if (east != null && map[east.Y][east.X] > bestProbability) {
            bestNeighbour = east;
            bestProbability = map[east.Y][east.X];
        }

        if (south != null && map[south.Y][south.X] > bestProbability) {
            bestNeighbour = south;
            bestProbability = map[south.Y][south.X];
        }

        if (west != null && map[west.Y][west.X] > bestProbability) {
            bestNeighbour = west;
            bestProbability = map[west.Y][west.X];
        }

        return bestNeighbour;
    }

    List<Point> bestShots() {
        List<Point> shots = new ArrayList();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (map[y][x] == maxProb) {
                    shots.add(new Point(x, y));
                }
            }
        }

        if (shots.isEmpty()) {
            System.out.println("No best shot");
        }
        return shots;
    }

    Point randomBestShot() {
        List<Point> shots = bestShots();

        if (!shots.isEmpty()) {
            int randIdx = ThreadLocalRandom.current().nextInt(shots.size());
            return shots.get(randIdx);
        } else {
            return null;
        }
    }

}
