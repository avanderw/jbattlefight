package net.avdw.battlefight.hunt;

import net.avdw.battlefight.state.StateModel;

class MapQuery {
    static int countShipFit(StateModel.OpponentMap map, final int x, final int y, StateModel.ShipType ship) {
        int fit = 0;
        if (map.Cells.stream().anyMatch(cell -> (cell.X == x && cell.Y == y && !(cell.Damaged || cell.Missed)))) {
            if (y + ship.length() < 14 && map.Cells.stream().filter(cell -> cell.Y >= y && cell.Y < y + ship.length() && cell.X == x && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }

            if (y - ship.length() >= 0 && map.Cells.stream().filter(cell -> cell.Y <= y && cell.Y > y - ship.length() && cell.X == x && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }

            if (x + ship.length() < 14 && map.Cells.stream().filter(cell -> cell.X >= x && cell.X < x + ship.length() && cell.Y == y && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }

            if (x - ship.length() >= 0 && map.Cells.stream().filter(cell -> cell.X <= x && cell.X > x - ship.length() && cell.Y == y && !(cell.Damaged || cell.Missed)).count() == ship.length()) {
                fit++;
            }
        }
        return fit;
    }
}
