package net.avdw.battlefight.shot;

import java.io.File;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Action.Type;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;


public class ShotTypeDecisionTest {

    public ShotTypeDecisionTest() {
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
    public void testNoEnergyShot() {
        System.out.println("noEnergyShot");
        StateModel state = StateReader.read(new File("src/test/resources/hunt/spread-hunt.json"), StateModel.class);
        Action.Type expResult = Type.FIRESHOT;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testBattleshipMustShoot() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/must-shoot/battleship-must-shoot.json"), StateModel.class);
        Action.Type expResult = Type.CROSS_SHOT_DIAGONAL;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCarrierMustShoot() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/must-shoot/carrier-must-shoot.json"), StateModel.class);
        Action.Type expResult = Type.CORNER_SHOT;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCruiserMustShoot() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/must-shoot/cruiser-must-shoot.json"), StateModel.class);
        Action.Type expResult = Type.CROSS_SHOT_HORIZONTAL;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testBattleshipNotShoot() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/not-shoot/battleship-not-shoot.json"), StateModel.class);
        Action.Type expResult = Type.CROSS_SHOT_DIAGONAL;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertNotEquals(expResult, result);
    }

    @Test
    public void testCruiserNotShoot() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/not-shoot/cruiser-not-shoot.json"), StateModel.class);
        Action.Type expResult = Type.CROSS_SHOT_HORIZONTAL;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertNotEquals(expResult, result);
    }

    @Test
    public void testBattleshipSubWait() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/ready/battleship-submarine-wait.json"), StateModel.class);
        Action.Type expResult = Type.FIRESHOT;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCarrierWait() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/ready/carrier-wait.json"), StateModel.class);
        Action.Type expResult = Type.FIRESHOT;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testCruiserReady() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/ready/cruiser-ready.json"), StateModel.class);
        Action.Type expResult = Type.CROSS_SHOT_HORIZONTAL;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

    @Test
    public void testDestroyerWait() {
        StateModel state = StateReader.read(new File("src/test/resources/shot/ready/destroyer-wait.json"), StateModel.class);
        Action.Type expResult = Type.FIRESHOT;
        Action.Type result = ShotTypeDecision.huntShot(state);
        assertEquals(expResult, result);
    }

}
