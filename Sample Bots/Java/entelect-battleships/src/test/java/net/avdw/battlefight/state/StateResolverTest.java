/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.state;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author CP318674
 */
public class StateResolverTest {
    
    public StateResolverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        StateResolver.reset();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testHuntState() throws IOException {
        StateModel stateModel = StateReader.read(new File("src/test/resources/no-ships-hit.json"));
        StateResolver.setup(stateModel);
        assertEquals("Should always hunt if no ships are hit.", StateResolver.AiState.HUNT, StateResolver.state);
    }
    
    @Test
    public void testKillState() throws IOException {
        StateModel stateModel = StateReader.read(new File("src/test/resources/no-ships-sunk.json"));
        StateResolver.setup(stateModel);
        assertEquals("Should always kill when ships not sunk.", StateResolver.AiState.KILL, StateResolver.state);
    }
    
}
