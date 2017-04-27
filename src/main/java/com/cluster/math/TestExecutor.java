package com.cluster.math;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;
import com.cluster.math.utils.ClusterMath;
import com.cluster.math.utils.Config;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import matlabcontrol.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by envoy on 05.03.2017.
 */
public class TestExecutor {
    private static MatlabProxy proxy;
    private static Config config;
    public static PrintStream log;
    private static String MATLAB_FOLDER = "D:\\WORKSPACE\\MorseCluster\\";

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
                proxy.eval("addpath('" + MATLAB_FOLDER + "')");
            } catch (MatlabConnectionException e) {
                e.printStackTrace();
            } catch (MatlabInvocationException e) {
                e.printStackTrace();
            }
        }
        return proxy;
    }

   /* public static long[] inf_sup(long x, int N, int M) {
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
    }*/

    public static ArrayList<Vertex> localOpt(ArrayList<Vertex> vertices) throws MatlabInvocationException {
        double[] input = new double[vertices.size() * 3];
        int j = 0;
        for (int i = 0; i < vertices.size(); i++) {
            input[j++] = vertices.get(i).getX();
            input[j++] = vertices.get(i).getY();
            input[j++] = vertices.get(i).getZ();
        }

        Object[] out = proxy.returningFeval("LO", 1, config.getRO(), input);

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
        String userDir = System.getProperty("user.dir") + "\\";
        String configPath = userDir + "config.json";
        String logPath = userDir + "log.txt";
        System.out.println(logPath);
        if (args.length > 0) {
            configPath = args[0];
        }

        try {
            log = new PrintStream(new File(logPath));
            setupConfig(new FileReader(configPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }


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
        strongin.solve(new Bits(sbA), new Bits(sbB), config.getSTRONGIN_ITERATIONS(), config.getSTRONGIN_REPOSITORY_SIZE());

        showResult(strongin);
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s.");
    }

    private static void showResult(Strongin strongin) throws MatlabInvocationException {
        //result
        getMatlabProxy();
        Conformation opt;
        int i = 1;
        PrintWriter out;
        File directory = new File(config.getOUTPUT_FOLDERNAME());
        if (!directory.exists()) {
            directory.mkdir();
        }
        for (Conformation conformation : strongin.getRep().getMins()) {
            opt = ClusterMath.calc(conformation.getBits(), true);
            try {
                String name = "[" + (i++) + "] N" + config.getN() + " M" + config.getM() + " " + opt.getEnergy();
                out = new PrintWriter(directory.getAbsolutePath() + "\\" + name.replace(".", ",") + ".txt");
                out.print(opt);
                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            System.out.println(opt.getEnergy() + " -- " + opt.getBits().getBites());
        }

        //close all
        proxy.eval("rmpath('" + MATLAB_FOLDER + "')");
        proxy.disconnect();
    }

    private static ArrayList<Vertex> readVertices(File file) throws FileNotFoundException {
        ArrayList<Vertex> list = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextDouble()) {
            list.add(new Vertex(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
        }

        return list;
    }

    private static void setupConfig(FileReader fileReader) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(fileReader);
        config = gson.fromJson(reader, Config.class);

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
        ArrayList<Vertex> vertices = readVertices(new File(config.getINPUT_FILENAME()));
        ClusterMath.init(startConf, vertices, indexes);

        //strongin config
        config.setSTRONGIN_N(config.getN() - n2);
        config.setSTRONGIN_M(indexes.size());
        config.init();
    }
}
