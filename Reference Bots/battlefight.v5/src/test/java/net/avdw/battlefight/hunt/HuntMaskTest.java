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
        noShipsSunk = StateReader.read(new File("src/test/resources/no-ships-sunk.json"), StateModel.class);
        destroyerLeft = StateReader.read(new File("src/test/resources/destroyer-left.json"), StateModel.class);
        battleshipLeft = StateReader.read(new File("src/test/resources/battleship-left.json"), StateModel.class);
        carrierLeft = StateReader.read(new File("src/test/resources/carrier-left.json"), StateModel.class);
        carrierSunk = StateReader.read(new File("src/test/resources/carrier-sunk.json"), StateModel.class);
        cruiserLeft = StateReader.read(new File("src/test/resources/cruiser-left.json"), StateModel.class);
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
                "00010001000100 13\n"
                + "10001000100010 12\n"
                + "01000100010001 11\n"
                + "00100010001000 10\n"
                + "00010001000100 9\n"
                + "10001000100010 8\n"
                + "01000100010001 7\n"
                + "00100010001000 6\n"
                + "00010001000100 5\n"
                + "10001000100010 4\n"
                + "01000100010001 3\n"
                + "00100010001000 2\n"
                + "00010001000100 1\n"
                + "10001000100010 0\n"
                + "01234567890123", huntMask.toString());
    }

    @Test
    public void testDestroyerMask() {
        HuntMask huntMask = new HuntMask(destroyerLeft);
        assertEquals("Destroyer mask not correct.",
                "01010101010101 13\n"
                + "10101010101010 12\n"
                + "01010101010101 11\n"
                + "10101010101010 10\n"
                + "01010101010101 9\n"
                + "10101010101010 8\n"
                + "01010101010101 7\n"
                + "10101010101010 6\n"
                + "01010101010101 5\n"
                + "10101010101010 4\n"
                + "01010101010101 3\n"
                + "10101010101010 2\n"
                + "01010101010101 1\n"
                + "10101010101010 0\n"
                + "01234567890123", huntMask.toString());
    }

    @Test
    public void testCruiserSubMask() {
        HuntMask huntMask = new HuntMask(cruiserLeft);

        assertEquals("Cruiser mask not correct.",
                "00100100100100 13\n"
                + "10010010010010 12\n"
                + "01001001001001 11\n"
                + "00100100100100 10\n"
                + "10010010010010 9\n"
                + "01001001001001 8\n"
                + "00100100100100 7\n"
                + "10010010010010 6\n"
                + "01001001001001 5\n"
                + "00100100100100 4\n"
                + "10010010010010 3\n"
                + "01001001001001 2\n"
                + "00100100100100 1\n"
                + "10010010010010 0\n"
                + "01234567890123", huntMask.toString());
    }

    @Test
    public void testBattleshipMask() {
        HuntMask huntMask = new HuntMask(battleshipLeft);

        assertEquals("Battleship mask not correct.",
                "00010001000100 13\n"
                + "10001000100010 12\n"
                + "01000100010001 11\n"
                + "00100010001000 10\n"
                + "00010001000100 9\n"
                + "10001000100010 8\n"
                + "01000100010001 7\n"
                + "00100010001000 6\n"
                + "00010001000100 5\n"
                + "10001000100010 4\n"
                + "01000100010001 3\n"
                + "00100010001000 2\n"
                + "00010001000100 1\n"
                + "10001000100010 0\n"
                + "01234567890123", huntMask.toString());
    }

    @Test
    public void testCarrierMask() {
        HuntMask huntMask = new HuntMask(carrierLeft);

        assertEquals("Carrier mask not correct.",
                "00100001000010 13\n"
                + "00010000100001 12\n"
                + "00001000010000 11\n"
                + "10000100001000 10\n"
                + "01000010000100 9\n"
                + "00100001000010 8\n"
                + "00010000100001 7\n"
                + "00001000010000 6\n"
                + "10000100001000 5\n"
                + "01000010000100 4\n"
                + "00100001000010 3\n"
                + "00010000100001 2\n"
                + "00001000010000 1\n"
                + "10000100001000 0\n"
                + "01234567890123", huntMask.toString());
    }

    @Test
    public void testMaskOverride() {
        HuntMask mask = new HuntMask(2);
        assertEquals("Mask shoiuld be 2 mask.",
                "01010101010101 13\n"
                + "10101010101010 12\n"
                + "01010101010101 11\n"
                + "10101010101010 10\n"
                + "01010101010101 9\n"
                + "10101010101010 8\n"
                + "01010101010101 7\n"
                + "10101010101010 6\n"
                + "01010101010101 5\n"
                + "10101010101010 4\n"
                + "01010101010101 3\n"
                + "10101010101010 2\n"
                + "01010101010101 1\n"
                + "10101010101010 0\n"
                + "01234567890123", mask.toString());

        mask = new HuntMask(4);
        assertEquals("Battleship mask not correct.",
                "00010001000100 13\n"
                + "10001000100010 12\n"
                + "01000100010001 11\n"
                + "00100010001000 10\n"
                + "00010001000100 9\n"
                + "10001000100010 8\n"
                + "01000100010001 7\n"
                + "00100010001000 6\n"
                + "00010001000100 5\n"
                + "10001000100010 4\n"
                + "01000100010001 3\n"
                + "00100010001000 2\n"
                + "00010001000100 1\n"
                + "10001000100010 0\n"
                + "01234567890123", mask.toString());
    }

}
