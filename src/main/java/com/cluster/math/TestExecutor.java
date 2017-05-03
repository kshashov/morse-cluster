package com.cluster.math;

import com.cluster.StronginTask;
import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Interval;
import com.cluster.math.model.Vertex;
import com.cluster.math.utils.ClusterMath;
import com.cluster.math.utils.Config;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by envoy on 05.03.2017.
 */
public class TestExecutor {
    private static Config config;
    public static PrintStream log;

    public static Config getConfig() {
        return config;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long time = System.currentTimeMillis();
        String userDir = System.getProperty("user.dir") + File.separator;
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
        Bits a = new Bits(new StringBuilder(stronginZeros).append(stronginOnes));
        Bits b = new Bits(new StringBuilder(stronginOnes).append(stronginZeros));

        ExecutorService executor = Executors.newFixedThreadPool(config.getTHREADS_COUNT());
        ArrayList<FutureTask<Strongin>> tasks = new ArrayList<>(config.getTHREADS_COUNT());
        ArrayList<BigInteger> points = new ArrayList<>(config.getTHREADS_COUNT() + 1);
        points.add(a.getNumber());
        for (int i = 0; i < config.getTHREADS_COUNT() - 1; i++) {
            points.add(b.getNumber().subtract(a.getNumber()).divide(BigInteger.valueOf(config.getTHREADS_COUNT())).multiply(BigInteger.valueOf(i + 1)).add(a.getNumber()));
        }
        points.add(b.getNumber());
        for (int i = 0; i < config.getTHREADS_COUNT(); i++) {
            Strongin strongin = new Strongin(new Bits(config.getSTRONGIN_M(), points.get(i)), new Bits(config.getSTRONGIN_M(), points.get(i + 1)), config.getSTRONGIN_ITERATIONS(), config.getSTRONGIN_REPOSITORY_SIZE());
            FutureTask<Strongin> task = new FutureTask<Strongin>(new StronginTask(strongin, i));
            tasks.add(task);
            executor.execute(task);
        }
        Map<String, Conformation> output = new HashMap<>();
        int k = 1;
        for (FutureTask<Strongin> task : tasks) {
            Strongin strongin = task.get();
            String temp;
            for (Conformation variant : strongin.getRep().getMins()) {
                temp = variant.getBits().toString();
                if (!output.containsKey(temp)) {
                    output.put(temp, variant);
                } else {
                    if (variant.getEnergy() < output.get(temp).getEnergy()) {
                        output.put(temp, variant);
                    }
                }
            }
            logIntervals(strongin);
        }
        saveResults(output);
        executor.shutdown();
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s.");
    }

    private static void saveResults(Map<String, Conformation> map) {
        List<Conformation> output = new ArrayList<>(map.values());

        File directory = new File(config.getOUTPUT_FOLDERNAME());
        if (!directory.exists()) {
            directory.mkdir();
        }

        output.sort(new Comparator<Conformation>() {
            @Override
            public int compare(Conformation left, Conformation right) {
                if (left.getEnergy() == right.getEnergy()) {
                    return 0;
                }
                return left.getEnergy() > right.getEnergy() ? 1 : -1;
            }
        });
        Conformation opt;
        for (int i = 0; i < getConfig().getMINS_COUNT(); i++) {
            opt = ClusterMath.calc(output.get(i).getBits().getBites().toString(), true);
            try {
                saveConformation(directory, opt, "[" + (i + 1) + "]");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.printf("%20.15f -- %s\n", opt.getEnergy(), opt.getBits().getBites());
        }
    }

    private static void logIntervals(Strongin strongin) {
        strongin.getIntervals().sort(new Comparator<Interval>() {
            @Override
            public int compare(Interval left, Interval right) {
                return left.getA().getNumber().compareTo(right.getA().getNumber());
            }
        });

        TestExecutor.log.println("THREAD");
        for (Interval interval : strongin.getIntervals()) {
            TestExecutor.log.println(interval);
        }
        TestExecutor.log.flush();
    }

    private static void saveConformation(File dir, Conformation conf, String prefixName) throws FileNotFoundException {
        String name = prefixName + " N" + config.getN() + " M" + config.getM() + " " + conf.getEnergy();
        PrintWriter out = new PrintWriter(dir.getAbsolutePath() + File.separator + name.replace(".", ",") + ".txt");
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
