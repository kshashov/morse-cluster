package com.cluster.math.utils;

import com.cluster.math.TestExecutor;
import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;
import matlabcontrol.MatlabInvocationException;

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

    public static void init(Bits startConf, ArrayList<Vertex> blablaVertices, ArrayList<Integer> indexes) {
        ClusterMath.startConf = startConf;
        ClusterMath.vertices = blablaVertices;
        ClusterMath.indexes = indexes;
    }

    public static Conformation calcWithStartConf(String stronginBits, boolean isLocalOpt) throws MatlabInvocationException {
        return calcE(getFullBits(stronginBits), isLocalOpt);
    }

    public static Conformation calc(Bits fullBits, boolean isLocalOpt) throws MatlabInvocationException {
        return calcE(fullBits, isLocalOpt);
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
                if (vertex.distanceTo(vertices.get(i)) < TestExecutor.getConfig().getDISTANCE_MIN()) {
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

        return new Bits(sb);
    }

    private static Conformation calcE(Bits fullBits, boolean isLocalOpt) throws MatlabInvocationException {
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
                verticesConf.add(new Vertex(vertices.get(i)));
            }
        }

        ArrayList<Vertex> verticesOpt = isLocalOpt ? TestExecutor.localOpt(verticesConf) : verticesConf; //TODO local opt

        double energy = getEnergy(verticesOpt);
        Conformation conf = new Conformation(fullBits, verticesOpt, energy);
        if (isLocalOpt && !optCache.containsKey(key)) {
            optCache.put(key, conf);
        }
        return conf;
    }

    private static double getEnergy(ArrayList<Vertex> verticesConf) {
        double r;
        double energy = 0;
        for (int i = 0; i < verticesConf.size() - 1; i++) {
            for (int j = i + 1; j < verticesConf.size(); j++) {
                r = verticesConf.get(i).distanceTo(verticesConf.get(j));
                energy += Math.exp(TestExecutor.getConfig().getRO() * (1 - r)) * (Math.exp(TestExecutor.getConfig().getRO() * (1 - r)) - 2);
            }
        }

        return energy;
    }

}
