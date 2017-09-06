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
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String workingDirectory = args[1];

        try {
            StateModel stateModel = StateReader.read(new File(workingDirectory, "state.json"), StateModel.class);
            StateResolver.setup(stateModel);

            Action action = null;
            switch (StateResolver.state) {
                case PLACE:
                    action = PlacementStrategy.place(stateModel);
                    break;
                case KILL:
                    action = KillBehaviourTree.execute(stateModel);
                    if (action != null) {
                        break;
                    }
                case HUNT:
                    action = HuntBehaviourTree.execute(stateModel);
                    break;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, action.filename)))) {
                bufferedWriter.write(action.toString());
                bufferedWriter.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("[Bot]\tBot finished in %d ms.", (endTime - startTime)));
        }
    }
}
