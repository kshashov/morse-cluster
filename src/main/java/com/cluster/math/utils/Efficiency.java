package com.cluster.math.utils;

import com.cluster.math.MinsRepository;
import com.cluster.math.TestExecutor;
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
        int N = TestExecutor.getConfig().getSTRONGIN_N();
        int M = TestExecutor.getConfig().getSTRONGIN_M();
        int K = TestExecutor.getConfig().getSTRONGIN_K();

        //TODO inf sup
        long[] res = TestExecutor.inf_sup(x, N, M);
        xSup = new Bits(M, res[0]);
        xInf = new Bits(M, res[1]);

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

        if (k == K) {
            Conformation conf = findBestConf(new Bits(sb), TestExecutor.getConfig().getINF_SUP_ITERATIONS());
            rep.tryAddConf(conf);
            z = conf.getEnergy();
        } else {
            Conformation confInf = findBestConf(getFirstAtoms(xInf, K), TestExecutor.getConfig().getINF_ITERATIONS());
            Conformation confSup = findBestConf(getFirstAtoms(xSup, K), TestExecutor.getConfig().getSUP_ITERATIONS());

            z = confInf.getEnergy() + (confSup.getEnergy() - confInf.getEnergy()) * (x - xInf.getNumber()) / (xSup.getNumber() - xInf.getNumber());
            rep.tryAddConf((confInf.getEnergy() < confSup.getEnergy()) ? confInf : confSup);
        }
    }

    //call GrowthAlg or use cache
    private Conformation findBestConf(Bits bits, int iterations) {
        String key = bits.getBites().toString();
        if (!rep.getCache().containsKey(key)) {
            rep.getCache().put(key, GrowthAlg.buildBestConf(bits, TestExecutor.getConfig().getSTRONGIN_N(), iterations));
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
