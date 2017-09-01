package net.avdw.battlefight;

import net.avdw.battlefight.struct.Action;

public class NoAction extends Action {

    public NoAction() {
        super();
        filename = "command.txt";
        type = Action.Type.DO_NOTHING;
    }
}
