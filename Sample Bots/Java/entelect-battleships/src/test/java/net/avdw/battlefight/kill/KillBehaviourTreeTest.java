package net.avdw.battlefight.kill;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class KillBehaviourTreeTest {

    private static StateModel killState;
    private static StateModel continueKillState;

    @BeforeClass
    public static void setUpClass() throws IOException {
        killState = StateReader.read(new File("src/test/resources/kill-state.json"));
        continueKillState = StateReader.read(new File("src/test/resources/continue-kill.json"));
    }

    @Test
    public void testBasicExecute() {
        Action action = KillBehaviourTree.execute(killState);
        System.out.println(action);
        assertNotEquals("Cannot be an already shot cell", "1,7,3", action.toString());
        assertEquals("First shot is always south cell", "1,7,2", action.toString());
        assertNotEquals(Action.Type.DO_NOTHING, action.type);
        assertNotEquals(Action.Type.SHIELD, action.type);
    }

    @Test
    public void testContinueKill() {
        Action action = KillBehaviourTree.execute(continueKillState);
        System.out.println(action);
        assertEquals("Ship is clearly on row 8", 8, action.point.y);
    }

    @Test
    public void testUnfinishedKillCell() {
        StateModel.OpponentCell[][] map = new StateModel.OpponentCell[14][14];

        StateModel.OpponentCell shotCell = new StateModel.OpponentCell();
        StateModel.OpponentCell missCell = new StateModel.OpponentCell();
        StateModel.OpponentCell emptyCell = new StateModel.OpponentCell();

        shotCell.Y = shotCell.X = 1;
        shotCell.Damaged = missCell.Missed = true;
        shotCell.Missed = missCell.Damaged = emptyCell.Damaged = emptyCell.Missed = false;

        map[2][1] = emptyCell;
        map[1][0] = emptyCell;
        map[1][1] = shotCell;
        map[1][2] = emptyCell;
        map[0][1] = emptyCell;
        assertTrue("Surrounded by empty cells is head and tail.", KillBehaviourTree.killIsUnfinished(map, shotCell));

        map[2][1] = shotCell;
        assertTrue("Only shot above is unfinished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[0][1] = shotCell;
        assertFalse("Shot above and below is finished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[0][1] = map[2][1] = emptyCell;

        map[1][0] = shotCell;
        assertTrue("Only shot left is unfinished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[1][2] = shotCell;
        assertFalse("Shot left and right is finished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[1][0] = map[1][2] = emptyCell;

        map[2][1] = missCell;
        assertTrue("Only missed above is unfinished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[0][1] = map[2][1] = emptyCell;

        map[1][0] = missCell;
        assertTrue("Only missed left is unfinished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[1][0] = map[1][2] = emptyCell;

        map[2][1] = missCell;
        map[0][1] = shotCell;
        assertFalse("Missed above and shot below is finished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[0][1] = map[2][1] = emptyCell;

        map[1][0] = shotCell;
        map[1][2] = missCell;
        assertFalse("Shot left and missed right is finished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[1][0] = map[1][2] = emptyCell;

        map[2][1] = shotCell;
        map[1][2] = shotCell;
        assertTrue("Shot above and right is unfinished.", KillBehaviourTree.killIsUnfinished(map, shotCell));
        map[2][1] = map[1][2] = emptyCell;
    }

    @Test
    public void testTransformMap() {
        StateModel.OpponentCell[][] result = KillBehaviourTree.transformMap(killState.OpponentMap.Cells);
        assertEquals("First element of the map should be equal the list item.", killState.OpponentMap.Cells.get(0), result[0][0]);
    }

}
