package com.cluster.math;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;
import com.cluster.math.utils.ClusterMath;
import com.cluster.math.utils.Config;
import matlabcontrol.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by envoy on 05.03.2017.
 */
public class TestExecutor {
    private static MatlabProxy proxy;
    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public static MatlabProxy getMatlabProxy() {
        if (proxy == null) {
            MatlabProxyFactory factory = null;
            MatlabProxyFactoryOptions options =
                    new MatlabProxyFactoryOptions.Builder()
                            .setUsePreviouslyControlledSession(true)
                            .build();
            factory = new MatlabProxyFactory(options);
            try {
                proxy = factory.getProxy();
                proxy.eval("addpath('" + config.getINPUT_PATH() + "')");
            } catch (MatlabConnectionException e) {
                e.printStackTrace();
            } catch (MatlabInvocationException e) {
                e.printStackTrace();
            }
        }
        return proxy;
    }

    public static long[] inf_sup(long x, int N, int M) {
        getMatlabProxy();
        Object[] out = null;
        try {
            out = proxy.returningFeval("inf_sup", 2, x, N, M);
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        }
        double xSup = ((double[]) out[0])[0];
        double xInf = ((double[]) out[1])[0];
        return new long[]{(long) xSup, (long) xInf};
    }

    public static ArrayList<Vertex> localOpt(ArrayList<Vertex> vertices) {
        getMatlabProxy();
        double[] input = new double[vertices.size() * 3];
        int j = 0;
        for (int i = 0; i < vertices.size(); i++) {
            input[j++] = vertices.get(i).getX();
            input[j++] = vertices.get(i).getY();
            input[j++] = vertices.get(i).getZ();
        }

        Object[] out = null;
        try {
            out = proxy.returningFeval("LO", 1, config.getRO(), input);
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        }
        double[] output = (double[]) out[0];
        ArrayList<Vertex> verticesOpt = new ArrayList<>(vertices.size());
        j = 0;
        for (int i = 0; i < vertices.size(); i++) {
            verticesOpt.add(new Vertex(output[j++], output[j++], output[j++]));
        }
        return verticesOpt;
    }

    public static void main(String[] args) throws MatlabInvocationException {
        long time = System.currentTimeMillis();
        setupConfig();
        //interval
        StringBuilder stronginOnes = new StringBuilder();
        for (int i = 0; i < config.getSTRONGIN_N(); i++) {
            stronginOnes.append('1');
        }
        StringBuilder stronginZeros = new StringBuilder();
        for (int i = 0; i < config.getSTRONGIN_M() - config.getSTRONGIN_N(); i++) {
            stronginZeros.append('0');
        }
        StringBuilder sbA = new StringBuilder(stronginZeros).append(stronginOnes);
        StringBuilder sbB = new StringBuilder(stronginOnes).append(stronginZeros);

        Strongin strongin = new Strongin();
        MinsRepository rep = strongin.solve(new Bits(sbA), new Bits(sbB), config.getSTRONGIN_ITERATIONS(), config.getSTRONGIN_REPOSITORY_SIZE());

        //result
        for (Conformation conformation : rep.getMins()) {
            System.out.println(conformation.getEnergy() + " -- " + conformation.getBits().getBites());
        }

        //close all
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s.");
        proxy.eval("rmpath('" + config.getINPUT_PATH() + "')");
        proxy.disconnect();
    }

    private static ArrayList<Vertex> readVertices(File file) {
        ArrayList<Vertex> list = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextDouble()) {
                list.add(new Vertex(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static void setupConfig() {
        config = new Config("D:\\WORKSPACE\\MorseCluster\\", "input.txt", "111111100001110001111100000000000000010000111000111111", 38);

        int n2 = 0;
        Bits startConf = new Bits(new StringBuilder(config.getSTART_CONF()));
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < startConf.getSize(); i++) {
            if (startConf.get(i) == '0') {
                indexes.add(i);
            } else {
                n2++;
            }
        }
        ArrayList<Vertex> vertices = readVertices(new File(config.getINPUT_PATH() + config.getINPUT_FILENAME()));
        ClusterMath.init(startConf, vertices, indexes);

        //strongin
        config.setSTRONGIN_N(config.getN() - n2);
        config.setSTRONGIN_M(indexes.size());
    }
}
