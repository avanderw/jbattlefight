/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avdw.battlefight.struct;

/**
 *
 * @author Andrew
 */
public class PotentialFieldPoint {
    public int x;
    public int y;
    public int potential;
    
    public PotentialFieldPoint(int x, int y, int potential) {
        this.x = x;
        this.y = y;
        this.potential = potential;
    }
}
