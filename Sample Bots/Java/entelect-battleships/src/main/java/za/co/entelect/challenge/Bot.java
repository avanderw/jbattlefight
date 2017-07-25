package za.co.entelect.challenge;

import za.co.entelect.challenge.strategy.DefaultPlacementAttackStrategy;
import com.google.gson.Gson;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.PlaceShipCommand;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.state.GameState;

import java.io.*;
import za.co.entelect.challenge.strategy.AvoidPlacementStrategy;

public class Bot {

    private String workingDirectory;


    private final String commandFileName = "command.txt";

    private final String placeShipFileName = "place.txt";

    private final String stateFileName = "state.json";

    private final Gson gson;

    public Bot(String key, String workingDirectory) {

        this.workingDirectory = workingDirectory;
        this.gson = new Gson();
    }

    public void execute() throws IOException {

        GameState gameState = gson.fromJson(new StringReader(loadState()), GameState.class);

        if (gameState.Phase == 1) {
            PlaceShipCommand placeShipCommand = placeShips(gameState);
            writePlaceShips(placeShipCommand);
        } else {
            Command command = makeMove(gameState);
            System.out.println(command);
            writeMove(command);
        }
    }

    private PlaceShipCommand placeShips(GameState state) {
        return AvoidPlacementStrategy.placeShips(state);
    }

    String loadState() throws IOException {

        StringBuilder jsonText = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(workingDirectory, stateFileName)));
        String line = bufferedReader.readLine();
        while (line != null) {
            jsonText.append(line);
            jsonText.append("\r\n");
            line = bufferedReader.readLine();
        }
        System.out.println(jsonText.toString());
        return jsonText.toString();
    }

    private Command makeMove(GameState state) {
        Command move = new DefaultPlacementAttackStrategy().hunt(state);
        if (!move.getCommandCode().equals(Code.DO_NOTHING)) {
            System.out.println("Hunting default placement, "+move);
            return move;
        }
        
        System.out.println("");
        move = new Kill(state).nextShot();
        if (!move.getCommandCode().equals(Code.DO_NOTHING)) {
            System.out.println("Found kill shot, " + move);
            return move;
        }
        
        System.out.println("");
        System.out.println("Hunting...");
        return new Hunt(state).probabilityShot();
    }

    private void writeMove(Command command) throws IOException {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, commandFileName)))) {
            bufferedWriter.write(command.toString());
            bufferedWriter.flush();
        }
    }

    private void writePlaceShips(PlaceShipCommand placeShipCommand) throws IOException {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(workingDirectory, placeShipFileName)))) {
            bufferedWriter.write(placeShipCommand.toString());
            bufferedWriter.flush();
        }
    }
}
