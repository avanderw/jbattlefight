package net.avdw.battlefight;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Direction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        lastAction.type = PersistentModel.ActionType.FIRESHOT;
        lastAction.x = 10;
        lastAction.y = 10;

        final StateModel stateModel = StateReader.read(new File("src/test/resources/dont-kill-dead-ships.json"), StateModel.class);
        final StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        Stream<StateModel.OpponentCell> unfinishedKillshots = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && MapQuery.killIsUnfinished(map, cell, lastAction));
        assertEquals("Do not allow more than two unfinshed-kill shots.", 1, unfinishedKillshots.count());
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
}
