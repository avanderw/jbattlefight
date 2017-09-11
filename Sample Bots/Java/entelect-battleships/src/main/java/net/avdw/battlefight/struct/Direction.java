package net.avdw.battlefight.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
    North, South, East, West;
    
    static public Direction random() {
        return Direction.values()[ThreadLocalRandom.current().nextInt(4)];
    }
    
    static public Direction random(Direction excl) {
        List<Direction> all = new ArrayList();
        all.add(North);
        all.add(South);
        all.add(East);
        all.add(West);
        all.remove(excl);
        
        return all.get(ThreadLocalRandom.current().nextInt(3));
    }
}
