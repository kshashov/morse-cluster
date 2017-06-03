package com.cluster;


import com.shashov.cluster.math.ClustersFinder;
import com.shashov.cluster.math.StronginTask;
import com.shashov.cluster.math.model.Conformation;
import com.shashov.cluster.math.model.Interval;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by envoy on 05.03.2017.
 */
public class ExecutorService {
    private static String DEFAULT_CONFIG = "config.json";

    public ExecutorService(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir") + File.separator;
        String configPath = userDir + DEFAULT_CONFIG;

        if (args.length > 0) {
            configPath = args[0].replace("\\", "\\\\");
        }

        Configuration.setupConfig(new BufferedReader(new FileReader(configPath)));
    }

    public void process(StronginTask.Progress progress, OnFinishCallBack finishCallBack) throws ExecutionException, InterruptedException, FileNotFoundException {
        long time = System.currentTimeMillis();

        ClustersFinder clustersFinder = new ClustersFinder();
        clustersFinder.process(Configuration.get(), progress, (mins) -> {
            try {
                saveResults(mins);
            } catch (FileNotFoundException e) {
                MainApp.logError(e);
            }
            finishCallBack.onFinish(mins, System.currentTimeMillis() - time);
        });
    }

    private List<Conformation> saveResults(List<Conformation> mins) throws FileNotFoundException {
        File directory = new File(Configuration.getOutputFolderName());
        if (!directory.exists()) {
            directory.mkdir();
        }

        int i = 1;
        for (Conformation conformation : mins) {
            saveConformation(directory, conformation, "[" + (i++) + "]");
        }
        return mins;
    }


    private void logIntervals(List<Interval> intervals) throws FileNotFoundException { //TODO use this
        MainApp.log.println("THREAD");
        for (Interval interval : intervals) {
            MainApp.log.println(interval);
        }
        MainApp.log.flush();
    }

    private void saveConformation(File dir, Conformation conf, String prefixName) throws FileNotFoundException {
        String name = prefixName + " N" + Configuration.get().getTaskParams().getN() + " M" + Configuration.get().getM() + " " + conf.getEnergy();
        PrintWriter out = new PrintWriter(dir.getAbsolutePath() + File.separator + name.replace(".", ",") + ".txt");
        out.print(conf);
        out.flush();
        out.close();
    }

    @FunctionalInterface
    public interface OnFinishCallBack {
        void onFinish(List<Conformation> results, long milliseconds);
    }
}
