package net.avdw.battlefight.struct;

import net.avdw.battlefight.state.StateModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZoneTest {

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
    public void testContainsPoint() {
        assertTrue(Zone.ONE.containsPoint(0, 13));
        assertFalse(Zone.ONE.containsPoint(0, 0));
        assertTrue(Zone.ONE.containsPoint(4, 9));
        assertFalse(Zone.ONE.containsPoint(4, 8));
    }

    @Test
    public void testFit() {
        Place place = Zone.ONE.fit(StateModel.ShipType.Carrier, true);
        assertTrue(Zone.ONE.containsPoint(place.x, place.y));
        switch (place.direction) {
            case North:
                assertTrue(Zone.ONE.containsPoint(place.x, place.y + 4));
                break;
            case South:
                assertTrue(Zone.ONE.containsPoint(place.x, place.y - 4));
                break;
            case East:
                assertTrue(Zone.ONE.containsPoint(place.x + 4, place.y));
                break;
            case West:
                assertTrue(Zone.ONE.containsPoint(place.x - 4, place.y));
                break;
        }

        place = Zone.ONE.fit(StateModel.ShipType.Destroyer, false);
        assertNotEquals(0, place.x);
        assertNotEquals(4, place.x);
        assertNotEquals(13, place.y);
        assertNotEquals(9, place.y);
        switch (place.direction) {
            case North:
                assertNotEquals(13, place.y + 1);
                break;
            case South:
                assertNotEquals(9, place.y - 1);
                break;
            case East:
                assertNotEquals(4, place.x + 1);
                break;
            case West:
                assertNotEquals(0, place.x - 1);
                break;
        }
    }

}
