package com.cluster.math.utils;

import com.cluster.math.MinsRepository;
import com.cluster.math.Strongin;
import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;

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

        int k = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            if ((k < Strongin.getK()) && (xInf.get(i) == xSup.get(i)) && (xInf.get(i) == '1')) {
                k++;
                sb.append('1');
            } else {
                sb.append('0');
            }
        }

        if (k == Strongin.getK()) {
            Conformation conf = findBestConf(new Bits(sb.length(), sb), 10);
            rep.tryAddConf(conf);
            z = conf.getEnergy();
        } else {
            Conformation confInf = findBestConf(getFirstAtoms(xInf, Strongin.getK()), 10);
            Conformation confSup = findBestConf(getFirstAtoms(xSup, Strongin.getK()), 10);

            z = confInf.getEnergy() + (confSup.getEnergy() - confInf.getEnergy()) * (x - xInf.getNumber()) / (xSup.getNumber() - xInf.getNumber());
            rep.tryAddConf((confInf.getEnergy() < confSup.getEnergy()) ? confInf : confSup);
        }
    }

    //call GrowthAlg or use cache
    private Conformation findBestConf(Bits bits, int iterations) {
        String key = bits.getBites().toString();
        if (rep.getCache().containsKey(key)) {
            return rep.getCache().get(key);
        }
        Conformation conf = GrowthAlg.buildBestConf(bits, Strongin.getN(), iterations);

        rep.getCache().put(key, conf);
        return conf;
    }

    //get subBits with first k ones
    private Bits getFirstAtoms(Bits bits, int K) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (int i = 0; i < sb.length(); i++) {
            sb.append(k < K ? bits.get(i) : '0');
            if (bits.get(i) == '1') {
                k++;
            }
        }
        return new Bits(sb.length(), sb);
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
