package net.avdw.battlefight;

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
            StateModel stateModel = StateReader.read(new File(workingDirectory, "state.json"));
            StateMachine.setup(stateModel);
            
            Action action;
            switch (StateMachine.state) {
                case PLACE: 
                    action = PlacementStategy.place(stateModel);
                    break;
                case KILL: 
                    action = KillBehaviourTree.execute(stateModel);
                    break;
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
