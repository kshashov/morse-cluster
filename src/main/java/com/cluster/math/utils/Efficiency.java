package com.cluster.math.utils;

import com.cluster.math.MinsRepository;
import com.cluster.math.Strongin;
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
        int N = Strongin.getN();
        int M = Strongin.getM();
        //TODO inf sup
        //TODO z
        //1. generate startConf with K atoms
        //2. call growAlg api
        //3. get Conformation
        //4. try add Conformation to repository
        //4. calc z
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
