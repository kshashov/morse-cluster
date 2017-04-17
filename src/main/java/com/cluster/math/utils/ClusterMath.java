package com.cluster.math.utils;

import com.cluster.math.model.Bits;

import java.util.ArrayList;

/**
 * Created by envoy on 16.04.2017.
 */
public class ClusterMath {
    private static Bits startConf;
    private static ArrayList<Integer> indexes;
    private static ArrayList<Object> blablaVertexes;

    public static void init(Bits startConf,  ArrayList<Object> blablaVertexes, ArrayList<Integer> indexes) {
        ClusterMath.startConf = startConf;
        ClusterMath.blablaVertexes = blablaVertexes;
        ClusterMath.indexes = indexes;
    }

    public static double calcWithStartConf(Bits bits) {
        if (indexes.size() != bits.getSize()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        StringBuilder sb = new StringBuilder(startConf.getBites());
        for (int i = 0; i < bits.getSize(); i++) {
            sb.setCharAt(indexes.get(i), bits.get(i));
        }
        Bits conf = new Bits(startConf.getSize(), sb);

        return calcE(conf);
    }

    public static double calcE(Bits bits) {
        if (bits.getSize() != blablaVertexes.size()) {
            throw new IllegalArgumentException("Invalid bits size");
        }
        //TODO conf + blablaVertexes = energy
        return 0.0;
    }
}
