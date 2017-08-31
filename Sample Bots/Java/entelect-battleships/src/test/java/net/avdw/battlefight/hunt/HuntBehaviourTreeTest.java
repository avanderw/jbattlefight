package net.avdw.battlefight.hunt;

import java.io.File;
import java.io.IOException;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Point;
import org.junit.Test;
import static org.junit.Assert.*;

public class HuntBehaviourTreeTest {

    @Test
    public void testEmptyBoard() throws IOException {
        assertEquals("Empty board first hunt will be 5,5.",
                new HuntAction(new Point(5, 5)).toString(),
                HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/no-ships-hit.json"))).toString());
    }

}
