package net.avdw.battlefight.state;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateReader {

    static public <T> T read(File stateFile, Class<T> clazz) {
        try {
            StringBuilder jsonStringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(stateFile));
            String line = bufferedReader.readLine();
            while (line != null) {
                jsonStringBuilder.append(line);
                jsonStringBuilder.append("\r\n");
                line = bufferedReader.readLine();
            }

            return new Gson().<T>fromJson(jsonStringBuilder.toString(), clazz);
        } catch (IOException ex) {
            Logger.getLogger(StateReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
