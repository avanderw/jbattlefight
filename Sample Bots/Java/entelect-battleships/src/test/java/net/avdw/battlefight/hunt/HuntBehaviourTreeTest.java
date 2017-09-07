package net.avdw.battlefight.hunt;

import java.io.File;
import java.io.IOException;
import net.avdw.battlefight.MapQuery;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Point;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class HuntBehaviourTreeTest {

    @Test
    @Ignore
    public void testEmptyBoard() throws IOException {
        assertEquals("Empty board first hit will be for the Battleship at 7,5.",
                new HuntAction(new Point(7, 5)).toString(),
                HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/no-ships-hit.json"), StateModel.class)).toString());
    }
        
    @Test
    public void testHuntMaskSwitch() throws IOException {
        Action huntBattleship = HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/no-ships-hit.json"), StateModel.class));
        Action huntDestroyer = HuntBehaviourTree.execute(StateReader.read(new File("src/test/resources/cruiser-left.json"), StateModel.class));
        
        HuntMask battleship = new HuntMask(4);
        HuntMask destroyer = new HuntMask(2);
        
        assertNotNull(battleship.mask);
        assertNotNull(destroyer.mask);
        
        assertTrue("After battleship sunk, search for destroyer.", destroyer.mask[huntDestroyer.point.y][huntDestroyer.point.x]);
        assertTrue("Search battleship before carrier.", battleship.mask[huntBattleship.point.y][huntBattleship.point.x]);
    }
    
    @Test
    public void testCannotFindShip() throws IOException {
        StateModel model = StateReader.read(new File("src/test/resources/cannot-find-ship.json"), StateModel.class);
        StateModel.OpponentCell[][] map = MapQuery.transformMap(model.OpponentMap.Cells);
        MapQuery.printMap(map);
        
        Action action = HuntBehaviourTree.execute(model);
        assertNotEquals("Don't make illegal shots.", "1,0,0", action.toString());
        System.out.println(action);
    }
    
    @Test
    @Ignore("Good to shoot diagnals")
    public void testSpreadHunt() {
        StateModel model = StateReader.read(new File("src/test/resources/hunt/spread-hunt.json"), StateModel.class);
        Action action = HuntBehaviourTree.execute(model);
        assertNotEquals("Spread shots! Update potential field correctly.", "1,6,6", action.toString());
    }

}
