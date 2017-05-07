package com.cluster.math;

import com.cluster.Configuration;
import com.cluster.StronginTask;
import com.cluster.math.model.Bits;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Interval;
import com.cluster.math.utils.ClusterMath;
import com.cluster.math.utils.Config;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by envoy on 05.03.2017.
 */
public class ExecutorService {
    private static PrintStream log;
    private static String DEFAULT_CONFIG = "config.json";
    private static String DEFAULT_LOG = "log.txt";
    private static String DEFAULT_INPUT = "input.txt";
    private static String DEFAULT_OUTPUT = "output";

    public static PrintStream initLog() throws FileNotFoundException {

        if (log == null) {
            String userDir = System.getProperty("user.dir") + File.separator;
            String logPath = userDir + DEFAULT_LOG;
            log = new PrintStream(new File(logPath));
        }
        return log;
    }

    public static Config init(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir") + File.separator;
        String configPath = userDir + DEFAULT_CONFIG;

        String inputPath = userDir + DEFAULT_INPUT;
        String outputPath = userDir + DEFAULT_OUTPUT;

        if (args.length > 0) {
            configPath = args[0].replace("\\", "\\\\");
        }
        log = null;
        initLog();
        Configuration.setupConfig(new BufferedReader(new FileReader(configPath)), inputPath, outputPath);
        return Configuration.get();
    }

    public static void process(StronginTask.ProgressCallBack progressCallBack, OnFinishCallBack finishCallback) throws ExecutionException, InterruptedException, FileNotFoundException {
        if (Configuration.get() == null) {
            throw new RuntimeException("ExecutorService#init function is not be called");
        }

        long time = System.currentTimeMillis();
        //interval
        StringBuilder stronginOnes = new StringBuilder();
        for (int i = 0; i < Configuration.get().getSTRONGIN_N(); i++) {
            stronginOnes.append('1');
        }
        StringBuilder stronginZeros = new StringBuilder();
        for (int i = 0; i < Configuration.get().getSTRONGIN_M() - Configuration.get().getSTRONGIN_N(); i++) {
            stronginZeros.append('0');
        }
        Bits a = new Bits(new StringBuilder(stronginZeros).append(stronginOnes));
        Bits b = new Bits(new StringBuilder(stronginOnes).append(stronginZeros));

        java.util.concurrent.ExecutorService executor = Executors.newFixedThreadPool(Configuration.get().getTHREADS_COUNT());
        ArrayList<StronginTask> tasks = new ArrayList<>(Configuration.get().getTHREADS_COUNT());
        ArrayList<BigInteger> points = new ArrayList<>(Configuration.get().getTHREADS_COUNT() + 1);
        points.add(a.getNumber());
        for (int i = 0; i < Configuration.get().getTHREADS_COUNT() - 1; i++) {
            points.add(b.getNumber().subtract(a.getNumber()).divide(BigInteger.valueOf(Configuration.get().getTHREADS_COUNT())).multiply(BigInteger.valueOf(i + 1)).add(a.getNumber()));
        }
        points.add(b.getNumber());
        for (int i = 0; i < Configuration.get().getTHREADS_COUNT(); i++) {
            Strongin strongin = new Strongin(new Bits(Configuration.get().getSTRONGIN_M(), points.get(i)), new Bits(Configuration.get().getSTRONGIN_M(), points.get(i + 1)), Configuration.get().getSTRONGIN_ITERATIONS(), Configuration.get().getSTRONGIN_REPOSITORY_SIZE());
            StronginTask.ProgressCallBack progressCallBack1 = progressCallBack.clone();
            progressCallBack1.setId(i);
            StronginTask task = new StronginTask(strongin, progressCallBack1);
            tasks.add(task);
            executor.execute(task);
        }
        Map<String, Conformation> output = new HashMap<>();
        for (StronginTask task : tasks) {
            Strongin strongin = task.get();
            String key;
            for (Conformation variant : strongin.getRep().getMins()) {
                key = variant.getBits().getBites().toString();
                if (!output.containsKey(key)) {
                    output.put(key, variant);
                } else {
                    if (variant.getEnergy() < output.get(key).getEnergy()) {
                        output.put(key, variant);
                    }
                }
            }
            logIntervals(strongin);
            task.getProgressCallBack().onProgress(100);
        }
        progressCallBack.onFinish(10);
        executor.shutdown();
        ExecutorService.log.close();
        finishCallback.onFinish(saveResults(output, progressCallBack), System.currentTimeMillis() - time);
    }

    private static List<Conformation> saveResults(Map<String, Conformation> map, StronginTask.ProgressCallBack progressCallBack) throws FileNotFoundException {
        List<Conformation> output = new ArrayList<>();

        File directory = new File(Configuration.get().getOUTPUT_FOLDERNAME());
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (Conformation conformation : map.values()) {
            output.add(ClusterMath.calc(conformation.getBits().getBites().toString(), true));
        }
        progressCallBack.onFinish(50);
        output.sort(new Comparator<Conformation>() {
            @Override
            public int compare(Conformation left, Conformation right) {
                if (left.getEnergy() == right.getEnergy()) {
                    return 0;
                }
                return left.getEnergy() > right.getEnergy() ? 1 : -1;
            }
        });
        List<Conformation> mins = output.subList(0, Math.min(output.size(), Configuration.get().getMINS_COUNT()));
        int i = 1;
        for (Conformation conformation : mins) {
            saveConformation(directory, conformation, "[" + (i++) + "]");
            progressCallBack.onFinish(50 + 50 * ((i - 1) / mins.size()));
        }
        return mins;
    }


    private static void logIntervals(Strongin strongin) throws FileNotFoundException {
        initLog();
        strongin.getIntervals().sort(new Comparator<Interval>() {
            @Override
            public int compare(Interval left, Interval right) {
                return left.getA().getNumber().compareTo(right.getA().getNumber());
            }
        });

        ExecutorService.log.println("THREAD");
        for (Interval interval : strongin.getIntervals()) {
            ExecutorService.log.println(interval);
        }
        ExecutorService.log.flush();
    }

    private static void saveConformation(File dir, Conformation conf, String prefixName) throws FileNotFoundException {
        String name = prefixName + " N" + Configuration.get().getN() + " M" + Configuration.get().getM() + " " + conf.getEnergy();
        PrintWriter out = new PrintWriter(dir.getAbsolutePath() + File.separator + name.replace(".", ",") + ".txt");
        out.print(conf);
        out.flush();
        out.close();
    }

    public static void logError(Exception e) {
        try {
            initLog();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        if (ExecutorService.log != null) {
            e.printStackTrace(ExecutorService.log);
            ExecutorService.log.close();
        }
    }

    public interface OnFinishCallBack {
        void onFinish(List<Conformation> results, long milliseconds);
    }
}
