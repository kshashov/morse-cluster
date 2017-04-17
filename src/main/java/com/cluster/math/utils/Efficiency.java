package com.cluster.math.utils;

import com.cluster.math.MinsRepository;
import com.cluster.math.model.Bits;

/**
 * Created by envoy on 17.04.2017.
 */
public class Efficiency {
    private MinsRepository rep;
    private long x;
    private Bits xInf;
    private Bits xSup;
    private double z;

    public Efficiency(MinsRepository repository, long x) {
        this.rep = repository;
        this.x = x;
        updateData();
    }

    private void updateData() {
        //TODO inf sup z
    }

    public void setX(long x) {
        this.x = x;
        updateData();
    }

    public long getX() {
        return x;
    }

    public Bits getXInf() {
        return xInf;
    }

    public Bits getXSup() {
        return xSup;
    }

    public double getZ() {
        return z;
    }
}
