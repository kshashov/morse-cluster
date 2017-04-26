package com.cluster.math;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Interval;
import com.cluster.math.utils.Efficiency;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by envoy on 15.04.2017.
 */
public class Strongin {
    private static final long m = 150;

    public static double calcF(long a, long b, double zA, double zB) {
        double logA = Math.log(a) / Math.log(2);
        double logB = Math.log(b) / Math.log(2);
        return m * (logB - logA) + (Math.pow(zB - zA, 2) / (m * (logB - logA))) - 2 * (zA + zB);
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
            if (i % 100 == 0) {
                System.out.println("Strongin " + i);
            }
            interval = intervals.get(ind);
            double logA = Math.log(interval.getA().getNumber()) / Math.log(2);
            double logB = Math.log(interval.getB().getNumber()) / Math.log(2);
            long x = (interval.getB().getNumber() + interval.getA().getNumber()) / 2; //Math.ceil(Math.pow(2, (logA + logB) / 2.0));
            bitsX = new Bits(TestExecutor.getConfig().getSTRONGIN_M(), x);
            zX = new Efficiency(rep, bitsX.getNumber());
            isUsed = false;

            double logSize = Math.log(TestExecutor.getConfig().getSTRONGIN_EPS() / interval.getA().getNumber() + 1) / Math.log(2);
            if ((logB - logA) / 2.0 < logSize) { //todo check
                intervals.remove(ind);
            } else {
                cacheB = interval.getB();
                cacheZB = interval.getZB();
                if ((zX.getXInf().getNumber() >= interval.getA().getNumber()) && (zX.getXInf().getNumber() <= interval.getB().getNumber())) {
                    interval.setB(bitsX, zX);
                    isUsed = true;
                }

                if ((zX.getXSup().getNumber() >= bitsX.getNumber()) && (zX.getXSup().getNumber() <= cacheB.getNumber())) {
                    if (isUsed) {
                        intervals.add(new Interval(rep, bitsX, cacheB, zX, cacheZB));
                    } else {
                        interval.setA(bitsX, zX);
                        interval.setA(bitsX, zX);
                        isUsed = true;
                    }
                }

                if (!isUsed) {
                    intervals.remove(ind);
                }
            }

            if (intervals.size() == 0) {
                break;
            }

            ind = 0;
            interval = intervals.get(ind);
            for (int j = 0; j < intervals.size(); j++) {
                if (intervals.get(j).getF() > interval.getF()) {
                    ind = j;
                }
            }
        }

        Comparator<Interval> comparator = new Comparator<Interval>() {
            @Override
            public int compare(Interval left, Interval right) {
                return (int) (left.getA().getNumber() - right.getA().getNumber());
            }
        };

        intervals.sort(comparator);
        for (Interval interval1 : intervals) {
            double logA = Math.log(interval1.getA().getNumber()) / Math.log(2);
            double logB = Math.log(interval1.getB().getNumber()) / Math.log(2);
            System.out.println("[" + logA + "; " + logB + "] " + interval1.getF() + " " + interval1.getZA().getZ() + "-" + interval1.getZB().getZ());
        }
        return rep;
    }
}
