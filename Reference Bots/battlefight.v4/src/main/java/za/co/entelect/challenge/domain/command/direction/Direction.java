package za.co.entelect.challenge.domain.command.direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
    North,
    East,
    South,
    West;

    public static Direction random() {
        float random = ThreadLocalRandom.current().nextFloat();
        if (random < 0.25) {
            return North;
        } else if (random < 0.50) {
            return East;
        } else if (random < 0.75) {
            return South;
        } else {
            return West;
        }
    }

    public static List<Direction> list() {
        List<Direction> directions = new ArrayList();
        directions.add(North);
        directions.add(East);
        directions.add(South);
        directions.add(West);
        
        return directions;
    }
}
