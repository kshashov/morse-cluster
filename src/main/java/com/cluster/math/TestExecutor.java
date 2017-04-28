package com.cluster.math;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;
import com.cluster.math.utils.ClusterMath;
import com.cluster.math.utils.Config;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by envoy on 05.03.2017.
 */
public class TestExecutor {
    private static Config config;
    public static PrintStream log;

    public static Config getConfig() {
        return config;
    }

    public static void main(String[] args) {
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

    private static void showResult(Strongin strongin) {
        //result
        Conformation opt;
        int i = 1;
        PrintWriter out;
        File directory = new File(config.getOUTPUT_FOLDERNAME());
        if (!directory.exists()) {
            directory.mkdir();
        }
        for (Conformation conformation : strongin.getRep().getMins()) {
            try {
                printConformation(directory, conformation, "source [" + i + "]");
                opt = ClusterMath.calc(conformation.getBits().getBites().toString(), true);
                printConformation(directory, opt, "[" + (i++) + "]");
                System.out.println(opt.getEnergy() + " -- " + opt.getBits().getBites());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private static void printConformation(File dir, Conformation conf, String prefixName) throws FileNotFoundException {
        String name = prefixName + " N" + config.getN() + " M" + config.getM() + " " + conf.getEnergy();
        PrintWriter out = new PrintWriter(dir.getAbsolutePath() + "\\" + name.replace(".", ",") + ".txt");
        out.print(conf);
        out.flush();
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
        String startConf = config.getSTART_CONF();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < startConf.length(); i++) {
            if (startConf.charAt(i) == '0') {
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
