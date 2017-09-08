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

    @Test
    public void testHuntState() throws IOException {
        PersistentModel persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
        StateModel state = StateReader.read(new File("src/test/resources/no-ships-hit.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should always hunt if no ships are hit.", StateResolver.AiState.HUNT, StateResolver.state);
    }
    
    @Test
    public void testKillState() throws IOException {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = PersistentModel.ActionType.FIRESHOT;
        persist.lastAction.x = 4;
        persist.lastAction.y = 9;
        
        StateModel state = StateReader.read(new File("src/test/resources/no-ships-sunk.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should always kill when ships not sunk.", StateResolver.AiState.KILL, StateResolver.state);
    }
    
    @Test
    public void testPlaceState() throws IOException {
        PersistentModel persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
        StateModel state = StateReader.read(new File("src/test/resources/place-state.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Ships need to be placed.", StateResolver.AiState.PLACE, StateResolver.state);
    }
    
    @Test
    public void testContinueHuntState() throws IOException {
        PersistentModel persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
        StateModel state = StateReader.read(new File("src/test/resources/continue-hunt-after-kill.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should continue hunting after kill.", StateResolver.AiState.HUNT, StateResolver.state);
    }
    
    @Test
    public void testKilledShipState() throws IOException {
        PersistentModel persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
        StateModel state = StateReader.read(new File("src/test/resources/hunt-one-earlier.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should continue hunting.", StateResolver.AiState.HUNT, StateResolver.state);
    }
    
    @Test
    public void testNotKillingLastShip() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = PersistentModel.ActionType.FIRESHOT;
        persist.lastAction.x = 12;
        persist.lastAction.y = 3;
        StateModel state = StateReader.read(new File("src/test/resources/bug/not-killing-last-ship.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should continue killing.", StateResolver.AiState.KILL, StateResolver.state);
    }
}
