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
    public void testV6120527(){
        StateModel model = StateReader.read(new File("src/test/resources/bug/v6-12-05-27.json"), StateModel.class);
        Action action = HuntDecision.execute(model);
        assertEquals("Should use special shot.", "CORNER_SHOT", action.type.name());
        assertEquals("Should shoot highest potential.", "4,6,5", action.toString());
    }
}
