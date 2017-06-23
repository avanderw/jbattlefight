package za.co.entelect.challenge.domain.state;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;

public class OpponentMap {

    public boolean Alive;

    public int Points;

    public String Name;

    public ArrayList<OpponentShip> Ships;

    public ArrayList<OpponentCell> Cells;

    public OpponentCell getCellAt(int x, int y) {
        return Cells.stream().filter(c -> c.X == x && c.Y == y).findFirst().get();
    }

    public OpponentCell getAdjacentCell(OpponentCell cell, Direction direction) {
        if (cell == null) {
            return null;
        }
        try {
            switch (direction) {

                case North:
                    return getCellAt(cell.X, cell.Y + 1);
                case East:
                    return getCellAt(cell.X + 1, cell.Y);
                case South:
                    return getCellAt(cell.X, cell.Y - 1);
                case West:
                    return getCellAt(cell.X - 1, cell.Y);
                default:
                    throw new IllegalArgumentException(String.format("The direction passed %s does not exist", direction));
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public ArrayList<OpponentCell> getAllCellsInDirection(Point startLocation, Direction direction, int length) {
        OpponentCell startCell = getCellAt(startLocation.getX(), startLocation.getY());
        ArrayList<OpponentCell> cells = new ArrayList<>();
        cells.add(startCell);

        if (startCell == null) {
            return cells;
        }

        for (int i = 1; i < length; i++) {
            OpponentCell nextCell = getAdjacentCell(startCell, direction);
            if (nextCell == null) {
                throw new IllegalArgumentException("Not enough cells for the requested length");
            }

            cells.add(nextCell);
            startCell = nextCell;
        }

        return cells;
    }

    public boolean allCellsAreUnshot(Point startLocation, Direction direction, int length) {
        OpponentCell startCell = getCellAt(startLocation.getX(), startLocation.getY());

        if (startCell == null || startCell.isShot()) {
            return false;
        }

        for (int i = 1; i < length; i++) {
            OpponentCell nextCell = getAdjacentCell(startCell, direction);
            if (nextCell == null || nextCell.isShot()) {
                return false;
            }
            startCell = nextCell;
        }
        return true;
    }

    public boolean shotAllAdjacent(OpponentCell cell) {
        Boolean shotAll = Boolean.TRUE;

        OpponentCell north = getAdjacentCell(cell, Direction.North);
        OpponentCell east = getAdjacentCell(cell, Direction.East);
        OpponentCell south = getAdjacentCell(cell, Direction.South);
        OpponentCell west = getAdjacentCell(cell, Direction.West);

        shotAll = north == null || shotAll && (north.Damaged || north.Missed);
        shotAll = east == null || shotAll && (east.Damaged || east.Missed);
        shotAll = south == null || shotAll && (south.Damaged || south.Missed);
        shotAll = west == null || shotAll && (west.Damaged || west.Missed);
        return shotAll;
    }

    public OpponentCell getAdjacentUnshot(OpponentCell cell) {
        OpponentCell north = getAdjacentCell(cell, Direction.North);
        OpponentCell east = getAdjacentCell(cell, Direction.East);
        OpponentCell south = getAdjacentCell(cell, Direction.South);
        OpponentCell west = getAdjacentCell(cell, Direction.West);

        if (north != null && !north.isShot()) {
            return north;
        } else if (east != null && !east.isShot()) {
            return east;
        } else if (south != null && !south.isShot()) {
            return south;
        } else if (west != null && !west.isShot()) {
            return west;
        } else {
            throw new NoSuchElementException("No un-shot adjacent cell");
        }
    }

    public OpponentCell getNextShot(OpponentCell cell, Direction direction) {
        OpponentCell nextCell = getAdjacentCell(cell, direction);
        if (nextCell == null || nextCell.Missed) {
            return null;
        }
        
        if (nextCell.Damaged) {
            return getNextShot(nextCell, direction);
        }
        
        return nextCell;
    }
}
