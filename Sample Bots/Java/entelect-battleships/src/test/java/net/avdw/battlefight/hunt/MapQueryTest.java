package net.avdw.battlefight.hunt;

import java.io.File;
import java.io.IOException;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapQueryTest {
    static StateModel stateModel;
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        stateModel = StateReader.read(new File("src/test/resources/no-ships-sunk.json"));
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
    public void testCountShipFit() {
        assertEquals("Ship can only fit in two space at the corner of an empty map", 2, MapQuery.countShipFit(stateModel.OpponentMap, 13, 0, StateModel.ShipType.Destroyer));
        assertEquals("Ship can't start on a shot space", 0, MapQuery.countShipFit(stateModel.OpponentMap, 4, 9, StateModel.ShipType.Destroyer));
        assertEquals("Ship can't start on a shot space", 0, MapQuery.countShipFit(stateModel.OpponentMap, 5, 7, StateModel.ShipType.Destroyer));
        assertEquals("Ship cannot fit over a shot space", 3, MapQuery.countShipFit(stateModel.OpponentMap, 4, 8, StateModel.ShipType.Destroyer));
    }
    
}
