/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.entelect.challenge;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static junit.framework.TestCase.*;
import net.avdw.battlefight.Action;
import net.avdw.battlefight.Direction;
import net.avdw.battlefight.place.PlacementStrategy;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author CP318674
 */
public class PlacementTest {

    static private Action action;

    @BeforeClass
    public static void setUpClass() throws IOException {
        action = PlacementStrategy.place(StateReader.read(new File("src/main/resources/state.json")));
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
                        xy = placement.x1 + "" + (placement.y1 + i);
                        break;
                    case South:
                        xy = placement.x1 + "" + (placement.y1 - i);
                        break;
                    case East:
                        xy = (placement.x1 + i) + "" + placement.y1;
                        break;
                    case West:
                        xy = (placement.x1 - i) + "" + placement.y1;
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
            if (outOfBounds(placement.x1, placement.y1) || outOfBounds(placement.x2, placement.y2)) {
                fail(String.format("Ship out of bounds: %s", placement));
            }
        });
    }

    @Test
    public void notInCenter() {
        parseOutput(action.toString()).forEach((placement) -> {
            if (Zone.FIVE.contains(placement.x1, placement.y1) || Zone.FIVE.contains(placement.x2, placement.y2)) {
                fail(String.format("Ship in center: %s", placement));
            }
        });
    }

    @Test
    public void noMoreThanOneBorderShip() {
        long count = 0;
        count = parseOutput(action.toString()).stream().filter((placement) -> (onEdge(placement.x1, placement.y1) || onEdge(placement.x2, placement.y2))).count();
        if (count > 1) {
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
                            for (int[] row : map) {
                                System.out.println(Arrays.toString(row));
                            }
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
            fail("not done");
        });
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
    
    private boolean outOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x > 13 || y > 13);
    }

    private boolean onEdge(int x, int y) {
        return (x == 0 || y == 0 || x == 13 || y == 13);
    }

    static class Zone {

        static private final Zone ONE = new Zone(0, 0, 4, 4);
        static private final Zone TWO = new Zone(4, 0, 9, 4);
        static private final Zone THREE = new Zone(9, 0, 13, 4);
        static private final Zone FOUR = new Zone(0, 4, 4, 9);
        static private final Zone FIVE = new Zone(4, 4, 9, 9);
        static private final Zone SIX = new Zone(9, 4, 13, 9);
        static private final Zone SEVEN = new Zone(0, 9, 4, 13);
        static private final Zone EIGHT = new Zone(4, 9, 9, 13);
        static private final Zone NINE = new Zone(9, 9, 13, 13);
        
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        private Zone(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        
        public boolean contains(int x, int y) {
            return (x <= x2 && x >= x1 && y <= y2 && y >= y1);
        }
    }

    class ShipPlacement {

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
                    this.y2 = this.y1 + this.type.length();
                    this.x2 = this.x1;
                    break;
                case South:
                    this.y2 = this.y1 - this.type.length();
                    this.x2 = this.x1;
                    break;
                case West:
                    this.y2 = this.y1;
                    this.x2 = this.x1 - this.type.length();
                    break;
                case East:
                    this.y2 = this.y1;
                    this.x2 = this.x1 + this.type.length();
                    break;
                default:
                    this.y2 = -1;
                    this.x2 = -1;
                    break;
            }
        }

        @Override
        public String toString() {
            return String.format("%s, (%s, %s), %s, (%s, %s)", new Object[]{type, x1, y1, direction, x2, y2});
        }
    }
}
