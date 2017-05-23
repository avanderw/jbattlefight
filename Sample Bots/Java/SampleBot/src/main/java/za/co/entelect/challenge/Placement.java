package za.co.entelect.challenge;

import java.util.concurrent.ThreadLocalRandom;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;

public class Placement {

    public ShipType type;
    GameState state;
    public Point point;
    public Direction direction;

    public Placement(GameState state) {
        this.state = state;
    }

    public Placement random(ShipType shipType) {
        this.type = shipType;

        int randX = ThreadLocalRandom.current().nextInt(state.MapDimension);
        int randY = ThreadLocalRandom.current().nextInt(state.MapDimension);

        while (point == null && direction == null) {
            point = new Point(randX, randY);
            direction = Direction.random();

            boolean clear = false;
            switch (direction) {
                case North:
                    clear = (randY + type.length() >= state.MapDimension && state.canPlace(this));
                    break;
                case East:
                    clear = (randX + type.length() >= state.MapDimension && state.canPlace(this));
                    break;
                case South:
                    clear = (randY - type.length() < 0 && state.canPlace(this));
                    break;
                case West:
                    clear = (randX - type.length() < 0 && state.canPlace(this));
                    break;
            }
            if (clear) {
                point = null;
                direction = null;
            }
        }
        state.place(this);
        return this;
    }

    public Placement fixed(ShipType shipType, Point point, Direction direction) {
        this.type = shipType;
        this.point = point;
        this.direction = direction;
        return this;
    }

}
