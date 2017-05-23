package com.cluster.math.utils;

import com.cluster.Configuration;
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
    private static String startConf;
    private static ArrayList<Integer> indexes;
    private static ArrayList<Vertex> vertices;
    private static Map<String, Conformation> optCache = new HashMap<>();

    public static void init(String startConf, ArrayList<Vertex> vertices, ArrayList<Integer> indexes) {
        ClusterMath.startConf = startConf;
        ClusterMath.vertices = vertices;
        ClusterMath.indexes = indexes;
        optCache.clear();
    }

    public static Conformation calcWithStartConf(String stronginBits, boolean isLocalOpt) {
        return calcE(getFullBits(stronginBits), isLocalOpt);
    }

    public static Conformation calc(String fullBits, boolean isLocalOpt) {
        return calcE(fullBits, isLocalOpt);
    }

    public static double calcEnergyAtomWithStartConf(String stronginBits, int atomIndex) {
        if (atomIndex >= stronginBits.length()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        String fullBits = getFullBits(stronginBits);
        Vertex vertex = vertices.get(indexes.get(atomIndex));

        ArrayList<Vertex> verticesConf = new ArrayList<>();
        for (int i = 0; i < fullBits.length(); i++) {
            if (fullBits.charAt(i) == '1') {
                verticesConf.add(new Vertex(vertices.get(i)));
            }
        }

        double r;
        double energy = 0;
        for (int i = 0; i < verticesConf.size(); i++) {
            if (i != indexes.get(atomIndex)) {
                r = verticesConf.get(i).distanceTo(vertex);
                energy += Math.exp(Configuration.get().getRO() * (1 - r)) * (Math.exp(Configuration.get().getRO() * (1 - r)) - 2);
            }
        }

        return energy;
    }

    public static int calcAdjacentNumberWithStartConf(String stronginBits, int atomIndex) {
        if (atomIndex >= stronginBits.length()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        String fullBits = getFullBits(stronginBits);
        Vertex vertex = vertices.get(indexes.get(atomIndex));

        int count = 0;
        for (int i = 0; i < fullBits.length(); i++) {
            if ((i != indexes.get(atomIndex)) && (fullBits.charAt(i) == '1')) {
                if (vertex.distanceTo(vertices.get(i)) < Configuration.get().getDISTANCE_MIN()) {
                    count++;
                }
            }
        }

        return count;
    }

    private static String getFullBits(String stronginBits) {
        if (stronginBits == null) {
            return startConf;
        }

        if (indexes.size() != stronginBits.length()) {
            throw new IllegalArgumentException("Invalid bits size");
        }

        StringBuilder sb = new StringBuilder(startConf);
        for (int i = 0; i < stronginBits.length(); i++) {
            sb.setCharAt(indexes.get(i), stronginBits.charAt(i));
        }

        return sb.toString();
    }

    private static Conformation calcE(String fullBits, boolean isLocalOpt) {
        if (fullBits.length() != vertices.size()) {
            throw new IllegalArgumentException("Invalid bits size");
        }
        String key = fullBits;
        if (optCache.containsKey(key)) {
            return optCache.get(key);
        }

        ArrayList<Vertex> verticesConf = new ArrayList<>();
        for (int i = 0; i < fullBits.length(); i++) {
            if (fullBits.charAt(i) == '1') {
                verticesConf.add(new Vertex(vertices.get(i)));
            }
        }
        Conformation conf;
        if (isLocalOpt) {
            conf = Lbfgs.minimize(new Conformation(new Bits(fullBits), verticesConf, 0));
        } else {
            double energy = getEnergy(verticesConf);
            conf = new Conformation(new Bits(fullBits), verticesConf, energy);
        }

        if (isLocalOpt && !optCache.containsKey(key)) {
            optCache.put(key, conf);
        }
        return conf;
    }

    public static double getEnergy(ArrayList<Vertex> verticesConf) {
        double r;
        double energy = 0;
        for (int i = 0; i < verticesConf.size() - 1; i++) {
            for (int j = i + 1; j < verticesConf.size(); j++) {
                r = verticesConf.get(i).distanceTo(verticesConf.get(j));
                energy += Math.exp(Configuration.get().getRO() * (1 - r)) * (Math.exp(Configuration.get().getRO() * (1 - r)) - 2);
            }
        }

        return energy;
    }

}
