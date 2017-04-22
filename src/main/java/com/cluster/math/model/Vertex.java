package com.cluster.math.model;

/**
 * Created by envoy on 09.03.2017.
 */
public class Vertex {
    public double x, y, z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vertex vertex) {
        this(vertex.x, vertex.y, vertex.z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double distanceTo(Vertex vertex) {
        double rx = (x - vertex.x);
        double ry = (y - vertex.y);
        double rz = (z - vertex.z);
        return Math.sqrt(rx * rx + ry * ry + rz * rz);
    }
}
