package net.avdw.battlefight;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Direction;
import net.avdw.battlefight.struct.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class MapQueryTest {

    public MapQueryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testKillIsUnfinished() throws IOException {
        PersistentModel.Action lastAction = new PersistentModel.Action();
        lastAction.type = Action.Type.FIRESHOT;
        lastAction.x = 10;
        lastAction.y = 10;
        PersistentModel persist = new PersistentModel();
        persist.lastAction = lastAction;

        StateModel stateModel = StateReader.read(new File("src/test/resources/dont-kill-dead-ships.json"), StateModel.class);
        final StateModel.OpponentCell[][] map1 = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        MapQuery.printMap(map1);
        persist.unclearedHits = new ArrayList();
        persist.unclearedHits.add(new Point(10, 10));
        Stream<StateModel.OpponentCell> unfinishedKillshots = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && MapQuery.killIsUnfinished(map1, cell, persist));
        assertEquals("Do not allow more than one unfinshed-kill shots.", 1, unfinishedKillshots.count());

        stateModel = StateReader.read(new File("src/test/resources/bug/not-making-shot.json"), StateModel.class);
        final StateModel.OpponentCell[][] map2 = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        MapQuery.printMap(map2);
        StateModel.OpponentCell cell = new StateModel.OpponentCell();
        cell.Y = 10;
        cell.X = 0;
        lastAction.x = 0;
        lastAction.y = 11;
        assertFalse("This is not an unfinished kill.", MapQuery.killIsUnfinished(map2, cell, persist));
        lastAction.y = 10;
        cell.X = 1;
        persist.unclearedHits.clear();
        persist.unclearedHits.add(new Point(1, 10));
        assertTrue("This is an unfinished kill.", MapQuery.killIsUnfinished(map2, cell, persist));
    }

    @Test
    public void testShipCanFit() {
        final StateModel stateModel = StateReader.read(new File("src/test/resources/no-ships-sunk.json"), StateModel.class);
        StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        MapQuery.printMap(map);

        assertTrue("Ship can fit on an empty map.", MapQuery.shipCanFit(map, 0, 0, 2, Direction.North));
        assertTrue("Ship can fit in reverse.", MapQuery.shipCanFit(map, 0, 1, 2, Direction.South));
        assertFalse("Ship cannot fit over another ship.", MapQuery.shipCanFit(map, 0, 0, 2, Direction.East));
        assertFalse("Ship can not fit off an empty map.", MapQuery.shipCanFit(map, 0, 0, 2, Direction.West));

        assertFalse("Ship can't start on a damaged space", MapQuery.shipCanFit(map, 4, 9, 2, Direction.North));
        assertFalse("Ship can't start on a missed space", MapQuery.shipCanFit(map, 5, 7, 2, Direction.North));
        assertFalse("Ship cannot fit over a shot space", MapQuery.shipCanFit(map, 4, 8, 2, Direction.North));
    }

    @Test
    public void testCountShipFit() {
        final StateModel stateModel = StateReader.read(new File("src/test/resources/no-ships-sunk.json"), StateModel.class);
        assertEquals("Ship can only fit in two space at the corner of an empty map", 2, MapQuery.countShipFit(stateModel.OpponentMap, 13, 0, StateModel.ShipType.Destroyer));
        assertEquals("Ship can't start on a damaged space", 0, MapQuery.countShipFit(stateModel.OpponentMap, 4, 9, StateModel.ShipType.Destroyer));
        assertEquals("Ship can't start on a missed space", 0, MapQuery.countShipFit(stateModel.OpponentMap, 5, 7, StateModel.ShipType.Destroyer));
        assertEquals("Ship cannot fit over a shot space", 3, MapQuery.countShipFit(stateModel.OpponentMap, 4, 8, StateModel.ShipType.Destroyer));
    }

    @Test
    @Ignore("Consider enabling when testing new method for determining kill shots")
    public void testV6NotKilling() {
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-not-killing.json"), StateModel.class);
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.CORNER_SHOT;
        persist.lastAction.x = 4;
        persist.lastAction.y = 10;
        persist.lastHeading = null;
        
        final StateModel.OpponentCell[][] map = MapQuery.transformMap(state.OpponentMap.Cells);
        MapQuery.printMap(map);
        
        Stream<StateModel.OpponentCell> unfinishedKillshots = state.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && MapQuery.killIsUnfinished(map, cell, persist));
        assertEquals("Should identify kill shot.", 1, unfinishedKillshots.count());
        
    }
}
