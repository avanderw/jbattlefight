package net.avdw.battlefight.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateWriter {

    static public void write(String filename, Object content) {
        try (Writer writer = new FileWriter(new File(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(content, writer);
        } catch (IOException ex) {
            Logger.getLogger(StateWriter.class.getName()).log(Level.SEVERE, null, ex);
            try {
                new File("error").createNewFile();
            } catch (IOException ex1) {
                Logger.getLogger(StateWriter.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
