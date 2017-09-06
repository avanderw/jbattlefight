package net.avdw.battlefight;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
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
        final StateModel stateModel = StateReader.read(new File("src/test/resources/dont-kill-dead-ships.json"), StateModel.class);
        final StateModel.OpponentCell[][] map = MapQuery.transformMap(stateModel.OpponentMap.Cells);
        Stream<StateModel.OpponentCell> unfinishedKillshots = stateModel.OpponentMap.Cells.stream().filter(cell -> cell.Damaged && MapQuery.killIsUnfinished(map, cell, stateModel.OpponentMap.Ships));
        assertTrue("Do not allow more than one unfinshed-kill shot.", unfinishedKillshots.count() <= 1);
    }

}
