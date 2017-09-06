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

public class PotentialFieldTest {

    static StateModel noShipsSunk;
    static StateModel carrierSunk;

    @BeforeClass
    public static void setUpClass() throws IOException {
        noShipsSunk = StateReader.read(new File("src/test/resources/no-ships-sunk.json"), StateModel.class);
        carrierSunk = StateReader.read(new File("src/test/resources/carrier-sunk.json"), StateModel.class);
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
    public void testPotentialAt() {
        PotentialField field = new PotentialField(noShipsSunk);

        assertEquals("A corner should have maximum potential of 20", 20, field.potentialAt(13, 0));
        assertEquals("A corner can only place north should have potential of 10", 10, field.potentialAt(0, 0));
    }

    @Test
    public void testApply() {
        HuntMask mask = new HuntMask(carrierSunk);
        PotentialField field = new PotentialField(carrierSunk);
        assertEquals("3,13 should be 32 with one ship sunk", 32, field.potentialAt(3, 13));
        assertEquals("2,13 should be 30 with one ship sunk", 30, field.potentialAt(2, 13));
        field.apply(mask);
        assertEquals("3,13 should be 32 with mask applied", 32, field.potentialAt(3, 13));
        assertEquals("2,13 should be 0 with mask", 0, field.potentialAt(2, 13));
    }
    
    @Test
    public void testEmptyApply() {
        HuntMask mask = new HuntMask(2);
        StateModel model = StateReader.read(new File("src/test/resources/cannot-find-ship.json"), StateModel.class);
        PotentialField field = new PotentialField(model);
        
        field.apply(mask);
        assertNotEquals("There will always be a potential shot.", 0, field.maxPotential().size());
    }

    @Test
    public void testMaxPotential() {
        HuntMask mask = new HuntMask(carrierSunk);
        PotentialField field = new PotentialField(carrierSunk);
        assertEquals("There should be 28 max points", 28, field.maxPotential().size());
        assertEquals("First found point should be 3,3", "3,3", field.maxPotential().get(0).toString());
        field.apply(mask);
        assertEquals("There should be 8 max points after mask", 8, field.maxPotential().size());
        assertEquals("First found point after mask should be 5,3", "5,3", field.maxPotential().get(0).toString());
    }

}
