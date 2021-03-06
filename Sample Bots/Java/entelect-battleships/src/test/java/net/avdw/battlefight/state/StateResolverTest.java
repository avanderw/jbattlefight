/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.state;

import java.io.File;
import java.io.IOException;
import net.avdw.battlefight.struct.Action;
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
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 12;
        persist.lastAction.y = 3;
        StateModel state = StateReader.read(new File("src/test/resources/bug/not-killing-last-ship.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should continue killing.", StateResolver.AiState.KILL, StateResolver.state);
    }

    @Test
    public void testV5Bug() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 5;
        persist.lastAction.y = 1;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v5-didnt-kill.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Should continue killing.", StateResolver.AiState.KILL, StateResolver.state);
    }

    @Test
    public void testV6120527() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 4;
        persist.lastAction.y = 8;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-12-05-27.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Hunt state.", StateResolver.AiState.HUNT, StateResolver.state);
    }

    @Test
    public void testV6130239() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.CORNER_SHOT;
        persist.lastAction.x = 9;
        persist.lastAction.y = 10;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-02-39.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Kill state.", StateResolver.AiState.KILL, StateResolver.state);
    }

    @Test
    public void testV6130422() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 3;
        persist.lastAction.y = 6;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-04-22.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Hunt state.", StateResolver.AiState.HUNT, StateResolver.state);
    }
    
    @Test
    public void testV6130700() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 5;
        persist.lastAction.y = 8;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-07-00.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Kill state.", StateResolver.AiState.KILL, StateResolver.state);
    }
    
    @Test
    public void testV6130705() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 10;
        persist.lastAction.y = 13;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-07-05.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Kill state.", StateResolver.AiState.KILL, StateResolver.state);
    }
    
    @Test
    public void testShield() {
        PersistentModel persist = new PersistentModel();
        persist.lastAction = new PersistentModel.Action();
        persist.lastAction.type = Action.Type.FIRESHOT;
        persist.lastAction.x = 10;
        persist.lastAction.y = 13;
        persist.lastHeading = null;
        StateModel state = StateReader.read(new File("src/test/resources/shield/v7-15-15-07.json"), StateModel.class);
        StateResolver.setup(state, persist);
        assertEquals("Kill state.", StateResolver.AiState.SHIELD, StateResolver.state);
    }
}
