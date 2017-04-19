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
        //TODO code
        return 0.0;
    }
}
