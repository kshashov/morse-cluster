package com.cluster.math.model;

import java.util.ArrayList;

/**
 * Created by envoy on 17.04.2017.
 */
public class Conformation {
    private Bits bits;
    private double energy;
    private ArrayList<Vertex> vertices;

    public Conformation(Bits bits, ArrayList<Vertex> vertices, double energy) {
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

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bits.getBites()).append(System.lineSeparator()).append("E = ").append(getEnergy()).append(System.lineSeparator());
        for (Vertex vertex : vertices) {
            sb.append(vertex.getX()).append(" ").append(vertex.getY()).append(" ").append(vertex.getZ()).append(System.lineSeparator());
        }
        return sb.toString().replace(".", ",");
    }
}
