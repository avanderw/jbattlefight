/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.state;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import net.avdw.battlefight.state.StateModel;
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
        StateModel state = StateReader.read(new File("src/test/resources/state.json"));

        assertNotNull(state);
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

        assertNotNull(state.PlayerMap);
        assertNotNull(state.PlayerMap.Owner);
        assertNotNull(state.PlayerMap.Owner.Energy);
        assertNotNull(state.PlayerMap.Owner.FailedFirstRoundCommands);
        assertNotNull(state.PlayerMap.Owner.IsWinner);
        assertNotNull(state.PlayerMap.Owner.Key);
        assertNotNull(state.PlayerMap.Owner.Killed);
        assertNotNull(state.PlayerMap.Owner.Name);
        assertNotNull(state.PlayerMap.Owner.Points);
        assertNotNull(state.PlayerMap.Owner.Ships);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0));
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Cells);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Destroyed);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Placed);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).ShipType);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Weapons);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Weapons.get(0));
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Weapons.get(0).EnergyRequired);
        assertEquals(1, state.PlayerMap.Owner.Ships.get(0).Weapons.get(0).EnergyRequired);
        assertNotNull(state.PlayerMap.Owner.Ships.get(0).Weapons.get(0).WeaponType);
        assertNotNull(state.PlayerMap.Owner.ShipsRemaining);
        assertNotNull(state.PlayerMap.Owner.ShotsFired);
        assertNotNull(state.PlayerMap.Owner.ShotsHit);
        assertNotNull(state.PlayerMap.Cells);
        assertNotNull(state.PlayerMap.Cells.get(0));
        assertNotNull(state.PlayerMap.Cells.get(0).Hit);
        assertNotNull(state.PlayerMap.Cells.get(0).Occupied);
        assertNotNull(state.PlayerMap.Cells.get(0).X);
        assertNotNull(state.PlayerMap.Cells.get(0).Y);
        assertNotNull(state.PlayerMap.MapWidth);
        assertEquals(state.PlayerMap.MapWidth, 14);
        assertNotNull(state.PlayerMap.MapHeight);
        assertEquals(state.PlayerMap.MapHeight, 14);

        assertNotNull(state.OpponentMap);
        assertNotNull(state.OpponentMap.Alive);
        assertNotNull(state.OpponentMap.Cells);
        assertNotNull(state.OpponentMap.Cells.get(0));
        assertNotNull(state.OpponentMap.Cells.get(0).Damaged);
        assertNotNull(state.OpponentMap.Cells.get(0).Missed);
        assertNotNull(state.OpponentMap.Cells.get(0).X);
        assertNotNull(state.OpponentMap.Cells.get(0).Y);
        assertNotNull(state.OpponentMap.Name);
        assertNotNull(state.OpponentMap.Points);
        assertNotNull(state.OpponentMap.Ships);
        assertNotNull(state.OpponentMap.Ships.get(0));
        assertNotNull(state.OpponentMap.Ships.get(0).Destroyed);
        assertNotNull(state.OpponentMap.Ships.get(0).ShipType);
        assertNotNull(state.OpponentMap.Ships.get(0).ShipType.Battleship);
    }
}
