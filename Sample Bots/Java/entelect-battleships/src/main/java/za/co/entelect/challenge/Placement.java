package za.co.entelect.challenge;

import java.util.Collections;
import java.util.List;
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
        state.place(this);
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", new Object[]{type, point, direction});
    }

    public Placement group(ShipType shipType, Point start) {
        this.type = shipType;

        Point point = start;
        while (!state.canPlace(point)) {
            switch (Direction.random()) {
                case North:
                    if (point.y + 1 < state.MapDimension) {
                        point.y++;
                    }
                    break;
                case East:
                    if (point.x + 1 < state.MapDimension) {
                        point.x++;
                    }
                    break;
                case South:
                    if (point.y - 1 >= 0) {
                        point.y--;
                    }
                    break;
                case West:
                    if (point.x - 1 >= 0) {
                        point.x--;
                    }
                    break;
            }
        }

        this.point = new Point(point.x, point.y);
        List<Direction> directions = Direction.list();
        Collections.shuffle(directions);
        while (!directions.isEmpty()) {
            this.direction = directions.remove(0);
            if (state.canPlace(this)) {
                state.place(this);
                return this;
            }
        }

        return group(shipType, this.point);
    }

    public Placement avoid(ShipType shipType) {
        this.type = shipType;
        while (!state.canPlace(this)) {
            this.point = new Point(ThreadLocalRandom.current().nextInt(state.MapDimension), ThreadLocalRandom.current().nextInt(state.MapDimension));
            this.direction = Direction.random();
        }

        if (!state.secludedPlace(this)) {
            this.point = new Point(ThreadLocalRandom.current().nextInt(state.MapDimension), ThreadLocalRandom.current().nextInt(state.MapDimension));
            this.direction = Direction.random();
            return avoid(shipType);
        }

        state.place(this);
        return this;
    }
}
