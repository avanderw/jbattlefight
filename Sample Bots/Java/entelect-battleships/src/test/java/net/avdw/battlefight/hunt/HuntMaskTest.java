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

    static StateModel noShipsSunk;
    static StateModel destroyerLeft;
    static StateModel battleshipLeft;
    static StateModel carrierLeft;
    static StateModel carrierSunk;
    static StateModel cruiserLeft;

    @BeforeClass
    public static void setUpClass() throws IOException {
        noShipsSunk = StateReader.read(new File("src/test/resources/no-ships-sunk.json"));
        destroyerLeft = StateReader.read(new File("src/test/resources/destroyer-left.json"));
        battleshipLeft = StateReader.read(new File("src/test/resources/battleship-left.json"));
        carrierLeft = StateReader.read(new File("src/test/resources/carrier-left.json"));
        carrierSunk = StateReader.read(new File("src/test/resources/carrier-sunk.json"));
        cruiserLeft = StateReader.read(new File("src/test/resources/cruiser-left.json"));
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
    public void testHuntLargestFirstMask() {
        HuntMask huntMask = new HuntMask(carrierSunk);
        assertEquals("Battleship should be hunted first.",
                "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n", huntMask.toString());
    }

    @Test
    public void testDestroyerMask() {
        HuntMask huntMask = new HuntMask(destroyerLeft);
        assertEquals("Destroyer mask not correct.",
                "01010101010101\n"
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
                + "01010101010101\n"
                + "10101010101010\n", huntMask.toString());
    }

    @Test
    public void testCruiserSubMask() {
        HuntMask huntMask = new HuntMask(cruiserLeft);

        assertEquals("Cruiser mask not correct.",
                "00100100100100\n"
                + "10010010010010\n"
                + "01001001001001\n"
                + "00100100100100\n"
                + "10010010010010\n"
                + "01001001001001\n"
                + "00100100100100\n"
                + "10010010010010\n"
                + "01001001001001\n"
                + "00100100100100\n"
                + "10010010010010\n"
                + "01001001001001\n"
                + "00100100100100\n"
                + "10010010010010\n", huntMask.toString());
    }

    @Test
    public void testBattleshipMask() {
        HuntMask huntMask = new HuntMask(battleshipLeft);

        assertEquals("Battleship mask not correct.",
                "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n", huntMask.toString());
    }

    @Test
    public void testCarrierMask() {
        HuntMask huntMask = new HuntMask(carrierLeft);

        assertEquals("Carrier mask not correct.",
                "00100001000010\n"
                + "00010000100001\n"
                + "00001000010000\n"
                + "10000100001000\n"
                + "01000010000100\n"
                + "00100001000010\n"
                + "00010000100001\n"
                + "00001000010000\n"
                + "10000100001000\n"
                + "01000010000100\n"
                + "00100001000010\n"
                + "00010000100001\n"
                + "00001000010000\n"
                + "10000100001000\n", huntMask.toString());
    }
    
    @Test
    public void testMaskOverride() {
        HuntMask mask = new HuntMask(2);
        assertEquals("Mask shoiuld be 2 mask.",
                "01010101010101\n"
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
                + "01010101010101\n"
                + "10101010101010\n", mask.toString());
        
        mask = new HuntMask(4);
        assertEquals("Battleship mask not correct.",
                "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n"
                + "01000100010001\n"
                + "00100010001000\n"
                + "00010001000100\n"
                + "10001000100010\n", mask.toString());
    }

}
