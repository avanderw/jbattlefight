package net.avdw.battlefight;

import com.google.gson.Gson;
import net.avdw.battlefight.struct.Action;
import net.avdw.battlefight.state.StateResolver;
import net.avdw.battlefight.kill.KillDecision;
import net.avdw.battlefight.place.PlacementDecision;
import net.avdw.battlefight.hunt.HuntDecision;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.avdw.battlefight.place.PlaceAction;
import net.avdw.battlefight.state.PersistentModel;
import net.avdw.battlefight.state.StateModel;
import net.avdw.battlefight.state.StateReader;
import net.avdw.battlefight.state.StateWriter;
import net.avdw.battlefight.struct.Point;

public class Main {

    public static void main(String[] args) throws IOException, Exception {
        long startTime = System.currentTimeMillis();
        String workingDirectory = args[1];

        try {
            StateModel state = StateReader.read(new File(workingDirectory, "state.json"), StateModel.class);

            if (state.Phase == 1 && new File("persistent.json").exists()) {
                new File("persistent.json").delete();
            }
            PersistentModel persist = null;
            if (new File("persistent.json").exists()) {
                persist = StateReader.read(new File("persistent.json"), PersistentModel.class);
                if (persist.unclearedHits == null) {
                    persist.unclearedHits = new ArrayList();
                }
                if (persist.clearedHits == null) {
                    persist.clearedHits = new ArrayList();
                }
            } else {
                persist = new PersistentModel();
                persist.unclearedHits = new ArrayList();
                persist.clearedHits = new ArrayList();
            }

            StateResolver.setup(state, persist);
            Action action = null;
            switch (StateResolver.state) {
                case PLACE:
                    action = PlacementDecision.place(state);
                    break;
                case KILL:
                    System.out.println("Killing");
                    action = KillDecision.execute(state, persist);
                    if (action != null) {
                        break;
                    }
                case HUNT:
                    System.out.println("Hunting");
                    persist.lastHeading = null;
                    if (persist.lastState == StateResolver.AiState.KILL) {
                        persist.clearedHits.addAll(persist.unclearedHits);
                        persist.unclearedHits.clear();
                    }
                    action = HuntDecision.execute(state);
                    break;
            }

            if (action == null) {
                System.out.println("Killing failed! Hunting as fallback.");
                action = HuntDecision.execute(state);
            } else if (state.Phase != 1) {
                final Point p = action.point;
                final Action a = action;
                if (state.OpponentMap.Cells.stream().anyMatch(cell -> cell.Y == p.y && cell.X == p.x && (cell.Damaged || cell.Missed) && a.type == Action.Type.FIRESHOT)) {
                    throw new Exception("Something fucked out! Shooting a shot space.");
                }
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, action.filename)))) {
                bufferedWriter.write(action.toString());
                bufferedWriter.flush();
            }

            System.out.println(action);
            System.out.println(action.type);
            persist.lastState = StateResolver.state;
            if (!(action instanceof PlaceAction)) {
                persist.lastAction = new PersistentModel.Action();
                System.out.println(action.type.name());
                persist.lastAction.type = Action.Type.valueOf(action.type.name());
                persist.lastAction.x = action.point.x;
                persist.lastAction.y = action.point.y;
            }
            persist.clearedHits = persist.clearedHits.stream().distinct().collect(Collectors.toList());
            persist.unclearedHits = persist.unclearedHits.stream().distinct().collect(Collectors.toList());
            System.out.println(new Gson().toJson(persist));
            StateWriter.write("persistent.json", persist);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("[Bot]\tBot finished in %d ms.", (endTime - startTime)));
        }
    }
}
