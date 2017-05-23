package za.co.entelect.challenge;

import java.util.ArrayList;
import java.util.List;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.state.GameState;

public class DefaultPlacement {

    List<Point> huntDefault;
    List<Point> killDefault;

    public DefaultPlacement() {
        huntDefault = new ArrayList();
        huntDefault.add(new Point(1, 0));
        huntDefault.add(new Point(3, 1));
        huntDefault.add(new Point(4, 2));
        huntDefault.add(new Point(7, 3));
        huntDefault.add(new Point(1, 8));

        killDefault = new ArrayList();
        killDefault.add(new Point(1, 1));
        killDefault.add(new Point(1, 2));
        killDefault.add(new Point(1, 3));
        killDefault.add(new Point(4, 1));
        killDefault.add(new Point(5, 1));
        killDefault.add(new Point(6, 1));
        killDefault.add(new Point(7, 1));
        killDefault.add(new Point(4, 3));
        killDefault.add(new Point(4, 4));
        killDefault.add(new Point(7, 4));
        killDefault.add(new Point(2, 8));
        killDefault.add(new Point(3, 8));
    }

    public Command hunt(GameState state) {
        for (Point point : huntDefault) {
            if (state.OpponentMap.getCellAt(point.getX(), point.getY()).Missed) {
                return new Command(Code.DO_NOTHING, 0, 0);
            }
            if (!(state.OpponentMap.getCellAt(point.getX(), point.getY()).Damaged)) {
                return new Command(Code.FIRESHOT, point.getX(), point.getY());
            }
        }

        return kill(state);
    }

    public Command kill(GameState state) {
        for (Point point : killDefault) {
            if (state.OpponentMap.getCellAt(point.getX(), point.getY()).Missed) {
                return new Command(Code.DO_NOTHING, 0, 0);
            }
            if (!(state.OpponentMap.getCellAt(point.getX(), point.getY()).Damaged)) {
                return new Command(Code.FIRESHOT, point.getX(), point.getY());
            }
        }
        
        return new Command(Code.DO_NOTHING, 0, 0);
    }
}
