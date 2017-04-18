package com.cluster.math.utils;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by envoy on 18.04.2017.
 */
public class GrowthAlg {
    public static Conformation buildBestConf(Bits bits, int n, int iterations) {
        Conformation conf = null;

        Map<String, Conformation> results = new HashMap<>();
        buildConfRecursive(bits, n, iterations, results);
        for (int i = 0; i < results.size(); i++) {
            if ((conf == null) || (conf.getEnergy() > results.get(0).getEnergy())) {
                conf = results.get(0);
            }
        }
        return conf;
    }

    private static void buildConfRecursive(final Bits bits, final int n, int iterations, final Map<String, Conformation> results) {
        //TODO add some logic
        if (results.containsKey(bits.getBites().toString())) {
            return;
        }

        int currSize = 0;
        for (int i = 0; i < bits.getSize(); i++) {
            if (bits.get(i) == '1') currSize++;
        }

        if (currSize == n) {
            results.put(bits.getBites().toString(), ClusterMath.calcWithStartConf(bits, true));
            return;
        }

        ArrayList<Bits> temp = findBestAdjacentAtom(bits);

        int itersMin = 1;
        if (iterations > 0) {
            itersMin = (temp.size() < iterations) ? temp.size() : iterations;
            iterations -= itersMin;
        }

        for (int i = 0; i < itersMin; i++) {
            buildConfRecursive(temp.get(i), n, iterations, results);
        }
    }

    public static ArrayList<Bits> findBestAdjacentAtom(Bits startBits) {
        ArrayList<Bits> list = new ArrayList<>();

        ArrayList<Bits> adjacentList = new ArrayList<>();
        //TODO move smegH_2017 to adjacentList

        for (Bits bits : adjacentList) {
            //TODO TOP_MAX_2017 from matlab to list
        }

        return list;
    }
}
