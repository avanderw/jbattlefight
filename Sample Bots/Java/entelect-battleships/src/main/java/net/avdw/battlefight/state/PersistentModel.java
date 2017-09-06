package net.avdw.battlefight.state;

public class PersistentModel {
    public Action lastAction;
    
    static public class Action {
        public int x;
        public int y;
        public int type;
    }
}
