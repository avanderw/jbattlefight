package net.avdw.battlefight.place;

import net.avdw.battlefight.Action;

public class PlaceAction extends Action {

    public PlaceAction() {
        super();
        filename = "place.txt";
    }

    @Override
    public String toString() {
        return "Battleship 0 0 East\nCarrier 7 3 West\nCruiser 3 5 North\nDestroyer 10 5 North\nSubmarine 4 12 West";
    }
}
