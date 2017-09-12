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
    public void testUseEnergy() throws IOException {
        StateModel model = StateReader.read(new File("src/test/resources/cannot-find-ship.json"), StateModel.class);

        Action action = HuntDecision.execute(model);
        assertNotEquals("Should use special shot.", "FIRESHOT", action.type.name());
    }

    @Test
    public void testCornerShot() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/carrier-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CORNER_SHOT", action.type.name());
    }

    @Test
    public void testCrossShotDiagonal() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/battleship-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CROSS_SHOT_DIAGONAL", action.type.name());

    }

    @Test
    public void testCrossShotHorizontal() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/cruiser-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CROSS_SHOT_HORIZONTAL", action.type.name());
    }

    @Test
    public void testSeekerMissile() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/submarine-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CROSS_SHOT_HORIZONTAL", action.type.name());
    }

    @Test
    public void testDoubleShot() {
        StateModel model = StateReader.read(new File("src/test/resources/shot/must-shoot/destroyer-must-shoot.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertNotEquals("Should use special shot.", "CROSS_SHOT_HORIZONTAL", action.type.name());
        assertNotEquals("Should use special shot.", "CROSS_SHOT_HORIZONTAL", action.type.name());
        assertNotEquals("Should use special shot.", "CROSS_SHOT_DIAGONAL", action.type.name());
        assertNotEquals("Should use special shot.", "CORNER_SHOT", action.type.name());
    }
}
