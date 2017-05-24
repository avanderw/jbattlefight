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

        long start = System.currentTimeMillis();
        while (point == null && direction == null) {
            point = new Point(ThreadLocalRandom.current().nextInt(state.MapDimension), ThreadLocalRandom.current().nextInt(state.MapDimension));
            direction = Direction.random();

            boolean clear = false;
            switch (direction) {
                case North:
                    clear = (point.y + type.length() >= state.MapDimension);
                    if (!clear) {
                        clear = !state.canPlace(this);
                    }
                    break;
                case East:
                    clear = (point.x + type.length() >= state.MapDimension);
                    if (!clear) {
                        clear = !state.canPlace(this);
                    }
                    break;
                case South:
                    clear = (point.y - type.length() < 0);
                    if (!clear) {
                        clear = !state.canPlace(this);
                    }
                    break;
                case West:
                    clear = (point.x - type.length() < 0);
                    if (!clear) {
                        clear = !state.canPlace(this);
                    }
                    break;
            }
//            if (System.currentTimeMillis() - start > 2000) {
//                System.out.println(state.debugPlace());
//                System.out.println(point);
//                System.out.println(direction);
//            }
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
    
    @Override
    public String toString() {
        return String.format("%s %s %s", new Object[]{type, point, direction});
    }
}
