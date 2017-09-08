package net.avdw.battlefight.struct;

import org.junit.Test;
import static org.junit.Assert.*;

public class ActionTest {

    @Test
    public void testActionTypes() {
        assertEquals(0, Action.Type.DO_NOTHING.ordinal());
        assertEquals(1, Action.Type.FIRESHOT.ordinal());
        assertEquals(2, Action.Type.DOUBLE_SHOT_VERTICAL.ordinal());
        assertEquals(3, Action.Type.DOUBLE_SHOT_HORIZONTAL.ordinal());
        assertEquals(4, Action.Type.CORNER_SHOT.ordinal());
        assertEquals(5, Action.Type.CROSS_SHOT_DIAGONAL.ordinal());
        assertEquals(6, Action.Type.CROSS_SHOT_HORIZONTAL.ordinal());
        assertEquals(7, Action.Type.SEEKER_MISSILE.ordinal());
        assertEquals(8, Action.Type.SHIELD.ordinal());
    }
    
}
