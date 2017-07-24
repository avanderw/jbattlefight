package za.co.entelect.challenge.domain.command.ship;

public enum ShipType {
    Battleship(4),
    Carrier(5),
    Cruiser(3),
    Destroyer(2),
    Submarine(3);

    private final int length;

    private ShipType(final int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }
}
