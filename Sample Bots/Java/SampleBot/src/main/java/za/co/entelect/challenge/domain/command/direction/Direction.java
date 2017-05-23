package za.co.entelect.challenge.domain.command.direction;

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
}
