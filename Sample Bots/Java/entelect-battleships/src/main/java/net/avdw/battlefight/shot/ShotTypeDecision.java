package net.avdw.battlefight.shot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateModel.OpponentShip;
import net.avdw.battlefight.state.StateModel.Ship;
import net.avdw.battlefight.struct.Action.Type;

public class ShotTypeDecision {

    static public Type huntShot(StateModel state) {

        List<Ship> shipsThatCanFire = state.PlayerMap.Owner.Ships.stream()
                .filter(ship -> !ship.Destroyed && ship.ShipType != StateModel.ShipType.Cruiser
                && ship.Weapons.get(1).EnergyRequired < state.PlayerMap.Owner.Energy
                && ship.Cells.stream().filter(cell -> cell.Hit).count() < ship.ShipType.length() - 1)
                .sorted((ship1, ship2) -> {
                    return ship2.Weapons.get(1).EnergyRequired - ship1.Weapons.get(1).EnergyRequired;
                }).collect(Collectors.toList());

        if (shipsThatCanFire.isEmpty()) {
            if (state.PlayerMap.Owner.Ships.stream().filter(ship -> !ship.Destroyed).count() == 1) {
                if (state.PlayerMap.Owner.Ships.stream().anyMatch(ship -> !ship.Destroyed && ship.ShipType == StateModel.ShipType.Cruiser)) {
                    return Type.CROSS_SHOT_HORIZONTAL;
                } else {
                    return Type.FIRESHOT;
                }
            } else {
                return Type.FIRESHOT;
            }
        }

        Optional<Ship> priorityShip = shipsThatCanFire.stream().filter(ship -> ship.Cells.stream().anyMatch(cell -> cell.Hit)).findAny();
        Ship firingShip = null;
        if (shipsThatCanFire.get(0).ShipType == StateModel.ShipType.Submarine && shipsThatCanFire.stream().anyMatch(ship -> ship.ShipType == StateModel.ShipType.Battleship) && !priorityShip.isPresent()) {
            return Type.CROSS_SHOT_DIAGONAL;
        } else {
            firingShip = shipsThatCanFire.get(0);
            if (firingShip.ShipType == StateModel.ShipType.Destroyer
                    && state.PlayerMap.Owner.Ships.stream().anyMatch(ship -> !ship.Destroyed && ship.ShipType == StateModel.ShipType.Cruiser
                    && ship.Weapons.get(1).EnergyRequired < state.PlayerMap.Owner.Energy)) {
                firingShip = state.PlayerMap.Owner.Ships.stream().filter(ship -> !ship.Destroyed && ship.ShipType == StateModel.ShipType.Cruiser).findAny().get();
            }
        }

        if (priorityShip.isPresent() && !(priorityShip.get().ShipType == StateModel.ShipType.Carrier && state.PlayerMap.Owner.Ships.stream().anyMatch(ship -> ship.ShipType == StateModel.ShipType.Battleship && ship.Destroyed == false))) {
            firingShip = priorityShip.get();
        } else {
            Ship waitForShip = state.PlayerMap.Owner.Ships.stream()
                    .filter(ship -> !ship.Destroyed && ship.ShipType != StateModel.ShipType.Cruiser
                    && ship.Cells.stream().filter(cell -> cell.Hit).count() == 0)
                    .sorted((ship1, ship2) -> {
                        return ship2.Weapons.get(1).EnergyRequired - ship1.Weapons.get(1).EnergyRequired;
                    }).findFirst().get();

            if (firingShip.ShipType != waitForShip.ShipType) {
                return Type.FIRESHOT;
            }
        }

        System.out.println("firing:" + firingShip);
        switch (firingShip.ShipType) {
            case Carrier:
                return Type.CORNER_SHOT;
            case Battleship:
                return Type.CROSS_SHOT_DIAGONAL;
            case Destroyer:
                return Type.DOUBLE_SHOT_HORIZONTAL;
            case Cruiser:
                return Type.CROSS_SHOT_HORIZONTAL;
            case Submarine:
                return Type.SEEKER_MISSILE;
        }

        return Type.FIRESHOT;
    }

    static public Type killShot(StateModel state) {
        return Type.FIRESHOT;
    }
}
