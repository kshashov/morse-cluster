package com.cluster.model;


import com.cluster.math.model.Vertex;

import java.util.List;

/**
 * Created by envoy on 09.03.2017.
 */
public class Decision {
    private String title;
    private final Double energy;
    private final Double p;
    private List<Vertex> coords;

    public Decision(Double p, Double energy,List<Vertex> coords) {
        this.p = p;
        this.energy = energy;
        this.coords = coords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getEnergy() {
        return energy;
    }

    public Double getP() {
        return p;
    }

    public List<Vertex> getCoords() {
        return coords;
    }
}
