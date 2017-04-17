package com.cluster.math;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Interval;
import com.cluster.math.utils.Efficiency;

import java.util.ArrayList;

/**
 * Created by envoy on 15.04.2017.
 */
public class Strongin {
    private static final int m = 150;
    private static final double eps = 20000;
    private static int N = 0;
    private static int M = 0;
    private static int K = 0;

    public static double calcF(long a, long b, double zA, double zB) {
        return m * (b - a) + (Math.pow(zB - zA, 2) / (m * (b - a))) - 2 * (zA + zB);
    }

    public MinsRepository solve(Bits a, Bits b, final int iterations, int sizeMins) {
        MinsRepository rep = new MinsRepository(sizeMins);
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(rep, a, b));

        int ind = 0;
        Interval interval = null;
        Efficiency zX = null;
        Bits bitsX = null;
        boolean isUsed = false;
        Bits cacheB = null;
        Efficiency cacheZB = null;
        for (int i = 0; i < iterations; i++) {
            interval = intervals.get(ind);
            bitsX = new Bits(M, (long) Math.ceil((interval.getA().getNumber() + interval.getB().getNumber()) / 2.0));
            zX = new Efficiency(rep, bitsX.getNumber());

            if (interval.getSize() < eps) { //todo check
                intervals.remove(ind);
            } else {
                isUsed = false;
                cacheB = interval.getB();
                cacheZB = interval.getZB();
                if (zX.getXInf().getNumber() >= interval.getA().getNumber()) {
                    interval.setB(bitsX, zX);
                    intervals.add(interval);
                    isUsed = true;
                }

                if (zX.getXSup().getNumber() >= bitsX.getNumber()) {
                    if (isUsed) {
                        intervals.add(new Interval(rep, bitsX, cacheB, zX, cacheZB));
                    } else {
                        interval.setA(bitsX, zX);
                        intervals.add(interval);
                        isUsed = true;
                    }
                }

                if (!isUsed) {
                    intervals.remove(ind);
                }
            }

            ind = 0;
            interval = intervals.get(ind);
            for (int j = 0; j < intervals.size(); j++) {
                if (intervals.get(j).getF() > interval.getF()) {
                    ind = j;
                }
            }
        }
        return rep;
    }

    public static int getN() {
        return N;
    }

    public static int getM() {
        return M;
    }

    public static int getK() {
        return K;
    }
}
