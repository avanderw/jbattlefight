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
            PlaceShipCommand placeShipCommand = placeShips();
            writePlaceShips(placeShipCommand);
        } else {
            Command command = makeMove(gameState);
            writeMove(command);
        }
    }

    private PlaceShipCommand placeShips() {

        ArrayList<ShipType> shipsToPlace = new ArrayList<>();
        shipsToPlace.add(ShipType.Battleship);
        shipsToPlace.add(ShipType.Carrier);
        shipsToPlace.add(ShipType.Cruiser);
        shipsToPlace.add(ShipType.Destroyer);
        shipsToPlace.add(ShipType.Submarine);

        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 0));
        points.add(new Point(3, 1));
        points.add(new Point(4, 2));
        points.add(new Point(7, 3));
        points.add(new Point(1, 8));

        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.North);
        directions.add(Direction.East);
        directions.add(Direction.North);
        directions.add(Direction.North);
        directions.add(Direction.East);

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
            return new Hunt(state).randomShot();
        } else {
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

    private void log(String message) {

        System.out.println(String.format("[BOT]\t%s", message));
    }

    private Boolean isHunting(GameState state) {
        return !state.OpponentMap.Cells.stream().filter(c -> c.Damaged && !state.OpponentMap.shotAllAdjacent(c)).findAny().isPresent();

    }
}
