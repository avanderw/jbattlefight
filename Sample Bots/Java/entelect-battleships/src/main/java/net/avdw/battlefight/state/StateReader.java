package net.avdw.battlefight.state;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StateReader {
    static public StateModel read(File stateFile) throws FileNotFoundException, IOException {
        StringBuilder jsonStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(stateFile));
        String line = bufferedReader.readLine();
        while (line != null) {
            jsonStringBuilder.append(line);
            jsonStringBuilder.append("\r\n");
            line = bufferedReader.readLine();
        }
        
        return new Gson().fromJson(jsonStringBuilder.toString(), StateModel.class);
    }
}
