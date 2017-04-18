package com.cluster.math.utils;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by envoy on 16.04.2017.
 */
public class ClusterMath {
    private static Bits startConf;
    private static ArrayList<Integer> indexes;
    private static ArrayList<Object> blablaVertices;
    private static Map<String, Conformation> optCache = new HashMap<>();

    public static void init(Bits startConf, ArrayList<Object> blablaVertices, ArrayList<Integer> indexes) {
        ClusterMath.startConf = startConf;
        ClusterMath.blablaVertices = blablaVertices;
        ClusterMath.indexes = indexes;
    }

    public static Conformation calcWithStartConf(Bits bits, boolean isLocalOpt) {
        if (indexes.size() != bits.getSize()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        StringBuilder sb = new StringBuilder(startConf.getBites());
        for (int i = 0; i < bits.getSize(); i++) {
            sb.setCharAt(indexes.get(i), bits.get(i));
        }
        return calcE(new Bits(startConf.getSize(), sb), isLocalOpt);
    }

    public static Conformation calcE(Bits bits, boolean isLocalOpt) {
        if (bits.getSize() != blablaVertices.size()) {
            throw new IllegalArgumentException("Invalid bits size");
        }
        String key = bits.getBites().toString();
        if (optCache.containsKey(key)) {
            return optCache.get(key);
        }

        //TODO bits + blablaVertexes = energy
        double energy = 0.0;
        ArrayList<Object> vertices = isLocalOpt ? null : null;

        Conformation conf = new Conformation(bits, vertices, energy);
        if (isLocalOpt && !optCache.containsKey(key)) {
            optCache.put(key, conf);
        }
        return conf;
    }

}
