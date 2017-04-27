package com.cluster.math.utils;

import com.cluster.math.MinsRepository;
import com.cluster.math.TestExecutor;
import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import matlabcontrol.MatlabInvocationException;

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

    public Efficiency(MinsRepository repository, Bits x) throws MatlabInvocationException {
        this.rep = repository;
        this.x = x;
        updateData();
    }

    private void updateData() throws MatlabInvocationException {
        int N = TestExecutor.getConfig().getSTRONGIN_N();
        int M = TestExecutor.getConfig().getSTRONGIN_M();
        int K = TestExecutor.getConfig().getSTRONGIN_K();

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
            conf = findBestConf(new Bits(sb), TestExecutor.getConfig().getINF_SUP_ITERATIONS());
            z = conf.getEnergy();
        } else {
            Conformation confInf = findBestConf(getFirstAtoms(xInf, K), TestExecutor.getConfig().getINF_ITERATIONS());
            Conformation confSup = findBestConf(getFirstAtoms(xSup, K), TestExecutor.getConfig().getSUP_ITERATIONS());
            conf = (confInf.getEnergy() < confSup.getEnergy()) ? confInf : confSup;
            BigDecimal t = new BigDecimal(confSup.getEnergy() - confInf.getEnergy()).setScale(TestExecutor.getConfig().getBIG_DECIMAL_SCALE(), BigDecimal.ROUND_HALF_UP);
            z = confInf.getEnergy() + t.multiply(new BigDecimal(x.getNumber().subtract(xInf.getNumber()).divide(xSup.getNumber().subtract(xInf.getNumber()))).setScale(TestExecutor.getConfig().getBIG_DECIMAL_SCALE())).doubleValue(); //TODO not log?
        }
        rep.tryAddConf(conf);
    }

    //call GrowthAlg or use cache
    private Conformation findBestConf(Bits bits, int iterations) throws MatlabInvocationException {
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
