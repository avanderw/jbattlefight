package za.co.entelect.challenge;

import com.google.gson.Gson;
import za.co.entelect.challenge.domain.command.Command;
import za.co.entelect.challenge.domain.command.PlaceShipCommand;
import za.co.entelect.challenge.domain.command.Point;
import za.co.entelect.challenge.domain.command.code.Code;
import za.co.entelect.challenge.domain.command.direction.Direction;
import za.co.entelect.challenge.domain.command.ship.ShipType;
import za.co.entelect.challenge.domain.state.GameState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import za.co.entelect.challenge.domain.state.OpponentCell;

public class Bot {

    private String workingDirectory;

    private String key;

    private final String commandFileName = "command.txt";

    private final String placeShipFileName = "place.txt";

    private final String stateFileName = "state.json";

    private final Gson gson;

    public Bot(String key, String workingDirectory) {

        this.workingDirectory = workingDirectory;
        this.key = key;
        this.gson = new Gson();
    }

    public void execute() throws IOException {

        GameState gameState = gson.fromJson(new StringReader(loadState()), GameState.class);

        if (gameState.Phase == 1) {
            PlaceShipCommand placeShipCommand = placeShips(gameState);
            writePlaceShips(placeShipCommand);
        } else {
            Command command = makeMove(gameState);
            writeMove(command);
        }
    }

    private PlaceShipCommand placeShips(GameState state) {
        Placement battleship = new Placement(state).random(ShipType.Battleship);
        Placement carrier = new Placement(state).random(ShipType.Carrier);
        Placement cruiser = new Placement(state).random(ShipType.Cruiser);
        Placement destroyer = new Placement(state).random(ShipType.Destroyer);
        Placement submarine = new Placement(state).random(ShipType.Submarine);
        
        ArrayList<ShipType> shipsToPlace = new ArrayList<>();
        shipsToPlace.add(battleship.type);
        shipsToPlace.add(carrier.type);
        shipsToPlace.add(cruiser.type);
        shipsToPlace.add(destroyer.type);
        shipsToPlace.add(submarine.type);

        ArrayList<Point> points = new ArrayList<>();
        points.add(battleship.point);
        points.add(carrier.point);
        points.add(cruiser.point);
        points.add(destroyer.point);
        points.add(submarine.point);

        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(battleship.direction);
        directions.add(carrier.direction);
        directions.add(cruiser.direction);
        directions.add(destroyer.direction);
        directions.add(submarine.direction);

        return new PlaceShipCommand(shipsToPlace, points, directions);
    }

    private String loadState() throws IOException {

        StringBuilder jsonText = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(workingDirectory, stateFileName)));
        String line = bufferedReader.readLine();
        while (line != null) {
            jsonText.append(line);
            jsonText.append("\r\n");
            line = bufferedReader.readLine();
        }
        return jsonText.toString();
    }

    private Command makeMove(GameState state) {
        if (this.isHunting(state)) {
            System.out.println("Hunting");
            Command defaultHunt = new DefaultPlacement().hunt(state);
            if (!defaultHunt.getCommandCode().equals(Code.DO_NOTHING)) {
                return defaultHunt;
            }
            return new Hunt(state).probabilityShot();
        } else {
            System.out.println("Killing");
            Command defaultKill = new DefaultPlacement().hunt(state);
            if (!defaultKill.getCommandCode().equals(Code.DO_NOTHING)) {
                return defaultKill;
            }
            return new Kill(state).nextShot();
        }
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
    
    private Boolean isHunting(GameState state) {
        return !state.OpponentMap.Cells.stream().filter(c -> c.Damaged && !state.OpponentMap.shotAllAdjacent(c)).findAny().isPresent();

    }
}
