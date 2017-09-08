package net.avdw.battlefight;

import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateResolver;
import net.avdw.battlefight.kill.KillBehaviourTree;
import net.avdw.battlefight.place.PlacementStrategy;
import net.avdw.battlefight.hunt.HuntBehaviourTree;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.avdw.battlefight.place.PlaceAction;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.state.StateWriter;

public class Main {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String workingDirectory = args[1];

        PersistentModel persist = null;
        if (new File("persistent.json").exists()) {
            persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
        }

        try {
            StateModel state = StateReader.read(new File(workingDirectory, "state.json"), StateModel.class);
            StateResolver.setup(state, persist);

            Action action = null;
            switch (StateResolver.state) {
                case PLACE:
                    action = PlacementStrategy.place(state);
                    break;
                case KILL:
                    System.out.println("Killing");
                    action = KillBehaviourTree.execute(state, persist);
                    if (action != null) {
                        break;
                    }
                case HUNT:
                    System.out.println("Hunting");
                    action = HuntBehaviourTree.execute(state);
                    break;
            }

            if (action == null) {
                System.out.println("Killing failed! Hunting as fallback.");
                action = HuntBehaviourTree.execute(state);
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, action.filename)))) {
                bufferedWriter.write(action.toString());
                bufferedWriter.flush();
            }

            System.out.println(action);
            System.out.println(action.type);
            if (!(action instanceof PlaceAction)) {
                persist.lastAction = new PersistentModel.Action();
                System.out.println(action.type.name());
                persist.lastAction.type = PersistentModel.ActionType.valueOf(action.type.name());
                persist.lastAction.x = action.point.x;
                persist.lastAction.y = action.point.y;
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            StateWriter.write("persistent.json", persist);
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("[Bot]\tBot finished in %d ms.", (endTime - startTime)));
        }
    }
}
