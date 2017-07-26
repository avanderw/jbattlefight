package net.avdw.battlefight.state;

public class StateResolver {

    public static AiState state;

    public enum AiState {
        PLACE, HUNT, KILL;
    }

    static public void setup(StateModel stateModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
