package com.cluster.math.model;

import java.util.ArrayList;

/**
 * Created by envoy on 17.04.2017.
 */
public class Conformation {
    private Bits bits;
    private double energy;
    private ArrayList<Object> vertices;

    public Conformation(Bits bits, ArrayList<Object> vertices, double energy) {
        this.bits = bits;
        this.vertices = vertices;
        this.energy = energy;
    }

    public Bits getBits() {
        return bits;
    }

    public double getEnergy() {
        return energy;
    }

    public ArrayList<Object> getVertices() {
        return vertices;
    }
}
