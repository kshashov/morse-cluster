package com.cluster.math.utils;

import com.cluster.Configuration;
import com.cluster.math.MinsRepository;
import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;

import java.math.BigDecimal;

/**
 * Created by envoy on 17.04.2017.
 */
public class Efficiency {
    private MinsRepository rep;
    private Bits x;
    private Bits xInf;
    private Bits xSup;
    private double z;

    public Efficiency(MinsRepository repository, Bits x) {
        this.rep = repository;
        this.x = x;
        updateData();
    }

    private void updateData() {
        int N = Configuration.get().getSTRONGIN_N();
        int M = Configuration.get().getSTRONGIN_M();
        int K = Configuration.get().getSTRONGIN_K();

        StringBuilder[] res = InfSupFinder.findInfSup(x.getBites().toString(), N, M);
        xSup = new Bits(res[0]);
        xInf = new Bits(res[1]);

        int k = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            if ((k < K) && (xInf.get(i) == xSup.get(i)) && (xInf.get(i) == '1')) {
                k++;
                sb.append('1');
            } else {
                sb.append('0');
            }
        }
        Conformation conf;
        if (k == K) {
            conf = findBestConf(new Bits(sb), Configuration.get().getINF_SUP_ITERATIONS());
            z = conf.getEnergy();
        } else {
            Conformation confInf = findBestConf(getFirstAtoms(xInf, K), Configuration.get().getINF_ITERATIONS());
            Conformation confSup = findBestConf(getFirstAtoms(xSup, K), Configuration.get().getSUP_ITERATIONS());
            conf = (confInf.getEnergy() < confSup.getEnergy()) ? confInf : confSup;
            BigDecimal t = new BigDecimal(confSup.getEnergy() - confInf.getEnergy()).setScale(Configuration.get().getBIG_DECIMAL_SCALE(), BigDecimal.ROUND_HALF_UP);
            z = confInf.getEnergy() + t.multiply(new BigDecimal(x.getNumber().subtract(xInf.getNumber()).divide(xSup.getNumber().subtract(xInf.getNumber()))).setScale(Configuration.get().getBIG_DECIMAL_SCALE())).doubleValue(); //TODO not log?
        }
        rep.tryAddConf(conf);
    }

    //call GrowthAlg or use cache
    private Conformation findBestConf(Bits bits, int iterations) {
        String key = bits.getBites().toString();
        if (!rep.getCache().containsKey(key)) {
            rep.getCache().put(key, GrowthAlg.buildBestConf(bits, Configuration.get().getSTRONGIN_N(), iterations));
        }

        return rep.getCache().get(key);
    }

    //get subBits with first k ones
    private Bits getFirstAtoms(Bits bits, int K) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (int i = 0; i < bits.getSize(); i++) {
            sb.append(k < K ? bits.get(i) : '0');
            if (bits.get(i) == '1') {
                k++;
            }
        }
        return new Bits(sb);
    }

    public Bits getX() {
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
