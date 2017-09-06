/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.state;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew
 */
public class StateReaderTest {

    public StateReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = PersistentModel.ActionType.FIRESHOT;
        
        StateWriter.write("persistent.json", persist);
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
    public void testRead() throws Exception {
        final StateModel state = StateReader.read(new File("src/test/resources/dont-kill-dead-ships.json"), StateModel.class);
        final PersistentModel persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
        assertNotNull("State file should not read null.", state);
        assertNotNull("Persist file should not read null.", persist);
        assertEquals("Action type should resolve to enum.", PersistentModel.ActionType.FIRESHOT, persist.lastAction.type);
    }

}
