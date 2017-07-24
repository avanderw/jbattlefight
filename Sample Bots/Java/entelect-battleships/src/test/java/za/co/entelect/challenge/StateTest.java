/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.entelect.challenge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import junit.framework.TestCase;
import net.avdw.battlefight.state.State;
import net.avdw.battlefight.state.StateReader;
import org.junit.Test;

/**
 *
 * @author CP318674
 */
public class StateTest extends TestCase {

    public StateTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testLoad() throws IOException {
        State state = StateReader.read(new File("src/main/resources/state.json"));
        assertNotNull(state);

        assertNotNull(state.PlayerMap);
        assertNotNull(state.PlayerMap.Owner);
        assertNotNull(state.PlayerMap.Cells);
        assertNotNull(state.PlayerMap.MapWidth);
        assertEquals(state.PlayerMap.MapWidth, 14);
        assertNotNull(state.PlayerMap.MapHeight);
        assertEquals(state.PlayerMap.MapHeight, 14);

        assertNotNull(state.OpponentMap);
        assertNotNull(state.GameVersion);
        assertEquals(state.GameVersion, "1.0.0");
        assertNotNull(state.GameLevel);
        assertEquals(state.GameLevel, 1);
        assertNotNull(state.Round);
        assertEquals(state.Round, 1);
        assertNotNull(state.MapDimension);
        assertEquals(state.MapDimension, 14);
        assertNotNull(state.Phase);
        assertEquals(state.Phase, 2);

    }
}
