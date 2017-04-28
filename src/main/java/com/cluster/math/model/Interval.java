package com.cluster.math.model;

import com.cluster.math.MinsRepository;
import com.cluster.math.Strongin;
import com.cluster.math.utils.Efficiency;

import java.math.BigInteger;

/**
 * Created by envoy on 17.04.2017.
 */
public class Interval {
    private Bits a;
    private Bits b;
    private double f;
    private Efficiency zA;
    private Efficiency zB;
    private MinsRepository rep;

    public Interval(MinsRepository repository, Bits a, Bits b) {
        this.a = a;
        this.b = b;
        this.rep = repository;
        zA = new Efficiency(rep, a);
        zB = new Efficiency(rep, b);
        updateF();
    }

    public Interval(MinsRepository repository, Bits a, Bits b, Efficiency zA, Efficiency zB) {
        this.a = a;
        this.b = b;
        this.rep = repository;
        this.zA = zA;
        this.zB = zB;
        updateF();
    }

    public Bits getA() {
        return a;
    }

    public Bits getB() {
        return b;
    }

    public void setA(Bits a) {
        this.a = a;
        zA = new Efficiency(rep, a);
        updateF();
    }

    public void setA(Bits a, Efficiency zA) {
        this.a = a;
        this.zA = zA;
        updateF();
    }

    public void setB(Bits b) {
        this.b = b;
        zB = new Efficiency(rep, b);
        updateF();
    }

    public void setB(Bits b, Efficiency zB) {
        this.b = b;
        this.zB = zB;
        updateF();
    }

    private void updateF() {
        f = Strongin.calcF(a.getNumber(), b.getNumber(), zA.getZ(), zB.getZ());
    }

    public double getF() {
        return f;
    }

    public Efficiency getZA() {
        return zA;
    }

    public Efficiency getZB() {
        return zB;
    }

    public BigInteger getSize() {
        return b.getNumber().subtract(a.getNumber());
    }
}
