package net.avdw.battlefight.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
    North, South, East, West;
    
    static public Direction random() {
        return Direction.values()[ThreadLocalRandom.current().nextInt(4)];
    }
}
