package net.avdw.battlefight.place;

import net.avdw.battlefight.Action;

public class PlaceAction extends Action {

    public PlaceAction() {
        super();
        filename = "place.txt";
    }

    @Override
    public String toString() {
        return "Battleship 1 2 North\nCarrier 6 3 West\nCruiser 3 4 North\nDestroyer 4 5 North\nSubmarine 5 6 North";
    }
}
