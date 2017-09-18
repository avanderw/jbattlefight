package net.avdw.battlefight.hunt;

import java.io.File;
import java.io.IOException;
import net.avdw.battlefight.MapQuery;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.struct.Action;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class HuntDecisionTest {

    @Test
    public void testCannotFindShip() throws IOException {
        StateModel model = StateReader.read(new File("src/test/resources/cannot-find-ship.json"), StateModel.class);
        StateModel.OpponentCell[][] map = MapQuery.transformMap(model.OpponentMap.Cells);
        MapQuery.printMap(map);

        Action action = HuntDecision.execute(model);
        assertNotEquals("Don't make illegal shots.", "1,0,0", action.toString());
        System.out.println(action);
    }

    @Test
    public void testCornerShot() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/carrier-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CORNER_SHOT", action.type.name());
        assertEquals("Should shoot highest potential.", "4,10,7", action.toString());
    }

    @Test
    public void testCrossShotDiagonal() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/battleship-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CROSS_SHOT_DIAGONAL", action.type.name());
        assertEquals("Should shoot highest potential.", "5,2,8", action.toString());

    }

    @Test
    @Ignore("I don't like firing the cruiser")
    public void testCrossShotHorizontal() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/cruiser-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CROSS_SHOT_HORIZONTAL", action.type.name());
        assertEquals("Should shoot highest potential.", "6,1,5", action.toString());
    }

    @Test
    public void testSeekerMissile() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/submarine-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "SEEKER_MISSILE", action.type.name());
        assertEquals("Should shoot highest potential.", "7,2,7", action.toString());
    }

    @Test
    public void testDoubleShot() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/destroyer-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertNotEquals("Should use special shot.", "SEEKER_MISSILE", action.type.name());
        assertNotEquals("Should use special shot.", "CROSS_SHOT_HORIZONTAL", action.type.name());
        assertNotEquals("Should use special shot.", "CROSS_SHOT_DIAGONAL", action.type.name());
        assertNotEquals("Should use special shot.", "CORNER_SHOT", action.type.name());
        assertEquals("Should shoot highest potential.", "2,2,6", action.toString());
    }

    @Test
    public void testV6120527() {
        StateModel model = StateReader.read(new File("src/test/resources/bug/v6-12-05-27.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        System.out.println(action);
        assertEquals("Wait for battleship.", Action.Type.FIRESHOT, action.type);
    }

    @Test
    public void testV6130422() {
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-04-22.json"), StateModel.class);
        Action action = HuntDecision.execute(state);
        assertNotEquals("Use special shot.", Action.Type.FIRESHOT, action.type);
    }

    @Test
    public void testV6130439() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-13-04-39.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        assertNotEquals("Use special shot.", "7,7,2", action.toString());
        System.out.println(action);
    }

    @Test
    public void testV6130458() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-13-04-58.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        assertNotEquals("Don't use seeker.", Action.Type.SEEKER_MISSILE, action.type);
        System.out.println(action);
    }

    @Test
    public void testV6130542() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-13-05-42.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertTrue("Avoid middle with special shots.", action.point.x > 9 || action.point.x < 4 || action.point.y > 9 || action.point.y < 4);
    }

    @Test
    public void testV6130551() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-13-05-51.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertNotEquals("Wait for cross shot.", Action.Type.CORNER_SHOT, action.type);
    }

    @Test
    public void testV6130557() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-13-05-57.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertTrue("Hunt weak shots first with last ship.", action.point.x == 13 || action.point.x == 0 || action.point.y == 13 || action.point.y == 0);
    }

    @Test
    public void testV6131013() {
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-10-13.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertTrue("Hunt weak shots first with last ship.", action.point.x == 13 || action.point.x == 0 || action.point.y == 13 || action.point.y == 0);
    }

    @Test
    public void testV6132322() {
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-13-23-22.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertEquals("There is no better shot.", Action.Type.DOUBLE_SHOT_HORIZONTAL, action.type);
    }

    @Test
    public void testV6141930() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-14-19-30.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertNotEquals("Use special shot.", Action.Type.FIRESHOT, action.type);
    }

    @Test
    public void testV6141935() {
        StateModel state = StateReader.read(new File("src/test/resources/hunt/v6-14-19-35.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertNotEquals("Use special shot.", Action.Type.FIRESHOT, action.type);
    }

    @Test
    public void testV6141954() {
        StateModel state = StateReader.read(new File("src/test/resources/bug/v6-14-19-54.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
    }
    
    @Test
    public void testV7151600() {
        StateModel state = StateReader.read(new File("src/test/resources/bug/v7-15-16-00.json"), StateModel.class);
        assertNotNull(state);
        Action action = HuntDecision.execute(state);
        System.out.println(action);
        assertNotEquals("Don't use shots without energy", "6,1,1", action.toString());
    }
}
