package net.avdw.battlefight.hunt;

import java.io.File;
import java.io.IOException;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;
import org.junit.Test;
import static org.junit.Assert.*;

public class HuntBehaviourTreeTest {

    @Test
    public void testEmptyBoard() throws IOException {
        assertEquals("Empty board first hit will be for the Battleship at 7,5.",
                new HuntAction(new Point(7, 5)).toString(),
                HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/no-ships-hit.json"))).toString());
    }
        
    @Test
    public void testHuntMaskSwitch() throws IOException {
        Action huntBattleship = HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/no-ships-hit.json")));
        Action huntDestroyer = HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/cruiser-left.json")));
        
        HuntMask battleship = new HuntMask(4);
        HuntMask destroyer = new HuntMask(2);
        
        assertNotNull(battleship.mask);
        assertNotNull(destroyer.mask);
        
        assertTrue("After battleship sunk, search for destroyer.", destroyer.mask[huntDestroyer.point.y][huntDestroyer.point.x]);
        assertTrue("Search battleship before carrier.", battleship.mask[huntBattleship.point.y][huntBattleship.point.x]);
    }

}
