package net.avdw.battlefight.hunt;

import net.avdw.battlefight.state.StateModel;

class HuntMask {

    boolean[][] mask;
   
    HuntMask(int size) {
        System.out.println("Override mask length: " + size);

        mask = new boolean[14][14];
        for (int y = 0; y < mask.length; y++) {
            for (int x = 0; x < mask.length; x++) {
                mask[y][x] = ((x + y) % size) == 0;
            }
        }
        
        System.out.println(this);
    }
    
    HuntMask(StateModel stateModel) {
        int minLength = stateModel.OpponentMap.Ships.stream().filter(ship -> !ship.Destroyed)
                .mapToInt(ship -> ship.ShipType.length())
                .max().getAsInt();

        System.out.println("Hunting for ship length: " + minLength);

        mask = new boolean[14][14];
        for (int y = 0; y < mask.length; y++) {
            for (int x = 0; x < mask.length; x++) {
                mask[y][x] = ((x + y) % minLength) == 0;
            }
        }
        
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 13; y >= 0; y--) {
            for (int x = 0; x < mask.length; x++) {
                sb.append(mask[y][x] ? "1" : "0");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
