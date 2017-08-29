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
import org.junit.Ignore;

public class PotentialFieldTest {
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
    public void testPotentialAt() {
        PotentialField field = new PotentialField(stateModel);
        
        assertEquals("A corner should have maximum potential of 10", 10, field.potentialAt(13, 0));
        assertEquals("A corner can only place north should have potential of 5", 5, field.potentialAt(0, 0));
    }

    @Ignore
    @Test
    public void testApply() {
        System.out.println("apply");
        HuntMask mask = null;
        PotentialField instance = null;
        instance.apply(mask);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
