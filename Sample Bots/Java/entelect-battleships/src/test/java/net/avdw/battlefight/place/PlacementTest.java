/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.place;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static junit.framework.TestCase.*;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.struct.Direction;
import net.avdw.battlefight.struct.Zone;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateModel.ShipType;
import net.avdw.battlefight.state.StateReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlacementTest {

    static private Action action;

    @BeforeClass
    public static void setUpClass() throws IOException {
        action = PlacementStrategy.place(StateReader.read(new File("src/test/resources/state.json"), StateModel.class));
        System.out.println(action.toString());
        print();
    }

    @Before
    public void setupTest() {
        Zone.resetZones();
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
                        xy = pad(placement.x1) + "" + pad(placement.y1 + i);
                        break;
                    case South:
                        xy = pad(placement.x1) + "" + pad(placement.y1 - i);
                        break;
                    case East:
                        xy = pad(placement.x1 + i) + "" + pad(placement.y1);
                        break;
                    case West:
                        xy = pad(placement.x1 - i) + "" + pad(placement.y1);
                        break;
                }
                if (xySet.contains(xy)) {
                    System.out.println(xySet);
                    fail(String.format("Cannot place ship: %s", placement));
                } else {
                    xySet.add(xy);
                }
            }
        });
    }

    private String pad(int value) {
        return (value < 10) ? "0" + value : "" + value;
    }

    @Test
    public void inBounds() {
        parseOutput(action.toString()).forEach((placement) -> {
            if (outOfBounds(placement.x1, placement.y1) || outOfBounds(placement.x2, placement.y2)) {
                fail(String.format("Ship out of bounds: %s", placement));
            }
        });
    }

    @Test
    public void notInCenter() {
        parseOutput(action.toString()).forEach((placement) -> {
            if (Zone.FIVE.containsPointBoundsExclusive(placement.x1, placement.y1) || Zone.FIVE.containsPointBoundsExclusive(placement.x2, placement.y2)) {
                fail(String.format("Ship in center: %s", placement));
            }
        });
    }

    @Test
    public void noMoreThanOneBorderShip() {
        long count = 0;
        count = parseOutput(action.toString()).stream().filter((placement) -> (onEdge(placement.x1, placement.y1) || onEdge(placement.x2, placement.y2))).count();
        if (count > 2) {
            fail(String.format("%s ships on border", new Object[]{count}));
        }
    }

    @Test
    public void notTouching() {
        int[][] map = new int[14][14];
        parseOutput(action.toString()).forEach((placement) -> {
            for (int x = min(placement.x1, placement.x2); x <= max(placement.x1, placement.x2); x++) {
                for (int y = min(placement.y1, placement.y2); y <= max(placement.y1, placement.y2); y++) {
                    map[y][x] = 1;
                }
            }

            for (int x = 1; x < 13; x++) {
                for (int y = 1; y < 13; y++) {
                    if (map[y][x] == 1) {
                        if ((map[y][x - 1] == 1 || map[y][x + 1] == 1) && (map[y - 1][x] == 1 || map[y + 1][x] == 1)) {
                            fail(String.format("Ships are touching"));
                        }
                    }
                }
            }
        });
    }

    @Test
    public void inAllCornersNoDestroyer() {
        parseOutput(action.toString()).forEach((placement) -> {
            for (Zone zone : Zone.ALL_ZONES) {
                if (zone.containsPoint(placement.x1, placement.y1)) {
                    zone.addShip(placement.type);
                } else if (zone.containsPoint(placement.x2, placement.y2)) {
                    zone.addShip(placement.type);
                }
            }
        });

        assertFalse("Zone ONE may not contain the destroyer", Zone.ONE.containsShip(ShipType.Destroyer));
        assertFalse("Zone THREE may not contain the destroyer", Zone.THREE.containsShip(ShipType.Destroyer));
        assertFalse("Zone SEVEN may not contain the destroyer", Zone.SEVEN.containsShip(ShipType.Destroyer));
        assertFalse("Zone NINE may not contain the destroyer", Zone.NINE.containsShip(ShipType.Destroyer));

        assertTrue("Zone ONE must contain a ship, contains " + Zone.ONE.containedShipCount(), Zone.ONE.containedShipCount() == 1);
        assertTrue("Zone THREE must contain a ship, contains " + Zone.THREE.containedShipCount(), Zone.THREE.containedShipCount() == 1);
        assertTrue("Zone SEVEN must contain a ship, contains " + Zone.SEVEN.containedShipCount(), Zone.SEVEN.containedShipCount() == 1);
        assertTrue("Zone NINE must contain a ship, contains " + Zone.NINE.containedShipCount(), Zone.NINE.containedShipCount() == 1);
    }

    static private List<ShipPlacement> parseOutput(String output) {
        List<ShipPlacement> ships = new ArrayList();
        String[] lines = output.split("\n");
        for (String line : lines) {
            String[] ship = line.split("\\s");
            ships.add(new ShipPlacement(ship[0], ship[1], ship[2], ship[3]));
        }
        return ships;
    }

    private boolean outOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x > 13 || y > 13);
    }

    private boolean onEdge(int x, int y) {
        return (x == 0 || y == 0 || x == 13 || y == 13);
    }

    static private void print() {
        int[][] map = new int[14][14];
        parseOutput(action.toString()).forEach((placement) -> {
            System.out.println("placing: " + placement);
            if (placement.y1 == placement.y2) {
                if (placement.x1 < placement.x2) {
                    for (int x = placement.x1; x <= placement.x2; x++) {
                        map[placement.y1][x] = 1;
                    }
                } else {
                    for (int x = placement.x2; x <= placement.x1; x++) {
                        map[placement.y1][x] = 1;
                    }
                }

            }

            if (placement.x1 == placement.x2) {
                if (placement.y1 < placement.y2) {
                    for (int y = placement.y1; y <= placement.y2; y++) {
                        map[y][placement.x1] = 1;
                    }
                } else {
                    for (int y = placement.y2; y <= placement.y1; y++) {
                        map[y][placement.x1] = 1;
                    }
                }
            }
        });

        for (int y = map.length - 1; y >= 0; y--) {
            System.out.println(Arrays.toString(map[y]));
        }
    }

    static class ShipPlacement {

        private final Direction direction;
        private final StateModel.ShipType type;
        private final int y1;
        private final int x1;
        private final int y2;
        private final int x2;

        private ShipPlacement(String type, String x, String y, String direction) {
            this.type = StateModel.ShipType.valueOf(type);
            this.x1 = Integer.parseInt(x);
            this.y1 = Integer.parseInt(y);
            this.direction = Direction.valueOf(direction);
            switch (this.direction) {
                case North:
                    this.y2 = this.y1 + (this.type.length() - 1);
                    this.x2 = this.x1;
                    break;
                case South:
                    this.y2 = this.y1 - (this.type.length() - 1);
                    this.x2 = this.x1;
                    break;
                case West:
                    this.y2 = this.y1;
                    this.x2 = this.x1 - (this.type.length() - 1);
                    break;
                case East:
                    this.y2 = this.y1;
                    this.x2 = this.x1 + (this.type.length() - 1);
                    break;
                default:
                    this.y2 = -1;
                    this.x2 = -1;
                    fail("invalid placement");
                    break;
            }
        }

        @Override
        public String toString() {
            return String.format("%s (%s, %s) %s", new Object[]{type, x1, y1, direction});
        }
    }
}
