/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.entelect.challenge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static junit.framework.TestCase.*;
import net.avdw.battlefight.Action;
import net.avdw.battlefight.Direction;
import net.avdw.battlefight.place.PlacementStrategy;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author CP318674
 */
public class PlacementTest {

    static private Action action;

    public PlacementTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        action = PlacementStrategy.place(StateReader.read(new File("src/main/resources/state.json")));
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
    public void validOutput() {
        assertNotNull(action);

        assertEquals("place.txt", action.filename);
        assertEquals(5, action.toString().split("\n").length);
        assertEquals(20, action.toString().split("\\s").length);

        assertTrue("Does not contain Battleship", action.toString().contains(StateModel.ShipType.Battleship.name()));
        assertTrue("Does not contain Carrier", action.toString().contains(StateModel.ShipType.Carrier.name()));
        assertTrue("Does not contain Cruiser", action.toString().contains(StateModel.ShipType.Cruiser.name()));
        assertTrue("Does not contain Destroyer", action.toString().contains(StateModel.ShipType.Destroyer.name()));
        assertTrue("Does not contain Submarine", action.toString().contains(StateModel.ShipType.Submarine.name()));
    }

    @Test
    public void noOverlap() {
        Set<String> xySet = new HashSet();
        parseOutput(action.toString()).forEach((placement) -> {
            String xy = null;
            for (int i = 0; i < placement.type.length(); i++) {
                switch (placement.direction) {
                    case North:
                        xy = placement.x + "" + (placement.y + i);
                        break;
                    case South:
                        xy = placement.x + "" + (placement.y - i);
                        break;
                    case East:
                        xy = (placement.x + i) + "" + placement.y;
                        break;
                    case West:
                        xy = (placement.x - i) + "" + placement.y;
                        break;
                }
                if (xySet.contains(xy)) {
                    fail(String.format("Cannot place ship: %s", placement));
                } else {
                    xySet.add(xy);
                }
            }
        });
    }

    @Test
    public void inBounds() {
        parseOutput(action.toString()).forEach((placement) -> {
            switch (placement.direction) {
                case North:
                    if (placement.y + placement.type.length() >= 14) {
                        fail(String.format("Cannot place ship: %s", placement));
                    }
                    break;
                case South:
                    if (placement.y - placement.type.length() < 0) {
                        fail(String.format("Cannot place ship: %s", placement));
                    }
                    break;
                case East:
                    if (placement.x + placement.type.length() >= 14) {
                        fail(String.format("Cannot place ship: %s", placement));
                    }
                    break;
                case West:
                    if (placement.x - placement.type.length() < 0) {
                        fail(String.format("Cannot place ship: %s", placement));
                    }
                    break;
            }
        });
    }
    
    @Test
    public void inAllCornersNoDestroyer() {
        
    }

    @Test
    public void notInCenter() {

    }

    @Test
    public void noMoreThanOneEdgeShip() {

    }

    @Test
    public void notTouching() {

    }

    private List<ShipPlacement> parseOutput(String output) {
        List<ShipPlacement> ships = new ArrayList();
        String[] lines = output.split("\n");
        for (String line : lines) {
            String[] ship = line.split("\\s");
            ships.add(new ShipPlacement(ship[0], ship[1], ship[2], ship[3]));
        }
        return ships;
    }

    class ShipPlacement {

        private final Direction direction;
        private final StateModel.ShipType type;
        private final int y;
        private final int x;

        private ShipPlacement(String type, String x, String y, String direction) {
            this.type = StateModel.ShipType.valueOf(type);
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
            this.direction = Direction.valueOf(direction);
        }

        public String toString() {
            return String.format("%s, %s, %s, %s", new Object[]{type, x, y, direction});
        }
    }
}
