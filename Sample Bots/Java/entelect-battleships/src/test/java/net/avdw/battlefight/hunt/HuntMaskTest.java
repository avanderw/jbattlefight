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

public class HuntMaskTest {

    static StateModel noShipsSunkModel;

    @BeforeClass
    public static void setUpClass() throws IOException {
        noShipsSunkModel = StateReader.read(new File("src/test/resources/no-ships-sunk.json"));
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
    public void testDestroyerMask() {
        HuntMask huntMask = new HuntMask(noShipsSunkModel);
        assertEquals("Destroyer mask not correct.",
                "10101010101010\n"
                + "01010101010101\n"
                + "10101010101010\n"
                + "01010101010101\n"
                + "10101010101010\n"
                + "01010101010101\n"
                + "10101010101010\n"
                + "01010101010101\n"
                + "10101010101010\n"
                + "01010101010101\n"
                + "10101010101010\n"
                + "01010101010101\n"
                + "10101010101010\n"
                + "01010101010101\n", huntMask.toString());
    }

}
