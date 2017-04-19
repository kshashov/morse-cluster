package com.cluster.math.utils;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by envoy on 16.04.2017.
 */
public class ClusterMath {
    private static Bits startConf;
    private static ArrayList<Integer> indexes;
    private static ArrayList<Vertex> vertices;
    private static Map<String, Conformation> optCache = new HashMap<>();
    private static double DISTANCE_MIN = 1.1;

    public static void init(Bits startConf, ArrayList<Vertex> blablaVertices, ArrayList<Integer> indexes) {
        ClusterMath.startConf = startConf;
        ClusterMath.vertices = blablaVertices;
        ClusterMath.indexes = indexes;
    }

    public static Conformation calcWithStartConf(String stronginBits, boolean isLocalOpt) {
        return calcE(getFullBits(stronginBits), isLocalOpt);
    }

    public static int calcAdjacentNunberWithStartConf(String stronginBits, int atomIndex) {
        if (atomIndex >= stronginBits.length()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        Bits fullBits = getFullBits(stronginBits);
        Vertex vertex = vertices.get(indexes.get(atomIndex));

        int count = 0;
        for (int i = 0; i < fullBits.getSize(); i++) {
            if ((i != indexes.get(atomIndex)) && (fullBits.get(i) == '1')) {
                if (vertex.distanceTo(vertices.get(indexes.get(i))) < DISTANCE_MIN) {
                    count++;
                }
            }
        }

        return count;
    }

    private static Bits getFullBits(String stronginBits) {
        if (indexes.size() != stronginBits.length()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        StringBuilder sb = new StringBuilder(startConf.getBites());
        for (int i = 0; i < stronginBits.length(); i++) {
            sb.setCharAt(indexes.get(i), stronginBits.charAt(i));
        }

        return new Bits(startConf.getSize(), sb);
    }

    private static Conformation calcE(Bits fullBits, boolean isLocalOpt) {
        if (fullBits.getSize() != vertices.size()) {
            throw new IllegalArgumentException("Invalid bits size");
        }
        String key = fullBits.getBites().toString();
        if (optCache.containsKey(key)) {
            return optCache.get(key);
        }

        ArrayList<Vertex> verticesConf = new ArrayList<>();
        for (int i = 0; i < fullBits.getSize(); i++) {
            if (fullBits.get(i) == '1') {
                verticesConf.add(vertices.get(i));
            }
        }

        ArrayList<Vertex> verticesOpt = null;
        if (!isLocalOpt) {
            verticesOpt = verticesConf;
        } else {
            //TODO local optimization
            verticesOpt = verticesConf;
        }

        double energy = getEnergy(verticesConf);

        Conformation conf = new Conformation(fullBits, verticesOpt, energy);
        if (isLocalOpt && !optCache.containsKey(key)) {
            optCache.put(key, conf);
        }
        return conf;
    }

    private static double getEnergy(ArrayList<Vertex> verticesConf) {
        //TODO vertices = energy
        return 0.0;
    }

}
