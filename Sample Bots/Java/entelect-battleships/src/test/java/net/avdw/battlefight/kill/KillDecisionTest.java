package net.avdw.battlefight.kill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import net.avdw.battlefight.MapQuery;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Direction;
import net.avdw.battlefight.struct.Point;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KillDecisionTest {

    private static StateModel killState;
    private static StateModel continueKillState;
    private static StateModel finishKillState;
    private static StateModel continueHuntState;

    @BeforeClass
    public static void setUpClass() throws IOException {
        killState = StateReader.read(new File("src/test/resources/kill-state.json"), StateModel.class);
        continueKillState = StateReader.read(new File("src/test/resources/continue-kill.json"), StateModel.class);
        finishKillState = StateReader.read(new File("src/test/resources/finish-kill.json"), StateModel.class);
        continueHuntState = StateReader.read(new File("src/test/resources/continue-hunt-after-kill.json"), StateModel.class);
    }

    @Test
    public void testBasicExecute() {
        PersistentModel.Action lastAction = new PersistentModel.Action();
        lastAction.type = Action.Type.FIRESHOT;
        lastAction.x = 7;
        lastAction.y = 3;

        PersistentModel model = new PersistentModel();
        model.lastAction = lastAction;
        model.lastHeading = Direction.North;

        Action action = KillDecision.execute(killState, model);
        System.out.println(action);
        assertNotEquals("Cannot be an already shot cell", "1,7,3", action.toString());
        assertNotEquals(Action.Type.DO_NOTHING, action.type);
        assertNotEquals(Action.Type.SHIELD, action.type);
    }

    @Test
    public void testContinueKill() {
        PersistentModel.Action lastAction = new PersistentModel.Action();
        lastAction.type = Action.Type.FIRESHOT;
        lastAction.x = 2;
        lastAction.y = 8;

        PersistentModel model = new PersistentModel();
        model.lastAction = lastAction;
        model.lastHeading = Direction.East;

        Action action = KillDecision.execute(continueKillState, model);
        assertEquals("Ship is clearly on row 8", 8, action.point.y);
    }

    @Test
    public void testContinueHunt() {
        PersistentModel.Action lastAction = new PersistentModel.Action();
        lastAction.type = Action.Type.FIRESHOT;
        lastAction.x = 7;
        lastAction.y = 2;

        PersistentModel persist = new PersistentModel();
        persist.lastAction = lastAction;
        persist.lastHeading = Direction.North;
        persist.unclearedHits = new ArrayList();

        Action action = KillDecision.execute(continueHuntState, persist);
        assertNull(action);
    }

    @Test
    public void testUnfinishedKillCell() throws IOException {
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
        assertTrue("Surrounded by empty cells is head and tail.", MapQuery.killIsUnfinished(map, shotCell, null));

        map[2][1] = shotCell;
        assertTrue("Only shot above is unfinished.", MapQuery.killIsUnfinished(map, shotCell, null));
        map[0][1] = shotCell;
        map[0][1] = map[2][1] = emptyCell;

        map[1][0] = shotCell;
        assertTrue("Only shot left is unfinished.", MapQuery.killIsUnfinished(map, shotCell, null));
        map[1][2] = shotCell;
        map[1][0] = map[1][2] = emptyCell;

        map[2][1] = missCell;
        assertTrue("Only missed above is unfinished.", MapQuery.killIsUnfinished(map, shotCell, null));
        map[0][1] = map[2][1] = emptyCell;

        map[1][0] = missCell;
        assertTrue("Only missed left is unfinished.", MapQuery.killIsUnfinished(map, shotCell, null));
        map[1][0] = map[1][2] = emptyCell;

        map[2][1] = missCell;
        map[0][1] = shotCell;
        map[0][1] = map[2][1] = emptyCell;

        map[1][0] = shotCell;
        map[1][2] = missCell;
        map[1][0] = map[1][2] = emptyCell;

        map[2][1] = shotCell;
        map[1][2] = shotCell;
        assertTrue("Shot above and right is unfinished.", MapQuery.killIsUnfinished(map, shotCell, null));
        map[2][1] = map[1][2] = emptyCell;
    }

    @Test
    public void testTransformMap() {
        StateModel.OpponentCell[][] result = MapQuery.transformMap(killState.OpponentMap.Cells);
        assertEquals("First element of the map should be equal the list item.", killState.OpponentMap.Cells.get(0), result[0][0]);
    }

    @Test
    public void testBoundsCheck() throws IOException {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.x = 13;
        persist.lastAction.y = 2;
        persist.lastHeading = Direction.North;
        persist.unclearedHits = new ArrayList();
        persist.unclearedHits.add(new Point(13, 2));
        assertNotNull(KillDecision.execute(StateReader.read(new File("src/test/resources/bounds-check.json"), StateModel.class), persist));
        persist.lastAction.y = 1;
        assertNotNull(KillDecision.execute(StateReader.read(new File("src/test/resources/bounds-check-axis.json"), StateModel.class), persist));
    }

    @Test
    public void testKillingDeadShips() throws IOException {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.y = 10;
        persist.lastAction.x = 10;
        persist.lastHeading = Direction.North;

        Action action = KillDecision.execute(StateReader.read(new File("src/test/resources/dont-kill-dead-ships.json"), StateModel.class), persist);
        assertNotEquals("Row should be 9, 10, 11.", 5, action.point.y);
        assertNotEquals("Row should be 9, 10, 11.", 4, action.point.y);
        assertNotEquals("Row should be 9, 10, 11.", 3, action.point.y);

        persist.lastAction.y = 4;
        action = KillDecision.execute(StateReader.read(new File("src/test/resources/dont-kill-dead-ships-again.json"), StateModel.class), persist);
        assertNotEquals("Row should be 3, 4, 5.", 10, action.point.y);
        assertNotEquals("Row should be 3, 4, 5.", 7, action.point.y);
    }

    @Test
    public void killReturningNull() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.y = 10;
        persist.lastAction.x = 0;
        persist.lastHeading = Direction.North;

        Action action = KillDecision.execute(StateReader.read(new File("src/test/resources/bug/not-making-shot.json"), StateModel.class), persist);
        assertNotNull(action);
        assertNotNull(action.type);
        assertNotNull(action.point);
        System.out.println(action.toString());
    }

    @Test
    public void notKillingLastShip() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 12;
        persist.lastAction.y = 3;
        persist.lastHeading = Direction.North;
        persist.unclearedHits = new ArrayList();
        persist.unclearedHits.add(new Point(12, 4));
        StateModel state = StateReader.read(new File("src/test/resources/bug/not-killing-last-ship.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);

        assertTrue("Must be close to last shot.", 12 - action.point.x < 2);
    }

    @Test
    public void testV5Bug() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 5;
        persist.lastAction.y = 1;
        persist.lastHeading = Direction.East;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v5-didnt-kill.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertEquals("Should kill the final ship.", "1,6,1", action.toString());
    }

    @Test
    public void testNotReversing() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 13;
        persist.lastAction.y = 1;
        persist.lastHeading = Direction.South;
        persist.unclearedHits = new ArrayList();
        persist.unclearedHits.add(new Point(13,2));
        persist.unclearedHits.add(new Point(13,3));
        StateModel state = StateReader.read(new File("src/test/resources/bug/not-reversing.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertNotNull(action);
        assertEquals("Should kill the ship.", "1,13,4", action.toString());
    }

    @Test
    public void testV6NotFinishing() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 11;
        persist.lastAction.y = 1;
        persist.lastHeading = Direction.East;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-not-finishing.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertEquals("Should finish with 12,1.", "1,12,1", action.toString());
    }

    @Test
    public void testV6NotKilling() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.CORNER_SHOT;
        persist.lastAction.x = 4;
        persist.lastAction.y = 10;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-not-killing.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertEquals("Should finish.", "1,5,12", action.toString());
    }

    @Test
    public void testV6120527() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 4;
        persist.lastAction.y = 8;
        persist.lastHeading = null;
        persist.unclearedHits = new ArrayList();
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-12-05-27.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertNull("Wrong state.", action);
    }

    @Test
    public void testV6120547() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.CROSS_SHOT_DIAGONAL;
        persist.lastAction.x = 6;
        persist.lastAction.y = 2;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-12-05-47.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertEquals("Not finishing kill.", "1,7,0", action.toString());
    }

    @Test
    public void testV6130239() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.CORNER_SHOT;
        persist.lastAction.x = 9;
        persist.lastAction.y = 10;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-02-39.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        assertEquals("Not finishing kill.", "1,10,10", action.toString());
    }

    @Test
    public void testV6130700() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 5;
        persist.lastAction.y = 8;
        persist.lastHeading = null;
        persist.unclearedHits = new ArrayList();
        persist.unclearedHits.add(new Point(6, 8));
        persist.unclearedHits.add(new Point(7, 8));
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-07-00.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        System.out.println(action);
        assertEquals("Not finishing kill.", "1,8,8", action.toString());
    }
    
    @Test
    public void testV6130931() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 13;
        persist.lastAction.y = 4;
        persist.lastHeading = null;
        persist.unclearedHits = new ArrayList();
        persist.unclearedHits.add(new Point(13, 4));
        persist.unclearedHits.add(new Point(13, 5));
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-09-31.json"), StateModel.class);
        Action action = KillDecision.execute(state, persist);
        System.out.println(action);
        assertEquals("Not finishing kill.", "1,13,6", action.toString());
    }
}
